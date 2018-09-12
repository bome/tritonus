/*
 *	ControlTypeTestCase.java
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
// import javax.sound.sampled.BooleanControl;



/**	Tests for class javax.sound.sampled.Control.
 */
public class ControlTypeTestCase
extends TestCase
{
	public ControlTypeTestCase(String strName)
	{
		super(strName);
	}



	/**	Checks the constructor().
		The test checks if the constructor does not throw an
		exception.
	*/
	public void testConstructor()
		throws Exception
	{
		String	strTypeName = "TeSt";
		@SuppressWarnings("unused") Control.Type	type =
			new TestControlType(strTypeName);
	}



	/**	Checks equals().
		The test checks if an object is considered equal to
		itself.
	*/
	public void testEqualsSelfIdentity()
		throws Exception
	{
		String	strTypeName = "TeSt";
		Control.Type	type = new TestControlType(strTypeName);
		assertTrue("self-identity", type.equals(type));
	}



	/**	Checks equals().
		The test checks if two objects are considered unequal,
		even if they have the same type string.
	*/
	public void testEqualsSelfUnequality()
		throws Exception
	{
		String	strTypeName = "TeSt";
		Control.Type	type0 = new TestControlType(strTypeName);
		Control.Type	type1 = new TestControlType(strTypeName);
		assertTrue("unequality", ! type0.equals(type1));
	}



	/**	Checks hashCode().
		The test checks if two calls to hashCode() on the
		same object return the same value.
	*/
	public void testHashCode()
		throws Exception
	{
		String	strTypeName = "TeSt";
		Control.Type	type = new TestControlType(strTypeName);
		assertTrue("hash code", type.hashCode() == type.hashCode());
	}



	/**	Checks toString().
		The test checks if the string returned by toString()
		equals the one passed in the constructor
		(and doesn't throw an exception).
	*/
	public void testToString()
		throws Exception
	{
		String	strTypeName = "TeSt";
		Control.Type	type = new TestControlType(strTypeName);
		String		strReturnedTypeName = type.toString();
		assertEquals("toString() result", strTypeName, strReturnedTypeName);
	}



	/**	Inner class used to get around protected constructor.
	 */
	private class TestControlType
	extends Control.Type
	{
		public TestControlType(String strName)
		{
			super(strName);
		}
	}
}



/*** ControlTypeTestCase.java ***/
