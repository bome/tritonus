/*
 *	VorbisFormatConversionProvider.java
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
 *  Copyright (c) 2001 by Florian Bomers <florian@bome.com>
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
 */

package org.tritonus.sampled.convert.vorbis;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.tritonus.share.TDebug;
import org.tritonus.lowlevel.ogg.StreamState;
import org.tritonus.lowlevel.ogg.Packet;
import org.tritonus.lowlevel.ogg.Page;
import org.tritonus.lowlevel.vorbis.Block;
import org.tritonus.lowlevel.vorbis.Comment;
import org.tritonus.lowlevel.vorbis.DspState;
import org.tritonus.lowlevel.vorbis.Info;
// import org.tritonus.share.sampled.TConversionTool;
import org.tritonus.share.sampled.convert.TAsynchronousFilteredAudioInputStream;
import org.tritonus.share.sampled.convert.TEncodingFormatConversionProvider;
import org.tritonus.share.sampled.AudioFormats;
import org.tritonus.share.sampled.Encodings;



/**	ConversionProvider for ogg vorbis encoding.
	This FormatConversionProvider uses the native libraries libogg,
	libvorbis and libvorbisenc to implement encoding to ogg vorbis.

	@author Matthias Pfisterer
*/
public class VorbisFormatConversionProvider
	extends TEncodingFormatConversionProvider
{
	// only used as abbreviation
	private static final AudioFormat.Encoding	VORBIS = Encodings.getEncoding("VORBIS");
	private static final AudioFormat.Encoding	PCM_SIGNED = Encodings.getEncoding("PCM_SIGNED");


	private static final AudioFormat[]	INPUT_FORMATS =
	{
		// mono, 16 bit signed
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 1, 2, -1.0F, false),
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 1, 2, -1.0F, true),
		// stereo, 16 bit signed
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 2, 4, -1.0F, false),
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 2, 4, -1.0F, true),
		// TODO: other channel configurations
	};


	private static final AudioFormat[]	OUTPUT_FORMATS =
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



	/**	Constructor.
	 */
	public VorbisFormatConversionProvider()
	{
		super(Arrays.asList(INPUT_FORMATS),
		      Arrays.asList(OUTPUT_FORMATS)/*,
						     true, // new behaviour
						     false*/); // bidirectional .. constants UNIDIR../BIDIR..?
		if (TDebug.TraceAudioConverter) { TDebug.out("VorbisFormatConversionProvider.<init>(): begin"); }
		if (TDebug.TraceAudioConverter) { TDebug.out("VorbisFormatConversionProvider.<init>(): end"); }
	}



	public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream audioInputStream)
	{
		/** The AudioInputStream to return.
		 */
		AudioInputStream	convertedAudioInputStream = null;

		if (TDebug.TraceAudioConverter)
		{
			TDebug.out("VorbisFormatConversionProvider.getAudioInputStream():");
			TDebug.out("checking if conversion supported");
			TDebug.out("from: " + audioInputStream.getFormat());
			TDebug.out("to: " + targetFormat);
		}

		// what is this ???
		targetFormat = getDefaultTargetFormat(targetFormat, audioInputStream.getFormat());
		if (isConversionSupported(targetFormat,
					  audioInputStream.getFormat()))
		{
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("VorbisFormatConversionProvider.getAudioInputStream():");
				TDebug.out("conversion supported; trying to create DecodedJorbisAudioInputStream");
			}
			convertedAudioInputStream = new
				EncodedVorbisAudioInputStream(
					targetFormat,
					audioInputStream);
		}
		else
		{
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("VorbisFormatConversionProvider.getAudioInputStream():");
				TDebug.out("conversion not supported; throwing IllegalArgumentException");
			}
			throw new IllegalArgumentException("conversion not supported");
		}
		return convertedAudioInputStream;
	}



	protected AudioFormat getDefaultTargetFormat(AudioFormat targetFormat, AudioFormat sourceFormat)
	{
		if (TDebug.TraceAudioConverter) { TDebug.out("VorbisFormatConversionProvider.getDefaultTargetFormat(): target format: " + targetFormat); }
		if (TDebug.TraceAudioConverter) { TDebug.out("VorbisFormatConversionProvider.getDefaultTargetFormat(): source format: " + sourceFormat); }
		AudioFormat	newTargetFormat = null;
		// return first of the matching formats
		// pre-condition: the predefined target formats (FORMATS2) must be well-defined !
		Iterator iterator = getCollectionTargetFormats().iterator();
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
		if (TDebug.TraceAudioConverter) { TDebug.out("VorbisFormatConversionProvider.getDefaultTargetFormat(): new target format: " + newTargetFormat); }
		// hacked together...
		// ... only works for PCM target encoding ...
		newTargetFormat = new AudioFormat(targetFormat.getEncoding(),
						  sourceFormat.getSampleRate(),
						  newTargetFormat.getSampleSizeInBits(),
						  newTargetFormat.getChannels(),
						  newTargetFormat.getFrameSize(),
						  sourceFormat.getSampleRate(),
						  newTargetFormat.isBigEndian());
		if (TDebug.TraceAudioConverter) { TDebug.out("VorbisFormatConversionProvider.getDefaultTargetFormat(): really new target format: " + newTargetFormat); }
		return newTargetFormat;
	}
		


	/**	AudioInputStream returned on encoding to ogg vorbis.
		An instance of this class is returned if you call
		AudioSystem.getAudioInputStream(AudioFormat, AudioInputStream)
		to encode a PCM stream. This class contains the logic
		of maintaining buffers and calling the encoder.
	 */
	public static class EncodedVorbisAudioInputStream
		extends TAsynchronousFilteredAudioInputStream
	{
		/** How many PCM frames to encode at once.
		 */
		private static final int	READ = 1024;

		private AudioInputStream	m_decodedStream;
		private byte[]			m_abReadbuffer;

		private StreamState		os;
		private Page			og;
		private Packet			op;
  
		private Info			vi;
		private Comment			vc;
		private DspState		vd;
		private Block			vb;





		public EncodedVorbisAudioInputStream(
			AudioFormat outputFormat,
			AudioInputStream inputStream)
		{
			super(outputFormat,
			      AudioSystem.NOT_SPECIFIED,
			      262144, 16384);
			if (TDebug.TraceAudioConverter) { TDebug.out("EncodedVorbisAudioInputStream.<init>(): begin"); }
			m_decodedStream = inputStream;
			m_abReadbuffer = new byte[READ * getFrameSize()];

			String	strUseVBR = System.getProperty("tritonus.vorbis.usevbr", "true");
			boolean	bUseVBR = strUseVBR.toLowerCase().equals("true");
			String	strQuality = System.getProperty("tritonus.vorbis.quality", "0.1");
			float	fQuality = Float.parseFloat(strQuality);
			String	strMaxBitrate = System.getProperty("tritonus.vorbis.maxbitrate", "256");
			int	nMaxBitrate = Integer.parseInt(strMaxBitrate);
			String	strNominalBitrate = System.getProperty("tritonus.vorbis.nominalbitrate", "128");
			int	nNominalBitrate = Integer.parseInt(strNominalBitrate);
			String	strMinBitrate = System.getProperty("tritonus.vorbis.minbitrate", "32");
			int	nMinBitrate = Integer.parseInt(strMinBitrate);

			os = new StreamState();
			og = new Page();
			op = new Packet();
  
			vi = new Info();
			vc = new Comment();
			vd = new DspState();
			vb = new Block();

			vi.init();

			int	nSampleRate = (int) inputStream.getFormat().getSampleRate();
			if (bUseVBR)
			{
				vi.encodeInitVBR(getChannels(),
						 nSampleRate,
						 fQuality);
			}
			else
			{
				vi.encodeInit(getChannels(),
					      nSampleRate,
					      nMaxBitrate,
					      nNominalBitrate,
					      nMinBitrate);
			}

			vc.init();
			vc.addTag("ENCODER","Tritonus libvorbis wrapper");

			vd.init(vi);
			vb.init(vd);

			Random random = new Random(System.currentTimeMillis());
			os.init(random.nextInt());

			Packet header = new Packet();
			Packet header_comm = new Packet();
			Packet header_code = new Packet();

			vd.headerOut(vc, header, header_comm, header_code);
			os.packetIn(header);
			os.packetIn(header_comm);
			os.packetIn(header_code);

			while (true)
			{
				int result = os.flush(og);
				if(result == 0)
				{
					break;
				}
				getCircularBuffer().write(og.getHeader());
				getCircularBuffer().write(og.getBody());
			}

			if (TDebug.TraceAudioConverter) { TDebug.out("EncodedVorbisAudioInputStream.<init>(): end"); }
		}



		public void execute()
		{
			if (TDebug.TraceAudioConverter) { TDebug.out(">EncodedVorbisAudioInputStream.execute(): begin"); }
			boolean		eos = false;

			int	nFrameSize = getFrameSize();
			int	nChannels = getChannels();
			boolean	bBigEndian = isBigEndian();
			int	nBytesPerSample = nFrameSize / nChannels;
			int	nSampleSizeInBits = nBytesPerSample * 8;
			float	fScale = (float) Math.pow(2.0, nSampleSizeInBits - 1);
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("frame size: " + nFrameSize);
				TDebug.out("channels: " + nChannels);
				TDebug.out("big endian: " + bBigEndian);
				TDebug.out("sample size (bits): " + nSampleSizeInBits);
				TDebug.out("bytes per sample: " + nBytesPerSample);
				TDebug.out("scale: " + fScale);
			}

			while (!eos && writeMore())
			{
				int	bytes;
				try
				{
					bytes = m_decodedStream.read(m_abReadbuffer);
					if (TDebug.TraceAudioConverter) { TDebug.out("read from PCM stream: " + bytes); }
				}
				catch (IOException e)
				{
					if (TDebug.TraceAllExceptions || TDebug.TraceAudioConverter) { TDebug.out(e); }
					os.clear();
					vb.clear();
					vd.clear();
					vc.clear();
					vi.clear();
					try
					{
						close();
					}
					catch (IOException e1)
					{
						if (TDebug.TraceAllExceptions || TDebug.TraceAudioConverter) { TDebug.out(e1); }
					}
					if (TDebug.TraceAudioConverter) { TDebug.out("<"); }
					return;
				}

				if (bytes == 0 &&  bytes == -1)
				{
					if (TDebug.TraceAudioConverter) { TDebug.out("EOS reached; calling DspState.write(0)"); }
					vd.write(null, 0);
				}
				else
				{
					int	nFrames = bytes / nFrameSize;
					if (TDebug.TraceAudioConverter) { TDebug.out("processing frames: " + nFrames); }
					float[][] buffer = new float[nChannels][READ];
					/* uninterleave samples */
					for (int i = 0; i < nFrames; i++)
					{
						for (int nChannel = 0; nChannel < nChannels; nChannel++)
						{
							int	nSample;
							nSample = bytesToInt16(m_abReadbuffer, i * nFrameSize + nChannel * nBytesPerSample, bBigEndian);
							buffer[nChannel][i] = nSample / fScale;
						}
					}
					vd.write(buffer, nFrames);
				}

				while (vd.blockOut(vb) == 1)
				{
					vb.analysis(null);
					vb.addBlock();
					while (vd.flushPacket(op) != 0)
					{
						os.packetIn(op);
						while (!eos /*&& writeMore()*/)
						{
							int result = os.pageOut(og);
							if(result == 0)
							{
								break;
							}
							getCircularBuffer().write(og.getHeader());
							getCircularBuffer().write(og.getBody());

							if (og.isEos())
							{
								eos = true;
								if (TDebug.TraceAudioConverter) { TDebug.out("page has detected EOS"); }
							}
						}
					}
				}
			}

			if (eos)
			{
				if (TDebug.TraceAudioConverter) { TDebug.out("EOS; shutting down encoder"); }
				os.clear();
				vb.clear();
				vd.clear();
				vc.clear();
				vi.clear();
				try
				{
					close();
				}
				catch (IOException e)
				{
					if (TDebug.TraceAllExceptions || TDebug.TraceAudioConverter) { TDebug.out(e); }
				}
			}
			if (TDebug.TraceAudioConverter) { TDebug.out("<EncodedVorbisAudioInputStream.execute(): end"); }
		}



		private int getChannels()
		{
			return m_decodedStream.getFormat().getChannels();
		}



		private int getFrameSize()
		{
			return m_decodedStream.getFormat().getFrameSize();
		}



		private boolean isBigEndian()
		{
			return m_decodedStream.getFormat().isBigEndian();
		}



		public void close()
			throws	IOException
		{
			super.close();
			m_decodedStream.close();
		}



		// copied from TConversionTool
		private static int bytesToInt16(byte[] buffer,
						int byteOffset,
						boolean bigEndian)
		{
			return bigEndian ?
				((buffer[byteOffset]<<8) | (buffer[byteOffset+1] & 0xFF)) :
				((buffer[byteOffset+1]<<8) | (buffer[byteOffset] & 0xFF));
		}
	}
}



/*** VorbisFormatConversionProvider.java ***/
