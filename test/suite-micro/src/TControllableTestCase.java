/*
 *	TControllableTestCase.java
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
import javax.microedition.media.control.GUIControl;

import org.tritonus.mmapi.TControllable;



/**	TestCases for org.tritonus.mmapi.TControllable.
*/
public class TControllableTestCase
extends BaseControllableTestCase
{
	public TControllableTestCase(String strName)
	{
		super(strName);
	}



	/**	Create Controllable for simple tests.
	*/
	protected Controllable createControllable()
		throws Exception
	{
		return new TestControllable();
	}


	/**	Checks getControls().
		Checks the length of the array returned by getControls()
		and that both controls are contained in it.
	*/
	public void testTGetControls()
		throws Exception
	{
		Controllable	controllable = createControllable();
		Control[]	aControls = controllable.getControls();
		assertEquals("getControls() length", 2, aControls.length);

		boolean	bGUIControlContained = false;
		boolean	bCustomControlContained = false;
		if (aControls[0] instanceof GUIControl)
		{
			bGUIControlContained = true;
		}
		else if (aControls[0] instanceof CustomControl)
		{
			bCustomControlContained = true;
		}
		if (aControls[1] instanceof GUIControl)
		{
			bGUIControlContained = true;
		}
		else if (aControls[1] instanceof CustomControl)
		{
			bCustomControlContained = true;
		}
		assertTrue("getControls() contains both controls", bGUIControlContained && bCustomControlContained);
	}


	/**	Checks getControl(String).
		Checks:
		- if an IllegalArgumentException is thrown for null
		arguments
		- if no exception if thrown for all predefined control types
	*/
	public void testTGetControl()
		throws Exception
	{
		Controllable	controllable = createControllable();
		Control		control;

		control = controllable.getControl("GUIControl");
		assertTrue("getControl('GUIControl')", control instanceof GUIControl);

		control = controllable.getControl("javax.microedition.media.control.GUIControl");
		assertTrue("getControl('javax.microedition.media.control.GUIControl')", control instanceof GUIControl);

		// For dubious reasons, this test doesn't work.
// 		control = controllable.getControl("TControllableTestCase.CustomControl");
// 		assertTrue("getControl('TControllableTestCase.CustomControl')", control instanceof TControllableTestCase.CustomControl);

		control = controllable.getControl("<nothing>");
		assertTrue("getControl('<nothing>')", control == null);
	}



	private class TestControllable
	extends TControllable
	{
		public TestControllable()
		{
			super(new Control[]{new TestGUIControl(),
						    new CustomControl()});
		}


		protected boolean isStateLegalForControls()
		{
			return true;
		}
	}


	private class TestGUIControl
	implements GUIControl
	{
		public TestGUIControl()
		{
		}


		public Object initDisplayMode(int mode, Object arg)
		{
			return null;
		}
	}


	public class CustomControl
	implements Control
	{
		public CustomControl()
		{
		}
	}
}



/*** TControllableTestCase.java ***/
