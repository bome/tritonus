/*
 *	NodeSemantics.java
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



public class NodeSemantics
extends WidthAndRate
{
	/**	Auxiliary information.
	 */
	private Object		m_aux;



	public NodeSemantics(int nWidth, int nRate)
	{
		this(nWidth, nRate, null);
	}



	public NodeSemantics(Object aux)
	{
		this(WidthAndRate.WIDTH_UNKNOWN, WidthAndRate.RATE_UNKNOWN, aux);
	}



	public NodeSemantics(int nWidth, int nRate, Object aux)
	{
		super(nWidth, nRate);
		m_aux = aux;
	}



	public Object getAux()
	{
		return m_aux;
	}
}



/*** NodeSemantics.java ***/
