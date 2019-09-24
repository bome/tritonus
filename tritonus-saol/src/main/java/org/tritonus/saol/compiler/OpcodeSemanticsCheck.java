/*
 *	OpcodeSemanticsCheck.java
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



public class OpcodeSemanticsCheck
extends IOTCommonSemanticsCheck
{
	private static final boolean	DEBUG = true;
	private static final int[]	LEGAL_VARIABLE_TYPES = new int[]
	{
		WidthAndRate.RATE_I,
		WidthAndRate.RATE_K,
		WidthAndRate.RATE_A,
		WidthAndRate.RATE_X,
		WidthAndRate.RATE_OPARRAY,
	};

	private VariableTable		m_globalVariableTable;
	private VariableTable		m_localVariableTable;



	public OpcodeSemanticsCheck(VariableTable globalVariableTable,
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



    public void inAOpcodedeclOpcodedecl(AOpcodedeclOpcodedecl node)
    {
    }

    public void outAOpcodedeclOpcodedecl(AOpcodedeclOpcodedecl node)
    {
    }


    public void inAAopcodeOptype(AAopcodeOptype node)
    {
    }


    public void outAAopcodeOptype(AAopcodeOptype node)
    {
    }

    public void inAKopcodeOptype(AKopcodeOptype node)
    {
    }


    public void outAKopcodeOptype(AKopcodeOptype node)
    {
    }


    public void inAIopcodeOptype(AIopcodeOptype node)
    {
    }


    public void outAIopcodeOptype(AIopcodeOptype node)
    {
    }


    public void inAOpcodeOptype(AOpcodeOptype node)
    {
    }


    public void outAOpcodeOptype(AOpcodeOptype node)
    {
    }



	public void inAParamlistParamlist(AParamlistParamlist node)
	{
	}


	public void outAParamlistParamlist(AParamlistParamlist node)
	{
	}


	public void inAParamlistTailParamlistTail(AParamlistTailParamlistTail node)
	{
	}


	public void outAParamlistTailParamlistTail(AParamlistTailParamlistTail node)
	{
	}



	public void inAParamdeclParamdecl(AParamdeclParamdecl node)
	{
	}


	public void outAParamdeclParamdecl(AParamdeclParamdecl node)
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



/*** OpcodeSemanticsCheck.java ***/
