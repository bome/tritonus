/*
 *	VorbisAudioFileReader.java
 */

/*
 *  Copyright (c) 2001 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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


package	org.tritonus.sampled.file.vorbis;


import	java.io.InputStream;
import	java.io.IOException;

import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.UnsupportedAudioFileException;

import	org.tritonus.share.TDebug;
import	org.tritonus.share.sampled.Encodings;
import	org.tritonus.share.sampled.AudioFileTypes;
import	org.tritonus.share.sampled.file.TAudioFileFormat;
import	org.tritonus.share.sampled.file.TAudioFileReader;

import	com.jcraft.jogg.SyncState;
import	com.jcraft.jogg.StreamState;
import	com.jcraft.jogg.Page;
import	com.jcraft.jogg.Packet;
import	com.jcraft.jorbis.Info;
import	com.jcraft.jorbis.Comment;
import	com.jcraft.jorbis.DspState;
import	com.jcraft.jorbis.Block;



/**
 * @author Matthias Pfisterer
 */
public class VorbisAudioFileReader
	extends	TAudioFileReader
{
	// Note: this value is only an estimate
	private static final int	MARK_LIMIT = 10000;



	public VorbisAudioFileReader()
	{
		super(MARK_LIMIT, true);
	}



	protected AudioFileFormat getAudioFileFormat(InputStream inputStream, long lFileSizeInBytes)
		throws	UnsupportedAudioFileException, IOException
	{
		if (TDebug.TraceAudioFileReader) { TDebug.out("VorbisAudioFileReader.getAudioFileFormat(): begin"); }
		int	convsize = 4096 * 2;
		byte[]	convbuffer = new byte[convsize];

		// sync and verify incoming physical bitstream
		SyncState	oggSyncState = new SyncState();

		// take physical pages, weld into a logical stream of packets
		StreamState	oggStreamState = new StreamState();

		// one Ogg bitstream page.  Vorbis packets are inside
		Page		oggPage = new Page();

		// one raw packet of data for decode
		Packet		oggPacket = new Packet();

		// struct that stores all the static vorbis bitstream settings
		Info		vi = new Info();

		// struct that stores all the bitstream user comments
		Comment		vc = new Comment();

		// central working state for the packet->PCM decoder
		DspState	vd = new DspState();

		// local working space for packet->PCM decode
		Block		vb = new Block(vd);

		byte[]		buffer;
		int		bytes = 0;

		// Decode setup

		oggSyncState.init(); // Now we can read pages

		// while(true){ // we repeat if the bitstream is chained
		int eos=0;

		// grab some data at the head of the stream.  We want the first page
		// (which is guaranteed to be small and only contain the Vorbis
		// stream initial header) We need the first page to get the stream
		// serialno.

		// submit a 4k block to libvorbis' Ogg layer
		int	index = oggSyncState.buffer(4096);
		buffer = oggSyncState.data;
		bytes = inputStream.read(buffer, index, 4096);
		oggSyncState.wrote(bytes);
    
		// Get the first page.
		if (oggSyncState.pageout(oggPage) != 1)
		{
			// have we simply run out of data?  If so, we're done.
			if (bytes < 4096)
			{
				// IDEA: throw EOFException?
				throw new UnsupportedAudioFileException("not a Vorbis stream: ended prematurely");
			}
      
			// error case.  Must not be Vorbis data
			throw new UnsupportedAudioFileException("not a Vorbis stream: not in Ogg bitstream format");
		}

		// Get the serial number and set up the rest of decode.
		// serialno first; use it to set up a logical stream
		oggStreamState.init(oggPage.serialno());

		// extract the initial header from the first page and verify that the
		// Ogg bitstream is in fact Vorbis data

		// I handle the initial header first instead of just having the code
		// read all three Vorbis headers at once because reading the initial
		// header is an easy way to identify a Vorbis bitstream and it's
		// useful to see that functionality seperated out.

		vi.init();
		vc.init();
		if (oggStreamState.pagein(oggPage) < 0)
		{
			// error; stream version mismatch perhaps
			throw new UnsupportedAudioFileException("not a Vorbis stream: can't read first page of Ogg bitstream data");
		}
    
		if (oggStreamState.packetout(oggPacket) != 1)
		{
			// no page? must not be vorbis
			throw new UnsupportedAudioFileException("not a Vorbis stream: can't read initial header packet");
		}

		if (vi.synthesis_headerin(vc,oggPacket) < 0)
		{
			// error case; not a vorbis header
			throw new UnsupportedAudioFileException("not a Vorbis stream: does not contain Vorbis audio data");
		}

		// At this point, we're sure we're Vorbis.  We've set up the logical
		// (Ogg) bitstream decoder.  Get the comment and codebook headers and
		// set up the Vorbis decoder
    
		// The next two packets in order are the comment and codebook headers.
		// They're likely large and may span multiple pages.  Thus we reead
		// and submit data until we get our two pacakets, watching that no
		// pages are missing.  If a page is missing, error out; losing a
		// header page is the only place where missing data is fatal. */
    
		int	i = 0;
		while (i < 2)
		{
			while (i < 2)
			{
				int	result = oggSyncState.pageout(oggPage);
				if (result == 0)
				{
					break; // Need more data
				}
				// Don't complain about missing or corrupt data yet.  We'll
				// catch it at the packet output phase

				if (result == 1)
				{
					oggStreamState.pagein(oggPage); // we can ignore any errors here
					// as they'll also become apparent
					// at packetout
					while (i < 2)
					{
						result = oggStreamState.packetout(oggPacket);
						if (result == 0)
						{
							break;
						}
						if (result == -1)
						{
							// Uh oh; data at some point was corrupted or missing!
							// We can't tolerate that in a header.  Die.
							throw new UnsupportedAudioFileException("not a Vorbis stream: corrupt secondary header");
						}
						vi.synthesis_headerin(vc,oggPacket);
						i++;
					}
				}
			}
			// no harm in not checking before adding more
			index = oggSyncState.buffer(4096);
			buffer = oggSyncState.data; 
			bytes = inputStream.read(buffer, index, 4096);
			if(bytes == 0 && i < 2)
			{
				// IDEA: throw EOFException?
				throw new UnsupportedAudioFileException("not a Vorbis stream: ended before finding all Vorbis headers");
			}
			oggSyncState.wrote(bytes);
		}

		// Throw the comments plus a few lines about the bitstream we're
		// decoding
		byte[][]	ptr = vc.user_comments;
		for (int j = 0; j < ptr.length; j++)
		{
			if (ptr[j] == null)
				break;
			System.err.println(new String(ptr[j], 0, ptr[j].length - 1));
		}
		int	nChannels = vi.channels;
		float	fSampleRate = vi.rate;
		if (TDebug.TraceAudioFileReader) { TDebug.out("\nBitstream is " + vi.channels + " channel, " + vi.rate + "Hz"); }
		if (TDebug.TraceAudioFileReader) { TDebug.out("Encoded by: " + new String(vc.vendor, 0, vc.vendor.length - 1) + "\n"); }

		/*
		  If the file size is known, we derive the number of frames
		  ('frame size') from it.
		  If the values don't fit into integers, we leave them at
		  NOT_SPECIFIED. 'Unknown' is considered less incorrect than
		  a wrong value.
		*/
		// [fb] not specifying it causes Sun's Wave file writer to write rubbish
		int	nByteSize = AudioSystem.NOT_SPECIFIED;
		int	nFrameSize = AudioSystem.NOT_SPECIFIED;
		if (lFileSizeInBytes != AudioSystem.NOT_SPECIFIED
		    && lFileSizeInBytes <= Integer.MAX_VALUE)
		{
			nByteSize = (int) lFileSizeInBytes;
			// TODO: check if we can calculate a useful value here
			// nFrameSize = (int) (lFileSizeInBytes / 33);
		}

		AudioFormat	format = new AudioFormat(
			Encodings.getEncoding("OGG_VORBIS"),
			fSampleRate,
			AudioSystem.NOT_SPECIFIED /*???*/,
			nChannels,
			AudioSystem.NOT_SPECIFIED,	// gsm: 33,
			AudioSystem.NOT_SPECIFIED,	// gsm: 50.0F,
			true);	// this value is chosen arbitrarily
		AudioFileFormat	audioFileFormat =
			new TAudioFileFormat(
				AudioFileTypes.getType("Vorbis","ogg"),
				format,
				nFrameSize,
				nByteSize);
		if (TDebug.TraceAudioFileReader) { TDebug.out("VorbisAudioFileReader.getAudioFileFormat(): end"); }
		return audioFileFormat;
	}
}



/*** VorbisAudioFileReader.java ***/

