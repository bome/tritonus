/*
 *	OpcodeEntry.java
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

package org.tritonus.saol.engine.opcodes;


/**	Representation of one opcode implementation.
	This class is used for entries in the opcode table.
 */
public class OpcodeEntry
{
	private String		m_strOpcodeName;
	private OpcodeClass	m_opcodeClass;
	private String		m_strMethodName;
	private int		m_nRate;
	// TODO: parameter description, including dummy params



	// if opcode and method name are the same
	public OpcodeEntry(String strOpcodeName,
			   OpcodeClass opcodeClass,
			   int nRate)
	{
		this(strOpcodeName,
		     opcodeClass,
		     strOpcodeName,
		     nRate);
	}


	public OpcodeEntry(String strOpcodeName,
			   OpcodeClass opcodeClass,
			   String strMethodName,
			   int nRate)
	{
		m_strOpcodeName = strOpcodeName;
		m_opcodeClass = opcodeClass;
		m_strMethodName = strMethodName;
		m_nRate = nRate;
	}


	public String getOpcodeName()
	{
		return m_strOpcodeName;
	}


	public OpcodeClass getOpcodeClass()
	{
		return m_opcodeClass;
	}


	public String getMethodName()
	{
		return m_strMethodName;
	}


	public int getRate()
	{
		return m_nRate;
	}
}



/*** OpcodeEntry.java ***/
