/*
 *	GlobalsSearcher.java
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

import org.tritonus.saol.sablecc.analysis.*;
import org.tritonus.saol.sablecc.node.*;



public class GlobalsSearcher
extends DepthFirstAdapter
{
	private SAOLGlobals	m_saolGlobals;


	public GlobalsSearcher(SAOLGlobals saolGlobals)
	{
		m_saolGlobals = saolGlobals;
	}



	public SAOLGlobals getSAOLGlobals()
	{
		return m_saolGlobals;
	}


	public void outASrateRtparam(ASrateRtparam node)
	{
		TInteger	integer = node.getInteger();
		String		strInt = integer.getText();
		int		nARate = Integer.parseInt(strInt);
		getSAOLGlobals().setARate(nARate);
	}


// 	public void caseTNumber(TNumber node)
// 	{// When we see a number, we print it.
// 		System.out.print(node);
// 	}

// 	public void outAPlusExpr(APlusExpr node)
// 	{// out of alternative {plus} in Expr, we print the plus.
// 		System.out.print(node.getPlus());
// 	}

// 	public void outAMinusExpr(AMinusExpr node)
// 	{// out of alternative {minus} in Expr, we print the minus.
// 		System.out.print(node.getMinus());
// 	}

// 	public void outAMultFactor(AMultFactor node)
// 	{// out of alternative {mult} in Factor, we print the mult.
// 		System.out.print(node.getMult());
// 	}

// 	public void outADivFactor(ADivFactor node)
// 	{// out of alternative {div} in Factor, we print the div.
// 		System.out.print(node.getDiv());
// 	}

// 	public void outAModFactor(AModFactor node)
// 	{// out of alternative {mod} in Factor, we print the mod.
// 		System.out.print(node.getMod());
// 	}
}



/*** GlobalsSearcher.java ***/
