/*
 *	StrokeTypeTestCase.java
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



public class StrokeTypeTestCase
extends BaseTestCase
{
	public StrokeTypeTestCase(String strName)
	{
		super(strName);
	}



	public void testCreateDelete()
		throws Exception
	{
		InstrumentRepository	instrumentRepository = getInstrumentRepository();
		List	oldList = instrumentRepository.getAllStrokeTypes();
		StrokeTypeDetails	strokeTypeDetails = new StrokeTypeDetails(ZERO_LONG, TEST_NAME, "", new byte[0]);
		Long	iid = instrumentRepository.createStrokeType(strokeTypeDetails);
		List	newList = instrumentRepository.getAllStrokeTypes();
		instrumentRepository.deleteStrokeType(iid);
		List	newestList = instrumentRepository.getAllStrokeTypes();
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
		InstrumentRepository	instrumentRepository = getInstrumentRepository();
		List	oldList = instrumentRepository.getAllStrokeTypes();
		StrokeTypeDetails	strokeTypeDetails = new StrokeTypeDetails(ZERO_LONG, TEST_NAME, "", new byte[0]);

		Long	iid = instrumentRepository.createStrokeType(strokeTypeDetails);
		List	newList = instrumentRepository.getAllStrokeTypes();
		instrumentRepository.deleteStrokeType(iid);
		List	newestList = instrumentRepository.getAllStrokeTypes();
		assertEquals(oldList.size(), newList.size() - 1);
		assertEquals(oldList.size(), newestList.size());
		assertTrue(!containsId(oldList, iid));
		assertTrue(containsId(newList, iid));
		assertTrue(!containsId(newestList, iid));
	}



	private boolean containsId(List detailsList, Long id)
	{
		Iterator	iterator = detailsList.iterator();
		while (iterator.hasNext())
		{
			StrokeTypeDetails	details = (StrokeTypeDetails) iterator.next();
			if (details.getId().equals(id))
			{
				return true;
			}
		}
		return false;
	}
}



/*** StrokeTypeTestCase.java ***/
