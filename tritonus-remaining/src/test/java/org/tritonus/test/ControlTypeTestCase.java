/*
 *	ControlTypeTestCase.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer
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

import javax.sound.sampled.Control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
// import javax.sound.sampled.BooleanControl;



/**	Tests for class javax.sound.sampled.Control.
 */
public class ControlTypeTestCase
{
	/**	Checks the constructor().
		The test checks if the constructor does not throw an
		exception.
	*/
    @Test
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
    @Test
	public void testEqualsSelfIdentity()
		throws Exception
	{
		String	strTypeName = "TeSt";
		Control.Type	type = new TestControlType(strTypeName);
		assertTrue(type.equals(type), "self-identity");
	}



	/**	Checks equals().
		The test checks if two objects are considered unequal,
		even if they have the same type string.
	*/
    @Test
	public void testEqualsSelfUnequality()
		throws Exception
	{
		String	strTypeName = "TeSt";
		Control.Type	type0 = new TestControlType(strTypeName);
		Control.Type	type1 = new TestControlType(strTypeName);
		assertTrue(! type0.equals(type1), "unequality");
	}



	/**	Checks hashCode().
		The test checks if two calls to hashCode() on the
		same object return the same value.
	*/
    @Test
	public void testHashCode()
		throws Exception
	{
		String	strTypeName = "TeSt";
		Control.Type	type = new TestControlType(strTypeName);
		assertTrue(type.hashCode() == type.hashCode(), "hash code");
	}



	/**	Checks toString().
		The test checks if the string returned by toString()
		equals the one passed in the constructor
		(and doesn't throw an exception).
	*/
    @Test
	public void testToString()
		throws Exception
	{
		String	strTypeName = "TeSt";
		Control.Type	type = new TestControlType(strTypeName);
		String		strReturnedTypeName = type.toString();
		assertEquals(strTypeName, strReturnedTypeName, "toString() result");
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
