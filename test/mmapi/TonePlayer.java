/*
 *	TonePlayer.java
 *
 *	This file is part of the Java Sound Examples.
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */


import	javax.microedition.media.Manager;
import	javax.microedition.media.MediaException;


/*	+DocBookXML
	<title>Playing a note on the synthesizer</title>

	<formalpara><title>Purpose</title>
	<para>Plays a single note on the synthesizer.</para>
	</formalpara>

	<formalpara><title>Level</title>
	<para>newbie</para></formalpara>

	<formalpara><title>Usage</title>
	<para>
	<cmdsynopsis><command>java TonePlayer</command>
	<arg choice="plain"><replaceable class="parameter">keynumber</replaceable></arg>
	<arg choice="plain"><replaceable class="parameter">volume</replaceable></arg>
	<arg choice="plain"><replaceable class="parameter">duration</replaceable></arg>
	</cmdsynopsis>
	</para></formalpara>

	<formalpara><title>Parameters</title>
	<variablelist>
	<varlistentry>
	<term><replaceable class="parameter">keynumber</replaceable></term>
	<listitem><para>the MIDI key number</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><replaceable class="parameter">volume</replaceable></term>
	<listitem><para>the volume</para></listitem>
	</varlistentry>
	<varlistentry>
	<term><replaceable class="parameter">duration</replaceable></term>
	<listitem><para>the duration in milliseconds</para></listitem>
	</varlistentry>
	</variablelist>
	</formalpara>

	<formalpara><title>Bugs, limitations</title>
	<para>The precision of the duration depends on the precision
	of <function>Thread.sleep()</function>, which in turn depends on
	the precision of the system time and the latency of th
	thread scheduling of the Java VM. For many VMs, this
	means about 20 ms. When playing multiple notes, it is
	recommended to use a <classname>Sequence</classname> and the
	<classname>Sequencer</classname>, which is supposed to give better
	timing.</para>
	</formalpara>

	<formalpara><title>Source code</title>
	<para>
	<ulink url="TonePlayer.java.html">TonePlayer.java</ulink>
	</para>
	</formalpara>

-DocBookXML
*/
public class TonePlayer
{
	public static void main(String[] args)
	{
		int	nNoteNumber = 0;	// MIDI key number
		int	nVolume = 0;

		/*
		 *	Time between note on and note off event in
		 *	milliseconds. Note that on most systems, the
		 *	best resolution you can expect are 10 ms.
		 */
		int	nDuration = 0;
		if (args.length == 3)
		{
			nNoteNumber = Integer.parseInt(args[0]);
			nNoteNumber = Math.min(127, Math.max(0, nNoteNumber));
			nVolume = Integer.parseInt(args[1]);
			nVolume = Math.min(100, Math.max(0, nVolume));
			nDuration = Integer.parseInt(args[2]);
			nDuration = Math.max(0, nDuration);
		}
		else
		{
			printUsageAndExit();
		}

		/*
		 *	We need a synthesizer to play the note on.
		 *	Here, we simply request the default
		 *	synthesizer.
		 */
		try
		{
			Manager.playTone(nNoteNumber, nDuration, nVolume);
		}
		catch (MediaException e)
		{
			e.printStackTrace();
		}
	}



	private static void printUsageAndExit()
	{
		System.out.println("TonePlayer: usage:");
		System.out.println("java TonePlayer <note number> <volume> <duration>");
		System.out.println("<note number>: 0 .. 127, like MIDI");
		System.out.println("<volume>: 0 .. 100");
		System.out.println("<duration>: in milliseconds:");
		System.exit(1);
	}
}



/*** TonePlayer.java ***/
