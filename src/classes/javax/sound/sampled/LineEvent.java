/*
 *	LineEvent.java
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

import java.util.EventObject;



public class LineEvent
extends EventObject
{
	static final long serialVersionUID = -1274246333383880410L;

	private Type	m_type;
	private long	m_lPosition;



	public LineEvent(Line line,
			 Type type,
			 long lPosition)
	{
		super(line);
		m_type = type;
		m_lPosition = lPosition;
	}



	public Line getLine()
	{
		return (Line) getSource();
	}



	public Type getType()
	{
		return m_type;
	}



	public long getFramePosition()
	{
		return m_lPosition;
	}



	public String toString()
	{
		return super.toString() + "[type=" + getType() + ", framePosition=" + getFramePosition() + "]";
	}





	public static class Type
	{
		public static final Type	OPEN = new Type("OPEN");
		public static final Type	CLOSE = new Type("CLOSE");
		public static final Type	START = new Type("START");
		public static final Type	STOP = new Type("STOP");


		private String	m_strName;



		public Type(String strName)
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



		public String toString()
		{
			return m_strName;
		}
	}
}



/*** LineEvent.java ***/
