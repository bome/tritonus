/*
 *	InstrumentEntry.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.saol.compiler;

import org.tritonus.saol.sablecc.node.AInstrdeclInstrdecl;



public class InstrumentEntry
{
	private String			m_strInstrumentName;
	private AInstrdeclInstrdecl	m_startNode;
	VariableTable			m_localVariableTable;



	public InstrumentEntry(String strInstrumentName,
			       AInstrdeclInstrdecl startNode)
	{
		m_strInstrumentName = strInstrumentName;
		m_startNode = startNode;
		m_localVariableTable = new VariableTable();
	}



	public String getInstrumentName()
	{
		return m_strInstrumentName;
	}



	public AInstrdeclInstrdecl getStartNode()
	{
		return m_startNode;
	}


	public VariableTable getLocalVariableTable()
	{
		return m_localVariableTable;
	}
}



/*** InstrumentEntry.java ***/