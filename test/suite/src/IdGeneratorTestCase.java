/*
 *	IdGeneratorTestCase.java
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

import	org.jsresources.apps.esemiso.ejb.interfaces.InstrumentRepository;
import	org.jsresources.apps.esemiso.ejb.util.InstrumentDetails;
import	org.jsresources.apps.esemiso.ejb.util.StrokeDetails;
import	org.jsresources.apps.esemiso.ejb.util.StrokeTypeDetails;
import	org.jsresources.apps.esemiso.ejb.util.SampleDetails;



public class IdGeneratorTestCase
extends BaseTestCase
{
	public IdGeneratorTestCase(String strName)
	{
		super(strName);
	}



	public void testSequence()
		throws Exception
	{
		InstrumentRepository	instrumentRepository = getInstrumentRepository();
		InstrumentDetails	instrumentDetails = new InstrumentDetails(ZERO_LONG, TEST_NAME, "");
		Long	iid1 = instrumentRepository.createInstrument(instrumentDetails);
		Long	iid2 = instrumentRepository.createInstrument(instrumentDetails);
		StrokeTypeDetails	strokeTypeDetails = new StrokeTypeDetails(ZERO_LONG, TEST_NAME, "", new byte[0]);
		Long	stid1 = instrumentRepository.createStrokeType(strokeTypeDetails);
		Long	stid2 = instrumentRepository.createStrokeType(strokeTypeDetails);
		SampleDetails	sampleDetails = new SampleDetails(ZERO_LONG, 1, 2, 3, true, false);
		Long	sid1 = instrumentRepository.createSample(sampleDetails);
		Long	sid2 = instrumentRepository.createSample(sampleDetails);
		instrumentRepository.deleteInstrument(iid1);
		instrumentRepository.deleteInstrument(iid2);
		instrumentRepository.deleteStrokeType(stid1);
		instrumentRepository.deleteStrokeType(stid2);
		instrumentRepository.deleteSample(sid1);
		instrumentRepository.deleteSample(sid2);
		assertTrue(iid1.longValue() < iid2.longValue());
		assertTrue(iid2.longValue() < stid1.longValue());
		assertTrue(stid1.longValue() < stid2.longValue());
		assertTrue(stid2.longValue() < sid1.longValue());
		assertTrue(sid1.longValue() < sid2.longValue());
	}



// 	private void checkAudioFileFormat(AudioFileFormat audioFileFormat, boolean bRealLengthExpected)
// 		throws Exception
// 	{
// 		assertEquals("type",
// 			     getType(),
// 			     audioFileFormat.getType());
// 		checkAudioFormat(audioFileFormat.getFormat());
// 		long	lExpectedByteLength = AudioSystem.NOT_SPECIFIED;
// 		long	lExpectedFrameLength = AudioSystem.NOT_SPECIFIED;
// 		if (getCheckRealLengths() || bRealLengthExpected)
// 		{
// 			lExpectedByteLength = getByteLength();
// 			lExpectedFrameLength = getFrameLength();
// 		}
// 		assertEquals("byte length",
// 			     lExpectedByteLength,
// 			     audioFileFormat.getByteLength());
// 		assertEquals("frame length",
// 			     lExpectedFrameLength,
// 			     audioFileFormat.getFrameLength());
// 	}
}



/*** IdGeneratorTestCase.java ***/
