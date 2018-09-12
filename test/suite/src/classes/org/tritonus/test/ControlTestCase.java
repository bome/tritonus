/*
 *	ControlTestCase.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.tritonus.test;

import junit.framework.TestCase;

import javax.sound.sampled.Control;
import javax.sound.sampled.BooleanControl;



/**	Tests for class javax.sound.sampled.Control.
 */
public class ControlTestCase
extends TestCase
{
	public ControlTestCase(String strName)
	{
		super(strName);
	}



	/**	Checks getType().
		The test checks if the object returned by
		getType() is the one passed to the constructor.
	*/
	public void testGetTypeObject()
		throws Exception
	{
		Control.Type	type = BooleanControl.Type.MUTE;
		Control		control = new TestControl(type);
		Control.Type	returnedType = control.getType();
		assertEquals("type object", type, returnedType);
	}



	/**	Checks getType().
		The test checks if the object returned by
		getType() is null, as is passed to the constructor.
	*/
	public void testGetTypeNull()
		throws Exception
	{
		Control.Type	type = null;
		Control		control = new TestControl(type);
		Control.Type	returnedType = control.getType();
		assertEquals("type object (null)", type, returnedType);
	}



	/**	Checks toString().
		The test checks if the string returned by toString()
		contains characters (and doesn't throw an exception).
	*/
	public void testToString()
		throws Exception
	{
		Control.Type	type = BooleanControl.Type.MUTE;
		Control		control = new TestControl(type);
		String		strReturnedString = control.toString();
		assertTrue("toString() result", strReturnedString.length() > 0);
	}



	/**	Inner class used to get around protected constructor.
	 */
	private class TestControl
	extends Control
	{
		public TestControl(Control.Type type)
		{
			super(type);
		}
	}
}



/*** ControlTestCase.java ***/
