/*
 *	AlsaCtlTestCase.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer
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

package org.tritonus.test.alsa;

import junit.framework.TestCase;

import org.tritonus.lowlevel.alsa.AlsaCtl;



public class AlsaCtlTestCase
extends TestCase
{
	private static final boolean	DEBUG = false;
	private static final String	CARD_NAME_FOR_INDEX_TEST = "LIFE";



	public AlsaCtlTestCase(String strName)
	{
		super(strName);
	}



	public void testGetCards()
	{
		int[]	anCards = AlsaCtl.getCards();
		assertTrue(anCards != null);
		assertTrue(anCards.length == 1);
		assertTrue(anCards[0] >= 0);
	}



	public void testLoadCards()
	{
		int[]	anCards = AlsaCtl.getCards();
		for (int i = 0; i < anCards.length; i++)
		{
			int	nError = AlsaCtl.loadCard(anCards[i]);
			assertTrue(nError >= 0);
		}
	}



	public void testGetIndex()
	{
		int	nIndex = AlsaCtl.getCardIndex(CARD_NAME_FOR_INDEX_TEST);
		if (DEBUG)
		{
			System.out.println("card index: " + nIndex);
		}
		assertTrue(nIndex >= 0);
		int[]	anCards = AlsaCtl.getCards();
		if (DEBUG)
		{
			System.out.println("card index: " + anCards[0]);
		}
		assertTrue(nIndex == anCards[0]);
	}



	public void testGetNames()
	{
		int[]	anCards = AlsaCtl.getCards();
		String	strName = AlsaCtl.getCardName(anCards[0]);
		assertTrue(strName != null && !strName.equals(""));
		String	strLongName = AlsaCtl.getCardLongName(anCards[0]);
		assertTrue(strLongName != null && !strLongName.equals(""));
		assertTrue(!strName.equals(strLongName));
		if (DEBUG)
		{
			System.out.println("card name: " + strName);
			System.out.println("card long name: " + strLongName);
		}
	}
}



/*** AlsaCtlTestCase.java ***/
