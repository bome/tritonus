/*
 *	CompoundControl.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2004 by Matthias Pfisterer
 *
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
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package javax.sound.sampled;

import java.util.Arrays;
import java.util.Collection;

import org.tritonus.share.TDebug;



public abstract class CompoundControl
extends Control
{
	private static final Control[]	EMPTY_CONTROL_ARRAY = new Control[0];

	private Collection<Control>	m_memberControls;



	protected CompoundControl(Type type,
				 Control[] aMemberControls)
	{
		super(type);
		if (TDebug.TraceControl) TDebug.out("CompoundControl.<init>: begin");
		m_memberControls = Arrays.asList(aMemberControls);
		if (TDebug.TraceControl) TDebug.out("CompoundControl.<init>: end");
	}



	public Control[] getMemberControls()
	{
		return m_memberControls.toArray(EMPTY_CONTROL_ARRAY);
	}



	public String toString()
	{
		// TODO:
		return super.toString() + " components: " + "";
	}





	public static class Type
	extends Control.Type
	{
		protected Type(String strName)
		{
			super(strName);
		}
	}
}



/*** CompoundControl.java ***/
