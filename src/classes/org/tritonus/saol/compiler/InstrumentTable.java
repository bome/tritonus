/*
 *	InstrumentTable.java
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

package	org.tritonus.saol.compiler;


import	java.util.HashMap;
import	java.util.Map;



/**	The instrument table.
 */
public class InstrumentTable
{
	/**	Map that holds the instrument entries.
		Key: the name of the instrument.
		Value: a InstrumentEntry instance.
	*/
	private Map		m_instrumentMap;


	public InstrumentTable()
	{
		m_instrumentMap = new HashMap();
	}


	public void addEntry(InstrumentEntry instrumentEntry)
	{
		m_instrumentMap.put(instrumentEntry.getInstrumentName(), instrumentEntry);
	}


	public InstrumentEntry getInstrument(String strInstrumentName)
	{
		return (InstrumentEntry) m_instrumentMap.get(strInstrumentName);
	}
}



/*** InstrumentTable.java ***/
