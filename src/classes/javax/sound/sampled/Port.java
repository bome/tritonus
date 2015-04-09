/*
 *	Port.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer
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



public interface Port
extends Line
{

	public static class Info
	extends	Line.Info
	{
		public static Class getPortClass()
		{
			try
			{
				return Class.forName("javax.sound.sampled.Port");
			}
			catch (ClassNotFoundException e)
			{
				if (TDebug.TraceAllExceptions)
				{
					TDebug.out(e);
				}
			}
			return null;
		}



		public static final Info	MICROPHONE = new Info(getPortClass(), "MICROPHONE", true);
		public static final Info	LINE_IN = new Info(getPortClass(), "LINE_IN", true);
		public static final Info	COMPACT_DISC = new Info(getPortClass(), "COMPACT_DISC", true);
		public static final Info	SPEAKER = new Info(getPortClass(), "SPEAKER", false);
		public static final Info	HEADPHONE = new Info(getPortClass(), "HEADPHONE", false);
		public static final Info	LINE_OUT = new Info(getPortClass(), "LINE_OUT", false);


		private String		m_strName;
		private boolean		m_bIsSource;
	



		public Info(Class lineClass,
			    String strName,
			    boolean bIsSource)
		{
			super(lineClass);
			m_strName = strName;
			m_bIsSource = bIsSource;
		}



		public String getName()
		{
			return m_strName;
		}



		public boolean isSource()
		{
			return m_bIsSource;
		}



		public boolean matches(Line.Info info)
		{
			return super.matches(info) &&
				this.getName().equals(((Port.Info) info).getName()) &&
				this.isSource() == ((Port.Info) info).isSource();
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
			return super.toString() + "[name=" + getName() + ", source = " + isSource() + "]";
		}
	}
}



/*** Port.java ***/
