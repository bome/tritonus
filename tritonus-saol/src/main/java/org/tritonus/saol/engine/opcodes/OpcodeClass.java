/*
 *	OpcodeClass.java
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


/**	The Math Opcodes (Section 5.9.4).
 */
public class OpcodeClass
{
	/**	Opcode class type: no instance needed.
		This means the opcode is implemented as a static method
		of the class. The Runtime system doesn't need to
		create an instance of the opcode class.
	 */
	public static final int		TYPE_STATIC = 0;

	/**	Opcode class type: one instance per renderer instance.
	 */
	public static final int		TYPE_RUNTIME_INSTANCE = 1;

	/**	Opcode class type: one instance per opcode usage.
	 */
	public static final int		TYPE_OPCODE_INSTANCE = 2;


	/**	The name of the opcode class.
		A fully qualified class name (package.class).
	 */
	private String		m_strName;

	/**	The type of the opcode class.
		One of TYPE_STATIC, TYPE_RUNTIME_INSTANCE
		and TYPE_OPCODE_INSTANCE.
	 */
	private int		m_nType;



	/**	Constructor.
		@param strName the name of the opcode class.
		A fully qualified class name is expected
		(package.class).

		@param nType the instantiation type of the class.
		One of TYPE_STATIC, TYPE_RUNTIME_INSTANCE
		and TYPE_OPCODE_INSTANCE.
	 */
	public OpcodeClass(String strName, int nType)
	{
		m_strName = strName;
		m_nType = nType;

	}



	/**	Retrieves the name of the opcode class.
		@return the name that was set with the constructor.
		A fully qualified class name
		(package.class)
	*/
	public String getName()
	{
		return m_strName;
	}


	/**	Retrieves the type of the opcode class.
		@return the type that was set with the constructor.
		One of TYPE_STATIC, TYPE_RUNTIME_INSTANCE
		and TYPE_OPCODE_INSTANCE.
	*/
	public int getType()
	{
		return m_nType;
	}
}



/*** OpcodeClass.java ***/
