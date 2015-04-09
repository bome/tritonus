/*
 *	MidiDevice.java
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

package javax.sound.midi;

import java.util.List;



public interface MidiDevice
{
	public MidiDevice.Info getDeviceInfo();



	public void open()
		throws MidiUnavailableException;



	public void close();



	public boolean isOpen();



	public long getMicrosecondPosition();



	public int getMaxReceivers();



	public int getMaxTransmitters();



	public Receiver getReceiver()
		throws MidiUnavailableException;



	public Transmitter getTransmitter()
		throws MidiUnavailableException;



	public List<Receiver> getReceivers();


	public List<Transmitter> getTransmitters();



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


		public final boolean equals(Object obj)
		{
			return super.equals(obj);
		}



		public final int hashCode()
		{
			return super.hashCode();
		}



		public final String getName()
		{
			return m_strName;
		}



		public final String getVendor()
		{
			return m_strVendor;
		}



		public final String getDescription()
		{
			return m_strDescription;
		}



		public final String getVersion()
		{
			return m_strVersion;
		}



		public final String toString()
		{
			return super.toString() + "[name=" + getName() + ", vendor=" + getVendor() + ", description=" + getDescription() + ", version=" + getVersion() + "]";
		}
	}
}



/*** MidiDevice.java ***/
