/*
 *	InstrumentTestCase.java
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

import	java.util.Iterator;
import	java.util.List;

import	org.jsresources.apps.esemiso.ejb.interfaces.InstrumentRepository;
import	org.jsresources.apps.esemiso.ejb.util.InstrumentDetails;
import	org.jsresources.apps.esemiso.ejb.util.StrokeDetails;
import	org.jsresources.apps.esemiso.ejb.util.StrokeTypeDetails;
import	org.jsresources.apps.esemiso.ejb.util.SampleDetails;



public class InstrumentTestCase
extends BaseTestCase
{
	public InstrumentTestCase(String strName)
	{
		super(strName);
	}



	public void testCreateDelete()
		throws Exception
	{
		InstrumentRepository	instrumentRepository = getInstrumentRepository();
		List	oldList = instrumentRepository.getAllInstruments();
		InstrumentDetails	instrumentDetails = new InstrumentDetails(ZERO_LONG, TEST_NAME, "");
		Long	iid = instrumentRepository.createInstrument(instrumentDetails);
		List	newList = instrumentRepository.getAllInstruments();
		instrumentRepository.deleteInstrument(iid);
		List	newestList = instrumentRepository.getAllInstruments();
		assertEquals(oldList.size(), newList.size() - 1);
		assertEquals(oldList.size(), newestList.size());
		assertTrue(!containsId(oldList, iid));
		assertTrue(containsId(newList, iid));
		assertTrue(!containsId(newestList, iid));
	}



	// TODO:
	public void testSetGet()
		throws Exception
	{
		String	strNotes = "987654321";
		InstrumentRepository	instrumentRepository = getInstrumentRepository();
		InstrumentDetails	instrumentDetails = new InstrumentDetails(ZERO_LONG, TEST_NAME, strNotes);
		Long	iid = instrumentRepository.createInstrument(instrumentDetails);
		instrumentDetails = instrumentRepository.getInstrumentDetails(iid);
		assertEquals(TEST_NAME, instrumentDetails.getName());
		assertEquals(strNotes, instrumentDetails.getNotes());
		String	strName = "abcdefghijklmnopqrstuvwxyz";
		instrumentRepository.setInstrumentName(iid, strName);
		instrumentDetails = instrumentRepository.getInstrumentDetails(iid);
		instrumentRepository.deleteInstrument(iid);

		assertEquals(strName, instrumentDetails.getName());
	}



	private boolean containsId(List detailsList, Long id)
	{
		Iterator	iterator = detailsList.iterator();
		while (iterator.hasNext())
		{
			InstrumentDetails	details = (InstrumentDetails) iterator.next();
			if (details.getId().equals(id))
			{
				return true;
			}
		}
		return false;
	}
}



/*** InstrumentTestCase.java ***/
