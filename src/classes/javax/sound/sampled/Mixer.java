/*
 *	Mixer.java
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




public interface Mixer
extends Line
{
	public Mixer.Info getMixerInfo();

	public Line.Info[] getSourceLineInfo();

	public Line.Info[] getTargetLineInfo();

	public Line.Info[] getSourceLineInfo(Line.Info info);

	public Line.Info[] getTargetLineInfo(Line.Info info);

	public boolean isLineSupported(Line.Info info);

	public Line getLine(Line.Info info)
		throws LineUnavailableException;

	public int getMaxLines(Line.Info info);

	public Line[] getSourceLines();

	public Line[] getTargetLines();

	public void synchronize(Line[] aLines,
				boolean bMaintainSync);


	public void unsynchronize(Line[] aLines);


	public boolean isSynchronizationSupported(Line[] aLines,
				boolean bMaintainSync);



	public static class Info
	{
		private String		m_strName;
		private String		m_strVendor;
		private String		m_strDescription;
		private String		m_strVersion;



		protected Info(String strName,
			    String strVendor,
			    String strDescription,
			    String strVersion)
		{
			m_strName = strName;
			m_strVendor = strVendor;
			m_strDescription = strDescription;
			m_strVersion = strVersion;
		}



		public boolean equals(Object obj)
		{
			return super.equals(obj);
		}



		public int hashCode()
		{
			return super.hashCode();
		}



		public String getName()
		{
			return m_strName;
		}



		public String getVendor()
		{
			return m_strVendor;
		}



		public String getDescription()
		{
			return m_strDescription;
		}



		public String getVersion()
		{
			return m_strVersion;
		}



		public String toString()
		{
			return super.toString() /* + TODO: .... */;
		}

	}
}



/*** Mixer.java ***/
