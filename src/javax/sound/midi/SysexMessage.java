/*
 *	SysexMessage.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	javax.sound.midi;


/**	Holds the data of a MIDI System Exclusive event.
 *	Sysex event are tricking me over and over again. I give my
 *	understanding of the expected behaviour for
 *	review:
 *
 *	
      <table bgcolor="aqua">
	<tr>
	  <td>

	    <p align="center">
	      <strong></strong>
	    </p>
	  </td>

	  <td>
	    <p align="center">
	      <strong>Sysex (F0)</strong>
	    </p>
	  </td>

	  <td>
	    <p align="center">
	      <strong>special sysex (F7)</strong>
	    </p>
	  </td>


	</tr>
	<tr valign="top">
	  <td valign="center">
	    <p align="center">
	      0.2
	    </p>
	  </td>

	  <td>
	    <ul>
	      <li>Java Sound 1.0 API</li>
	      <li>reading and writing of .au, .aiff and .wav files</li>
	      <li>basic playback and recording</li>
	      <li>Alaw and mu-law converter</li>
	      <li>Java MP3 decoder</li>
	    </ul>
	  </td>

	  <td>
	    <ul>
	      <li>Java Sound 1.0 API</li>
	      <li>reading of MIDI files</li>
	      <li>basic Sequencer</li>
	      <li>basic hardware Synthesizer support</li>
	      <li>external MIDI I/O</li>
	    </ul>

	  </td>

	</tr>
	<tr valign="top">

	  <td valign="center">
	    <p align="center">
	      0.3
	    </p>
	  </td>

	  <td>
	    <ul>
	      <li>linear converter</li>
	      <li>Java EsounD support</li>
	      <li>Mixer details</li>
	      <li>Line details</li>
	      <li>full Clip support</li>
	      <li>Volume and balance/pan controls</li>
	    </ul>

	  </td>

	  <td>
	    <ul>
	      <li>writing of MIDI files</li>
	      <li>handling of Sysex messages</li>
	      <li>improved Sequencer</li>
	      <li>Sequencer and MIDI I/O on Mac</li>
	    </ul>

	  </td>


	</tr>
	<tr valign="top">

	  <td valign="center">
	    <p align="center">
	      0.4
	    </p>
	  </td>

	  <td>
	    <ul>
	      <li>ALSA support (allows hardware mixing)</li>
	      <li>24 bit/96 kHz throughout the implementation</li>
	      <li>smart selection of converters</li>
	      <!--li>native MP3 decoder</li-->
	      <li>sample rate converter</li>
	      <li>applet support</li>
	    </ul>

	  </td>

	  <td>
	    <ul>
	      <li>full Sequencer</li>
	      <li>improved hardware Synthesizer support</li>
	      <li>basic software Synthesizer support</li>
	    </ul>

	  </td>

	</tr>
	<tr valign="top">

	  <td valign="center">
	    <p align="center">
	      0.5
	    </p>
	  </td>

	  <td>
	    <ul>
	      <li>Ports</li>
	      <li>digital reading from CD</li>
	      <li>MP3 encoder</li>
	      <li>GSM codec</li>
	    </ul>

	  </td>

	  <td>
	    <ul>
	      <li>full hardware Synthesizer support</li>
	      <li>improved software Synthesizer support</li>
	      <li>Soundbank handling</li>
	    </ul>

	  </td>


	</tr>
	<tr valign="top">

	  <td valign="center">
	    <p align="center">
	      later
	    </p>
	  </td>

	  <td>
	    <ul>
	      <li>AAC decoder and encoder</li>
	      <li>other codecs</li>
	    </ul>

	  </td>

	  <td>
	    <ul>
	      <li>MPEG-4 structured audio</li>
	      <li>full software Synthesizer support</li>
	    </ul>

	  </td>


	</tr>
      </table>

 */
public class SysexMessage
    extends	MidiMessage
{
	public static final int		SYSTEM_EXCLUSIVE = 0xF0;
	public static final int		SPECIAL_SYSTEM_EXCLUSIVE = 0xF7;


	public SysexMessage()
	{
		super(null);
	}


	protected SysexMessage(byte[] abData)
	{
		super(abData);
	}



	public void setMessage(byte[] abData, int nLength)
		throws	InvalidMidiDataException
	{
		super.setMessage(abData, nLength);
	}



	public void setMessage(int nStatus, byte[] abData, int nLength)
		throws	InvalidMidiDataException
	{
		byte[]	abCompleteData = new byte[nLength + 1];
		abCompleteData[0] = (byte) nStatus;
		System.arraycopy(abData, 0, abCompleteData, 1, nLength);
		setMessage(abCompleteData, abCompleteData.length);
	}



	public byte[] getData()
	{
		byte[] abData = new byte[getLength() - 1];
		System.arraycopy(getMessage(), 1, abData, 0, abData.length);
		return abData;
	}



	public Object clone()
	{
		byte[]	abData = new byte[getLength()];
		System.arraycopy(getMessage(), 0, abData, 0, abData.length);
		SysexMessage	message = new SysexMessage(abData);
		return message;
	}



}



/*** SysexMessage.java ***/
