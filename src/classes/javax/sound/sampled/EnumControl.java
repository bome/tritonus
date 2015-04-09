/*
 *	EnumControl.java
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

import java.util.Collection;
import java.util.Arrays;

import org.tritonus.share.TDebug;



public abstract class EnumControl
extends Control
{
	private static final Object[]	EMPTY_OBJECT_ARRAY = new Object[0];



	private Object		m_value;
	private Collection<Object>	m_values;



	protected EnumControl(Type type,
			      Object[]	aValues,
			      Object value)
	{
		super(type);
		if (TDebug.TraceControl)
		{
			TDebug.out("EnumControl.<init>: begin");
		}
		m_values = Arrays.asList(aValues);
		setValue(value);
		if (TDebug.TraceControl)
		{
			TDebug.out("EnumControl.<init>: end");
		}
	}



	public void setValue(Object value)
	{
		if (m_values.contains(value))
		{
			m_value = value;
		}
		else
		{
			throw new IllegalArgumentException("illegal value " + value);
		}
	}



	public Object getValue()
	{
		return m_value;
	}



	public Object[] getValues()
	{
		return m_values.toArray(EMPTY_OBJECT_ARRAY);
	}



	public String toString()
	{
		return super.toString() + " [value = " + getValue() + "]";
	}




	public static class Type
	extends Control.Type
	{
		public static final Type	REVERB = new Type("REVERB");



		protected Type(String strName)
		{
			super(strName);
		}
	}
}



/*** EnumControl.java ***/

