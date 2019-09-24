/*
 *	GlobalSemanticsCheck.java
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



public class GlobalSemanticsCheck
extends IOGTCommonSemanticsCheck
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



	public GlobalSemanticsCheck(VariableTable globalVariableTable,
				    NodeSemanticsTable nodeSemanticsTable)
	{
		super(nodeSemanticsTable);
		m_globalVariableTable = globalVariableTable;
	}



////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////


	public void inAGlobaldeclGlobaldecl(AGlobaldeclGlobaldecl node)
	{
	}


	public void outAGlobaldeclGlobaldecl(AGlobaldeclGlobaldecl node)
	{
	}



    public void inARtparamGlobaldef(ARtparamGlobaldef node)
    {
    }

    public void outARtparamGlobaldef(ARtparamGlobaldef node)
    {
    }


    public void inARoutedefGlobaldef(ARoutedefGlobaldef node)
    {
    }

    public void outARoutedefGlobaldef(ARoutedefGlobaldef node)
    {
    }


    public void inASenddefGlobaldef(ASenddefGlobaldef node)
    {
    }

    public void outASenddefGlobaldef(ASenddefGlobaldef node)
    {
    }


    public void inASeqdefGlobaldef(ASeqdefGlobaldef node)
    {
    }

    public void outASeqdefGlobaldef(ASeqdefGlobaldef node)
    {
    }



////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////



	protected VariableTable getOwnVariableTable()
	{
		return m_globalVariableTable;
	}


	protected VariableTable getGlobalVariableTable()
	{
		return null;
	}



	protected int[] getLegalVariableTypes()
	{
		return LEGAL_VARIABLE_TYPES;
	}
}



/*** GlobalSemanticsCheck.java ***/
