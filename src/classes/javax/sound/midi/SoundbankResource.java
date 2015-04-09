/*
 *	SoundbankResource.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 by Matthias Pfisterer
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



public abstract class SoundbankResource
{
	private Soundbank	m_soundbank;
	private String		m_strName;
	private Class		m_dataClass;



	protected SoundbankResource(Soundbank soundbank,
				    String strName,
				    Class dataClass)
	{
		m_soundbank = soundbank;
		m_strName = strName;
	}


	public Soundbank getSoundbank()
	{
		return m_soundbank;
	}



	public String getName()
	{
		return m_strName;
	}



	public Class getDataClass()
	{
		return m_dataClass;
	}



	public abstract Object getData();
}



/*** SoundbankResource.java ***/
