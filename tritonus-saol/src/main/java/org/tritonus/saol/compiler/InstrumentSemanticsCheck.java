/*
 *	InstrumentSemanticsCheck.java
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tritonus.saol.sablecc.analysis.*;
import org.tritonus.saol.sablecc.node.*;



public class InstrumentSemanticsCheck
extends IOTCommonSemanticsCheck
{
	private static final boolean	DEBUG = true;
	private static final int[]	LEGAL_VARIABLE_TYPES = new int[]
	{
		WidthAndRate.RATE_I,
		WidthAndRate.RATE_K,
		WidthAndRate.RATE_A,
		WidthAndRate.RATE_OPARRAY,
	};

	private VariableTable		m_globalVariableTable;
	private VariableTable		m_localVariableTable;



	public InstrumentSemanticsCheck(VariableTable globalVariableTable,
					VariableTable localVariableTable,
					NodeSemanticsTable nodeSemanticsTable)
	{
		super(nodeSemanticsTable);
		m_globalVariableTable = globalVariableTable;
		m_localVariableTable = localVariableTable;
	}


////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////


	public void inAInstrdeclInstrdecl(AInstrdeclInstrdecl node)
	{
// 		String	strInstrumentName = node.getIdentifier().getText();
// 		m_strClassName = PACKAGE_PREFIX + strInstrumentName;
// 		m_classGen = new ClassGen(m_strClassName,
// 					  SUPERCLASS_NAME,
// 					  "<generated>",
// 					  Constants.ACC_PUBLIC | Constants.ACC_SUPER,
// 					  null);
// 		m_constantPoolGen = m_classGen.getConstantPool();
// 		m_instructionFactory = new InstructionFactory(m_constantPoolGen);
// 		m_aMethods[METHOD_CONSTR] = new InstrumentMethod(m_classGen, "<init>");
// 		m_aMethods[METHOD_I] = new InstrumentMethod(m_classGen, "doIPass");
// 		m_aMethods[METHOD_K] = new InstrumentMethod(m_classGen, "doKPass");
// 		m_aMethods[METHOD_A] = new InstrumentMethod(m_classGen, "doAPass");
// 		m_aMethods[METHOD_CONSTR].appendInstruction(InstructionConstants.ALOAD_0);
// 		Instruction	invokeSuperInstruction = m_instructionFactory.createInvoke(SUPERCLASS_NAME, "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL);
// //		Instruction	invokeSuperInstruction = m_instructionFactory.createInvoke(SUPERCLASS_NAME, SUPERCLASS_CONSTRUCTOR_NAME, Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL);
// 		m_aMethods[METHOD_CONSTR].appendInstruction(invokeSuperInstruction);
	}



	public void outAInstrdeclInstrdecl(AInstrdeclInstrdecl node)
	{
// 		for (int i = 0; i < m_aMethods.length; i++)
// 		{
// 			m_aMethods[i].finish();
// 		}
// 		JavaClass	javaClass = m_classGen.getJavaClass();
// 		try
// 		{
// 			ByteArrayOutputStream	baos = new ByteArrayOutputStream();
// 			javaClass.dump(baos);
// 			byte[]	abData = baos.toByteArray();
// 			Class	instrumentClass = m_classLoader.findClass(m_strClassName, abData);
// 			m_instrumentMap.put(m_strClassName, instrumentClass);
// 			if (DEBUG)
// 			{
// 				javaClass.dump(m_strClassName + CLASSFILENAME_SUFFIX);
// 			}
// 		}
// 		catch (IOException e)
// 		{
// 			e.printStackTrace();
// 		}
	}


	public void inAMiditagMiditag(AMiditagMiditag node)
	{
	}

	public void outAMiditagMiditag(AMiditagMiditag node)
	{
	}


	public void inAIntListIntList(AIntListIntList node)
	{
	}

	public void outAIntListIntList(AIntListIntList node)
	{
	}






////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////



	protected VariableTable getOwnVariableTable()
	{
		return m_localVariableTable;
	}


	protected VariableTable getGlobalVariableTable()
	{
		return m_globalVariableTable;
	}


	protected int[] getLegalVariableTypes()
	{
		return LEGAL_VARIABLE_TYPES;
	}
}



/*** InstrumentSemanticsCheck.java ***/
