/*
 *	StandardMidiFileReader.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	org.tritonus.midi.file;


import	java.io.DataOutputStream;
import	java.io.FileOutputStream;
import	java.io.OutputStream;
import	java.io.IOException;
import	java.io.File;

import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.SysexMessage;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.Sequence;
import	javax.sound.midi.Track;
import	javax.sound.midi.spi.MidiFileWriter;

import	org.tritonus.TDebug;



public class StandardMidiFileWriter
	extends		MidiFileWriter
{
	public static boolean		USE_RUNNING_STATUS = true;
	public static boolean		CANCEL_RUNNING_STATUS_ON_META_AND_SYSEX = true;

	// IDEA: put into a file MidiConstants
	private static final int	HEADER_MAGIC = 0x4d546864;	// "MThd"
	private static final int	TRACK_MAGIC = 0x4d54726b;	// "MTrk"
	private static final int	STATUS_NONE = 0;
	private static final int	STATUS_ONE_BYTE = 1;
	private static final int	STATUS_TWO_BYTES = 2;
	private static final int	STATUS_SYSEX = 3;
	private static final int	STATUS_META = 4;



	public int[] getMidiFileTypes()
	{
		return new int[]{0, 1};
	}



	public int[] getMidiFileTypes(Sequence sequence)
	{
		Track[]	tracks = sequence.getTracks();
		if (tracks.length == 1)
		{
			return new int[]{0};
		}
		else
		{
			return new int[]{1};
		}
	}



	public int write(Sequence sequence,
			 int nFileType,
			 OutputStream outputStream)
		throws	IOException
	{
		if (! isFileTypeSupported(nFileType, sequence))
		{
			throw new IllegalArgumentException("file type is not supported for this sequence");
		}
		Track[]	aTracks = sequence.getTracks();
		DataOutputStream	dataOutputStream = new DataOutputStream(outputStream);
		dataOutputStream.writeInt(HEADER_MAGIC);
		dataOutputStream.writeInt(6);	// header length
		dataOutputStream.writeShort(nFileType);
		dataOutputStream.writeShort(aTracks.length);
		float	fDivisionType = sequence.getDivisionType();
		int	nResolution = sequence.getResolution();
		int	nDivision = 0;
		if (fDivisionType == Sequence.PPQ)
		{
			nDivision = nResolution & 0x7fff;
		}
		else
		{
			// TODO:
		}
		dataOutputStream.writeShort(nDivision);	// unsigned?
		int	nBytesWritten = 14;
		for (int nTrack = 0; nTrack < aTracks.length; nTrack++)
		{
			nBytesWritten += writeTrack(aTracks[nTrack],
						    dataOutputStream);
		}

		return nBytesWritten;
	}



	public int write(Sequence sequence,
			 int nFileType,
			 File file)
		throws	IOException
	{
		OutputStream	outputStream = new FileOutputStream(file);
		int	nBytes = write(sequence,
				       nFileType,
				       outputStream);
		outputStream.close();
		return nBytes;
	}



	private static int writeTrack(Track track,
				      DataOutputStream dataOutputStream)
		throws	IOException
	{
		int	nLength = 0;
		if (dataOutputStream != null)
		{
			dataOutputStream.writeInt(TRACK_MAGIC);
		}
		/*
		 *	This is a recursive call!
		 *	It is to find out the length of the track without
		 *	actually writing. Having the second parameter as
		 *	null tells writeTrack() and its subordinate
		 *	methods to not write out data bytes.
		 */
		int	nTrackLength = 0;
		if (dataOutputStream != null)
		{
			nTrackLength = writeTrack(track, null);
		}
		if (dataOutputStream != null)
		{
			dataOutputStream.writeInt(nTrackLength);
		}
		MidiEvent	previousEvent = null;
		int[]	anRunningStatusByte = new int[1];
		anRunningStatusByte[0] = -1;
		for (int nEvent = 0; nEvent < track.size(); nEvent++)
		{
			MidiEvent	event = track.get(nEvent);
			nLength += writeEvent(event,
					      previousEvent,
					      anRunningStatusByte,
					      dataOutputStream);
			previousEvent = event;
		}
		return nLength;
	}



	private static int writeEvent(MidiEvent event,
				      MidiEvent previousEvent,
				      int[] anRunningStatusByte,
				      DataOutputStream dataOutputStream)
		throws	IOException
	{
		int	nLength = 0;
		long	lTickDelta = 0;
		if (previousEvent != null)
		{
			lTickDelta = event.getTick() - previousEvent.getTick();
		}
		if (lTickDelta < 0)
		{
			TDebug.out("StandardMidiFileWriter.writeEvent(): warning: events not in order");
		}
		// add bytes according to coded length of delta
		nLength += writeVariableLengthQuantity(lTickDelta, dataOutputStream);
		MidiMessage	message = event.getMessage();
		// byte[]		abData = message.getMessage();
		int		nDataLength = message.getLength();
		if (message instanceof ShortMessage)
		{
			if (USE_RUNNING_STATUS && anRunningStatusByte[0] == message.getStatus())
			{
				/*
				 *	Write without status byte.
				 */
				if (dataOutputStream != null)
				{
					dataOutputStream.write(
						message.getMessage(),
						1, nDataLength - 1);
				}
				nLength += nDataLength - 1;
			}
			else
			{
				/*
				 *	Write with status byte.
				 */
				if (dataOutputStream != null)
				{
					dataOutputStream.write(
						message.getMessage(),
						0, nDataLength);
				}
				nLength += nDataLength;
				anRunningStatusByte[0] = message.getStatus();
			}
		}
		else if (message instanceof SysexMessage)
		{
			if (CANCEL_RUNNING_STATUS_ON_META_AND_SYSEX)
			{
				anRunningStatusByte[0] = -1;
			}
			SysexMessage	sysexMessage = (SysexMessage) message;
			if (dataOutputStream != null)
			{
				dataOutputStream.write(sysexMessage.getStatus());
			}
			nLength++;
			nLength += writeVariableLengthQuantity(
				nDataLength - 1,
				dataOutputStream);
			if (dataOutputStream != null)
			{
				dataOutputStream.write(
					sysexMessage.getData(),
					0, nDataLength - 1);
			}
			nLength += nDataLength - 1;
		}
		else if (message instanceof MetaMessage)
		{
			if (CANCEL_RUNNING_STATUS_ON_META_AND_SYSEX)
			{
				anRunningStatusByte[0] = -1;
			}
			MetaMessage	metaMessage = (MetaMessage) message;
			if (dataOutputStream != null)
			{
				dataOutputStream.write(metaMessage.getStatus());
			}
			nLength++;
			if (dataOutputStream != null)
			{
				dataOutputStream.write(metaMessage.getType());
			}
			nLength++;
			nLength += writeVariableLengthQuantity(
				nDataLength,
				dataOutputStream);
			if (dataOutputStream != null)
			{
				dataOutputStream.write(metaMessage.getData(), 0, nDataLength);
			}
			nLength += nDataLength - 1;
		}
		else
		{
			// TODO: output warning
		}
		return nLength;
	}



	/**
	 *	outputStream == 0 signals to only calculate the number of
	 *	needed to represent the value.
	 */
	private static int writeVariableLengthQuantity(long lValue, OutputStream outputStream)
		throws	IOException
	{
		int	nLength = 0;
		// IDEA: use a loop
		boolean	bWritingStarted = false;
		int	nByte = (int) ((lValue >> 21) & 0x7f);
		if (nByte != 0)
		{
			if (outputStream != null)
			{
				outputStream.write(nByte | 0x80);
			}
			nLength++;
			bWritingStarted = true;
		}
		nByte = (int) ((lValue >> 14) & 0x7f);
		if (nByte != 0 || bWritingStarted)
		{
			if (outputStream != null)
			{
				outputStream.write(nByte | 0x80);
			}
			nLength++;
			bWritingStarted = true;
		}
		nByte = (int) ((lValue >> 7) & 0x7f);
		if (nByte != 0 || bWritingStarted)
		{
			if (outputStream != null)
			{
				outputStream.write(nByte | 0x80);
			}
			nLength++;
		}
		nByte = (int) (lValue & 0x7f);
		if (outputStream != null)
		{
			outputStream.write(nByte);
		}
		nLength++;
		return nLength;
	}
}



/*** StandardMidiFileWriter.java ***/

