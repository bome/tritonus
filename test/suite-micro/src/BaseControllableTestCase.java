/*
 *	BaseControllableTestCase.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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

import junit.framework.TestCase;

import javax.microedition.media.Control;
import javax.microedition.media.Controllable;



/**	Base TestCases for javax.microedition.media.Controllable.
*/
public abstract class BaseControllableTestCase
extends TestCase
{
	private static final String[]	CONTROL_TYPES =
	{
		"FramePositioningControl",
		"GUIControl",
		"MetaDataControl",
		"MIDIControl",
		"PitchControl",
		"RateControl",
		"RecordControl",
		"StopTimeControl",
		"TempoControl",
		"ToneControl",
		"VideoControl",
		"VolumeControl",
	};

	public BaseControllableTestCase(String strName)
	{
		super(strName);
	}


	/**	Calls both Controllable methods to see if an exception occurs.
		This method is intended for use by subclasses that need to
		test if IllegalStateExceptions are thrown in certain
		states.
	 */
	protected void callControllableMethods(Controllable controllable,
					     String strState)
		throws Exception
	{
		boolean	bExceptionThrown;
		String	strControlType = "GUIControl";

		bExceptionThrown = false;
		try
		{
			controllable.getControls();
		}
		catch (IllegalStateException e)
		{
			bExceptionThrown = true;
		}
		if (! bExceptionThrown)
		{
			fail("IllegalStateException on getControls() in " + strState + " state");
		}

		bExceptionThrown = false;
		try
		{
			controllable.getControl(strControlType);
		}
		catch (IllegalStateException e)
		{
			bExceptionThrown = true;
		}
		if (! bExceptionThrown)
		{
			fail("IllegalStateException on getControl(String) in " + strState + " state");
		}
	}



	/**	Create Controllable for simple tests.
		Subclasses must implement this method in a way
		that a Controllable is returned that can be fully used.
	*/
	protected abstract Controllable createControllable()
		throws Exception;


	/**	Basic getControls() test.
		Checks is the array returned by getControls()
		is not equal to null.
	*/
	public void testGetControls()
		throws Exception
	{
		Controllable	controllable = createControllable();
		Control[]	aControls = controllable.getControls();
		assertTrue("getControls() result", aControls != null);
	}


	/**	Basic getControl(String) test.
		Checks:
		- if an IllegalArgumentException is thrown for null
		arguments
		- if no exception if thrown for all predefined control types
	*/
	public void testGetControl()
		throws Exception
	{
		Controllable	controllable = createControllable();
		boolean	bExceptionThrown = false;
		try
		{
			controllable.getControl(null);
		}
		catch (IllegalArgumentException e)
		{
			bExceptionThrown = true;
		}
		if (! bExceptionThrown)
		{
			fail("IllegalArgumentException on getControl(null)");
		}

		// Here, we are only expecting that no exception is thrown.
		for (int i = 0; i < CONTROL_TYPES.length; i++)
		{
			controllable.getControl(CONTROL_TYPES[i]);
		}
	}
}



/*** BaseControllableTestCase.java ***/
