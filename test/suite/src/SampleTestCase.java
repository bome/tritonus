/*
 *	SampleTestCase.java
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



public class SampleTestCase
extends BaseTestCase
{
	public SampleTestCase(String strName)
	{
		super(strName);
	}



	public void testCreateDelete()
		throws Exception
	{
		InstrumentRepository	instrumentRepository = getInstrumentRepository();
		List	oldList = instrumentRepository.getAllSamples();
		SampleDetails	sampleDetails = new SampleDetails(ZERO_LONG, 1, 2, 3, true, false);
		Long	iid = instrumentRepository.createSample(sampleDetails);
		List	newList = instrumentRepository.getAllSamples();
		instrumentRepository.deleteSample(iid);
		List	newestList = instrumentRepository.getAllSamples();
		assertEquals(oldList.size(), newList.size() - 1);
		assertEquals(oldList.size(), newestList.size());
		assertTrue(!containsId(oldList, iid));
		assertTrue(containsId(newList, iid));
		assertTrue(!containsId(newestList, iid));
	}



	public void testSetGet()
		throws Exception
	{
		InstrumentRepository	instrumentRepository = getInstrumentRepository();
		SampleDetails	sampleDetails = new SampleDetails(ZERO_LONG, 1, 2, 3, true, false);

		Long	iid = instrumentRepository.createSample(sampleDetails);
		sampleDetails = instrumentRepository.getSampleDetails(iid);
		assertEquals(1, sampleDetails.getSampleRate());
		assertEquals(2, sampleDetails.getChannels());
		assertEquals(3, sampleDetails.getBitsPerSample());
		assertEquals(true, sampleDetails.getSigned());
		assertEquals(false, sampleDetails.getBigEndian());
		byte[]	abData = new byte[]{0,3, 99, 4};
		instrumentRepository.setSampleData(iid, abData);
		byte[]	abNewData = instrumentRepository.getSampleData(iid);
		assertTrue(compareByteArray(abData, abNewData));
		instrumentRepository.deleteSample(iid);
	}



	private boolean containsId(List detailsList, Long id)
	{
		Iterator	iterator = detailsList.iterator();
		while (iterator.hasNext())
		{
			SampleDetails	details = (SampleDetails) iterator.next();
			if (details.getId().equals(id))
			{
				return true;
			}
		}
		return false;
	}


	// returns true if equal
	private boolean compareByteArray(byte[] abData1, byte[] abData2)
	{
		if (abData1.length != abData2.length)
		{
			return false;
		}
		for (int i = 0; i < abData1.length; i++)
		{
			if (abData1[i] != abData2[i])
			{
				return false;
			}
		}
		return true;
	}
}



/*** SampleTestCase.java ***/
