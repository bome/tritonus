/*
 *	Control.java
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



public abstract class Control
{
	private Type	m_type;



	protected Control(Type type)
	{
		if (TDebug.TraceControl)
		{
			TDebug.out("Control.<init>: begin");
		}
		m_type = type;
		if (TDebug.TraceControl)
		{
			TDebug.out("Control.<init>: end");
		}
	}



	public Type getType()
	{
		return m_type;
	}


	public String toString()
	{
		return super.toString() + "[type = " + getType() + "]";
	}




	public static class Type
	{
		private String	m_strName;



		protected Type(String strName)
		{
			m_strName = strName;
		}



		public final boolean equals(Object obj)
		{
			return super.equals(obj);
		}



		public final int hashCode()
		{
			return super.hashCode();
		}



		public final String toString()
		{
			return m_strName;
		}
	}
}



/*** Control.java ***/

