/*
 *	JorbisFormatConversionProvider.java
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *  Copyright (c) 2001 by Florian Bomers <florian@bome.com>
 *
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */


package	org.tritonus.sampled.convert.vorbis;


import	java.io.DataInputStream;
import	java.io.InputStream;
import	java.io.IOException;

import	java.util.ArrayList;
import	java.util.Arrays;
import	java.util.Iterator;
import	java.util.List;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioSystem;

import	org.tritonus.share.TDebug;
import	org.tritonus.share.sampled.TConversionTool;
import	org.tritonus.share.sampled.convert.TAsynchronousFilteredAudioInputStream;
import	org.tritonus.share.sampled.convert.TEncodingFormatConversionProvider;
import	org.tritonus.share.sampled.convert.TSimpleFormatConversionProvider;
import	org.tritonus.share.sampled.AudioFormats;
import	org.tritonus.share.sampled.Encodings;

import com.jcraft.jogg.SyncState;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.Packet;
import com.jcraft.jorbis.Info;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Block;



/**	Pure-java decoder for ogg vorbis streams.
	The FormatConversionProvider uses the pure-java
	ogg vorbis decoder from www.jcraft.com/jorbis/.

	@author Matthias Pfisterer
*/
public class JorbisFormatConversionProvider
	extends		TEncodingFormatConversionProvider
{
	// only used as abbreviation
	private static final AudioFormat.Encoding	VORBIS = Encodings.getEncoding("VORBIS");
	private static final AudioFormat.Encoding	PCM_SIGNED = Encodings.getEncoding("PCM_SIGNED");


	private static final AudioFormat[]	INPUT_FORMATS =
	{
		// mono
		// TODO: mechanism to make the double specification with
		// different endianess obsolete.
		new AudioFormat(VORBIS, -1.0F, -1, 1, -1, -1.0F, false),
		new AudioFormat(VORBIS, -1.0F, -1, 1, -1, -1.0F, true),
		// stereo
		new AudioFormat(VORBIS, -1.0F, -1, 2, -1, -1.0F, false),
		new AudioFormat(VORBIS, -1.0F, -1, 2, -1, -1.0F, true),
		// TODO: other channel configurations
	};


	private static final AudioFormat[]	OUTPUT_FORMATS =
	{
		// mono, 16 bit signed
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 1, 2, -1.0F, false),
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 1, 2, -1.0F, true),
		// stereo, 16 bit signed
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 2, 4, -1.0F, false),
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 2, 4, -1.0F, true),
		// TODO: other channel configurations
	};




	/**	Constructor.
	 */
	public JorbisFormatConversionProvider()
	{
		super(Arrays.asList(INPUT_FORMATS),
		      Arrays.asList(OUTPUT_FORMATS)/*,
						     true, // new behaviour
						     false*/); // bidirectional .. constants UNIDIR../BIDIR..?
	}



	public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream audioInputStream)
	{
		/** The AudioInputStream to return.
		 */
		AudioInputStream	convertedAudioInputStream = null;

		if (TDebug.TraceAudioConverter)
		{
			TDebug.out("JorbisFormatConversionProvider.getAudioInputStream():");
			TDebug.out("checking if conversion supported");
			TDebug.out("from: " + audioInputStream.getFormat());
			TDebug.out("to: " + targetFormat);
		}

		// what is this ???
		targetFormat=getDefaultTargetFormat(targetFormat, audioInputStream.getFormat());
		if (isConversionSupported(targetFormat,
					  audioInputStream.getFormat()))
		{
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("JorbisFormatConversionProvider.getAudioInputStream():");
				TDebug.out("conversion supported; trying to create DecodedJorbisAudioInputStream");
			}
			convertedAudioInputStream = new
				DecodedJorbisAudioInputStream(
					targetFormat,
					audioInputStream);
		}
		else
		{
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("JorbisFormatConversionProvider.getAudioInputStream():");
				TDebug.out("conversion not supported; throwing IllegalArgumentException");
			}
			throw new IllegalArgumentException("conversion not supported");
		}
		return convertedAudioInputStream;
	}



	// TODO: recheck !!
	protected AudioFormat getDefaultTargetFormat(AudioFormat targetFormat, AudioFormat sourceFormat)
	{
		AudioFormat	newTargetFormat = null;
		// return first of the matching formats
		// pre-condition: the predefined target formats (FORMATS2) must be well-defined !
		Iterator iterator=getCollectionTargetFormats().iterator();
		while (iterator.hasNext())
		{
			AudioFormat format = (AudioFormat) iterator.next();
			if (AudioFormats.matches(targetFormat, format))
			{
				newTargetFormat = format;
			}
		}
		if (newTargetFormat == null)
		{
			throw new IllegalArgumentException("conversion not supported");
		}
		return newTargetFormat;
	}
		


	/**	AudioInputStream returned on decoding of ogg vorbis.
		An instance of this class is returned if you call
		AudioSystem.getAudioInputStream(AudioFormat, AudioInputStream)
		to decode an ogg/vorbis stream. This class contains the logic
		of maintaining buffers and calling the decoder.
	*/
	/* Class should be private, but is public due to a bug (?) in the
	   aspectj compiler. */
	/*private*/public static class DecodedJorbisAudioInputStream
	extends TAsynchronousFilteredAudioInputStream
	{
		public static boolean		DEBUG = false;

		private InputStream		oggBitStream_ = null;

		// Ogg structures
		private SyncState		oggSyncState_ = null;
  		private StreamState		oggStreamState_ = null;
  		private Page			oggPage_ = null;
  		private Packet			oggPacket_ = null;

		// Vorbis structures
  		private Info			vorbisInfo = null;
  		private Comment			vorbisComment = null;
  		private DspState		vorbisDspState = null;
  		private Block			vorbisBlock = null;

  		private int			bufferMultiple_ = 4;
  		private int			bufferSize_ = bufferMultiple_ * 256 * 2;
  		private int			convsize = bufferSize_ * 2;
  		private byte[]			convbuffer = new byte[convsize];
  		private byte[]			buffer = null;
  		private int			bytes = 0;
  		private int			rate = 0;
 	 	private int			channels = 0;
  		private List			songComments_ = new ArrayList();
		private double[][][]		_pcm = null;
		private float[][][]		_pcmf = null;
		private int[]			_index = null;
		private int			index = 0;
		private int			i = 0;

		private int			loopid = 1;
		private int			eos = 0;
		private boolean			streamStillHasData = true;



		/**
		 * Constructor.
		 */
		public DecodedJorbisAudioInputStream(AudioFormat outputFormat, AudioInputStream bitStream)
		{
			/*
			 */
			super(outputFormat, AudioSystem.NOT_SPECIFIED);
			this.oggBitStream_ = bitStream;
			loopid = 1;
			init_jorbis();
			index = 0;
		}



		/**
		 * Initializes all the jOrbis and jOgg vars that are used for song playback.
		 */
		private void init_jorbis()
		{
			oggSyncState_ = new SyncState();
			oggStreamState_ = new StreamState();
			oggPage_ = new Page();
			oggPacket_ = new Packet();
			vorbisInfo = new Info();
			vorbisComment = new Comment();
			vorbisDspState = new DspState();
			vorbisBlock = new Block(vorbisDspState);
			buffer = null;
			bytes = 0;
			oggSyncState_.init();
		}


		/**
		 * Main loop.
		 */
		public void execute()
		{
			// This code was developed by the jCraft group, slightly modifed by jOggPlayer developer
			// and adapted by E.B from JavaZOOM to suit to JavaSound SPI.
			// loop 1
			if (streamStillHasData == true)
			{
				if (loopid == 1)
				{
					if (DEBUG) System.err.println("loop1");
					eos = 0;
					// Headers (+ Comments).
					try
					{
						readHeaders();
					}	catch (IOException ioe)
					{
						streamStillHasData = false;
						return;
					}
					loopid = 2;
				}
				// loop 2
				switch (eos)
					//while (eos == 0)
				{
				case 0:
					if (DEBUG) System.err.println("loop2");
					// loop 3
					switch (eos)
						//while (eos == 0)
					{
					case 0:
						int result = oggSyncState_.pageout(oggPage_);
						if (DEBUG) System.err.println("loop3:"+result);
						if (result == 0)
						{
							loopid = 2;
							break;
						} // need more data
						if (result == -1)
						{ // missing or corrupt data at this page position
							if (DEBUG) System.err.println("Corrupt or missing data in bitstream; " + "continuing...");
						}
						else
						{
							oggStreamState_.pagein(oggPage_);
							// Decoding !
							if (DEBUG) System.err.println("Decoding");
							while (true)
							{
								result = oggStreamState_.packetout(oggPacket_);
								if (result == 0)
								{
									break;
								} // need more data
								if (result == -1)
								{ // missing or corrupt data at this page position
									// no reason to complain; already complained above
								}
								else
								{
									// we have a packet.  Decode it
									int samples;
									if (vorbisBlock.synthesis(oggPacket_) == 0)
									{ // test for success!
										vorbisDspState.synthesis_blockin(vorbisBlock);
									}
									while ((samples = vorbisDspState.synthesis_pcmout(_pcmf, _index)) > 0)
									{
										double[][] pcm = _pcm[0];
										float[][] pcmf = _pcmf[0];
										boolean clipflag = false;
										int bout = (samples < convsize ? samples : convsize);
										double fVal = 0.0;
										// convert doubles to 16 bit signed ints (host order) and
										// interleave
										for (i = 0; i < vorbisInfo.channels; i++)
										{
											int pointer = i * 2;
											//int ptr=i;
											int mono = _index[i];
											for (int j = 0; j < bout; j++)
											{
												fVal = (float) pcmf[i][mono + j] * 32767.;
												int val = (int) (fVal);
												if (val > 32767)
												{
													val = 32767;
													clipflag = true;
												}
												if (val < -32768)
												{
													val = -32768;
													clipflag = true;
												}
												if (val < 0)
												{
													val = val | 0x8000;
												}
												convbuffer[pointer] = (byte) (val);
												convbuffer[pointer + 1] = (byte) (val >>> 8);
												pointer += 2 * (vorbisInfo.channels);
											}
										}
										m_circularBuffer.write(convbuffer, 0, 2 * vorbisInfo.channels * bout);
										//outputLine.write(convbuffer, 0, 2 * vorbisInfo.channels * bout);
										vorbisDspState.synthesis_read(bout);
									}
								}
							}
							if (oggPage_.eos() != 0)
							{
								eos = 1;
							}
						} // end else.
						loopid = 3;
						break;
					default:
						loopid = 2;
						break;
					} // end loop3 // end switch(eos)
					if (loopid == 3) return;

					if (eos == 0)
					{
						index = oggSyncState_.buffer(bufferSize_);
						buffer = oggSyncState_.data;
						bytes = readFromStream(buffer, index, bufferSize_);
						if (DEBUG) System.err.println("More data : "+bytes);
						if (bytes == -1)
						{
							if (DEBUG) System.err.println("Ogg Stream empty.");
							streamStillHasData = false;
							eos = 1;
							//break;
						}
						else
						{
							oggSyncState_.wrote(bytes);
							if (bytes == 0)
							{
								eos = 1;
							}
						}
					}
					loopid = 2;
					break;
				default:
					loopid = 1;
					break;
				} // end loop 2 // end switch(eos)
				if (loopid == 2) return;

				oggStreamState_.clear();
				vorbisBlock.clear();
				vorbisDspState.clear();
				vorbisInfo.clear();
			} // end loop 1
			else // no more data
			{
				oggSyncState_.clear();
				if (DEBUG) System.out.println("Done Song.");
				try
				{
					if (oggBitStream_ != null)
					{
						oggBitStream_.close();
					}
					m_circularBuffer.close();
				}
				catch (Exception e)
				{
					if (DEBUG) e.printStackTrace();
				}
			}
		}

		/**
		 * Reads headers and comments.
		 */
		private void readHeaders() throws IOException
		{
			if (DEBUG) System.err.println("readHeaders()");
			index = oggSyncState_.buffer(bufferSize_);
			buffer = oggSyncState_.data;
			bytes = readFromStream(buffer, index, bufferSize_);
			if (bytes == -1)
			{
				if (DEBUG) System.err.println("Cannot get any data from selected Ogg bitstream.");
				throw new IOException("Cannot get any data from selected Ogg bitstream.");
			}
			oggSyncState_.wrote(bytes);
			if (oggSyncState_.pageout(oggPage_) != 1)
			{
				if (bytes < bufferSize_)
				{
					throw new IOException("EOF");
				}
				if (DEBUG) System.err.println("Input does not appear to be an Ogg bitstream.");
				throw new IOException("Input does not appear to be an Ogg bitstream.");
			}
			oggStreamState_.init(oggPage_.serialno());
			vorbisInfo.init();
			vorbisComment.init();
			if (oggStreamState_.pagein(oggPage_) < 0)
			{
				// error; stream version mismatch perhaps
				if (DEBUG) System.err.println("Error reading first page of Ogg bitstream data.");
				throw new IOException("Error reading first page of Ogg bitstream data.");
			}
			if (oggStreamState_.packetout(oggPacket_) != 1)
			{
				// no page? must not be vorbis
				if (DEBUG) System.err.println("Error reading initial header packet.");
				throw new IOException("Error reading initial header packet.");
			}
			if (vorbisInfo.synthesis_headerin(vorbisComment, oggPacket_) < 0)
			{
				// error case; not a vorbis header
				if (DEBUG) System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
				throw new IOException("This Ogg bitstream does not contain Vorbis audio data.");
			}
			//int i = 0;
			i = 0;
			while (i < 2)
			{
				while (i < 2)
				{
					int result = oggSyncState_.pageout(oggPage_);
					if (result == 0)
					{
						break;
					} // Need more data
					if (result == 1)
					{
						oggStreamState_.pagein(oggPage_);
						while (i < 2)
						{
							result = oggStreamState_.packetout(oggPacket_);
							if (result == 0)
							{
								break;
							}
							if (result == -1)
							{
								if (DEBUG) System.err.println("Corrupt secondary header.  Exiting.");
								throw new IOException("Corrupt secondary header.  Exiting.");
							}
							vorbisInfo.synthesis_headerin(vorbisComment, oggPacket_);
							i++;
						}
					}
				}
				index = oggSyncState_.buffer(bufferSize_);
				buffer = oggSyncState_.data;
				bytes = readFromStream(buffer, index, bufferSize_);
				if (bytes == -1)
				{
					break;
				}
				if (bytes == 0 && i < 2)
				{
					if (DEBUG) System.err.println("End of file before finding all Vorbis  headers!");
					throw new IOException("End of file before finding all Vorbis  headers!");
				}
				oggSyncState_.wrote(bytes);
			}

			byte[][] ptr = vorbisComment.user_comments;
			String currComment = "";
			songComments_.clear();
			for (int j = 0; j < ptr.length; j++)
			{
				if (ptr[j] == null)
				{
					break;
				}
				currComment = (new String(ptr[j], 0, ptr[j].length - 1)).trim();
				songComments_.add(currComment);
				if (currComment.toUpperCase().startsWith("ARTIST"))
				{
					String artistLabelValue = currComment.substring(7);
				}
				else if (currComment.toUpperCase().startsWith("TITLE"))
				{
					String titleLabelValue = currComment.substring(6);
					String miniDragLabel = currComment.substring(6);
				}
				if (DEBUG) System.err.println("Comment: " + currComment);
			}
			currComment = "Bitstream: " + vorbisInfo.channels + " channel," + vorbisInfo.rate + "Hz";
			songComments_.add(currComment);
			if (DEBUG) System.err.println(currComment);
			if (DEBUG) currComment = "Encoded by: " + new String(vorbisComment.vendor, 0, vorbisComment.vendor.length - 1);
			songComments_.add(currComment);
			if (DEBUG) System.err.println(currComment);
			convsize = bufferSize_ / vorbisInfo.channels;
			vorbisDspState.synthesis_init(vorbisInfo);
			vorbisBlock.init(vorbisDspState);
			_pcm = new double[1][][];
			_pcmf = new float[1][][];
			_index = new int[vorbisInfo.channels];
		}

		/**
		 * Reads from the oggBitStream_ a specified number of Bytes(bufferSize_) worth
		 * starting at index and puts them in the specified buffer[].
		 *
		 * @param buffer
		 * @param index
		 * @param bufferSize_
		 * @return             the number of bytes read or -1 if error.
		 */
		private int readFromStream(byte[] buffer, int index, int bufferSize_)
		{
			int bytes = 0;
			try
			{
				bytes = oggBitStream_.read(buffer, index, bufferSize_);
			}
			catch (Exception e)
			{
				if (DEBUG) System.out.println("Cannot Read Selected Song");
				bytes = -1;
			}
			return bytes;
		}

		/**
		 *
		 */
		private boolean isBigEndian()
		{
			return getFormat().isBigEndian();
		}

		/**
		 *
		 */
		public void close() throws IOException
		{
			super.close();
			oggBitStream_.close();
		}

	}
}



/*** JorbisFormatConversionProvider.java ***/
