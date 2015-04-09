/*
 *	BooleanControl.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer
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

import org.tritonus.share.TDebug;



public abstract class BooleanControl
extends Control
{
	private static final String	DEFAULT_TRUE_LABEL = "true";
	private static final String	DEFAULT_FALSE_LABEL = "false";



	private boolean		m_bValue;
	private String		m_strTrueLabel;
	private String		m_strFalseLabel;



	protected BooleanControl(Type type,
				 boolean bInitialValue,
				 String strTrueLabel,
				 String strFalseLabel)
	{
		super(type);

		if (TDebug.TraceControl)
		{
			TDebug.out("BooleanControl.<init>: begin");
		}
		/* $$mp 2001-02-06: TODO: what's the matter with this?
		 */
		//$$fb 2000-12-02: incompatible with Sun implementation...
		//setValue(bInitialValue);
		m_bValue = bInitialValue;

		m_strTrueLabel = strTrueLabel;
		m_strFalseLabel = strFalseLabel;
		if (TDebug.TraceControl)
		{
			TDebug.out("BooleanControl.<init>: end");
		}
	}



	protected BooleanControl(Type type,
				 boolean bInitialValue)
	{
		this(type,
		     bInitialValue,
		     DEFAULT_TRUE_LABEL,
		     DEFAULT_FALSE_LABEL);
	}



	public void setValue(boolean bValue)
	{
		m_bValue = bValue;
	}



	public boolean getValue()
	{
		return m_bValue;
	}



	public String getStateLabel(boolean bState)
	{
		if (bState)
		{
			return m_strTrueLabel;
		}
		else
		{
			return m_strFalseLabel;
		}
	}



	public String toString()
	{
		return super.toString() + " state = " + getStateLabel(getValue());
	}





	public static class Type
	extends Control.Type
	{
		public static final Type	MUTE = new Type("MUTE");
		public static final Type	APPLY_REVERB = new Type("APPLY_REVERB");



		protected Type(String strName)
		{
			super(strName);
		}
	}
}



/*** BooleanControl.java ***/

