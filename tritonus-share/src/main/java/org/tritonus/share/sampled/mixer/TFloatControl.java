/*
 *	TFloatControl.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2001 by Matthias Pfisterer
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

package org.tritonus.share.sampled.mixer;

import javax.sound.sampled.FloatControl;

import org.tritonus.share.TDebug;




/**	Base class for classes implementing Line.
 */
public class TFloatControl
extends FloatControl
implements TControllable
{
	private TControlController	m_controller;



	public TFloatControl(FloatControl.Type type,
			     float fMinimum,
			     float fMaximum,
			     float fPrecision,
			     int nUpdatePeriod,
			     float fInitialValue,
			     String strUnits)
	{
		super(type,
		      fMinimum,
		      fMaximum,
		      fPrecision,
		      nUpdatePeriod,
		      fInitialValue,
		      strUnits);
		if (TDebug.TraceControl)
		{
			TDebug.out("TFloatControl.<init>: begin");
		}
		m_controller = new TControlController();
		if (TDebug.TraceControl)
		{
			TDebug.out("TFloatControl.<init>: end");
		}
	}



	public TFloatControl(FloatControl.Type type,
			     float fMinimum,
			     float fMaximum,
			     float fPrecision,
			     int nUpdatePeriod,
			     float fInitialValue,
			     String strUnits,
			     String strMinLabel,
			     String strMidLabel,
			     String strMaxLabel)
	{
		super(type,
		      fMinimum,
		      fMaximum,
		      fPrecision,
		      nUpdatePeriod,
		      fInitialValue,
		      strUnits,
		      strMinLabel,
		      strMidLabel,
		      strMaxLabel);
		if (TDebug.TraceControl)
		{
			TDebug.out("TFloatControl.<init>: begin");
		}
		m_controller = new TControlController();
		if (TDebug.TraceControl)
		{
			TDebug.out("TFloatControl.<init>: end");
		}
	}



	public void setParentControl(TCompoundControl compoundControl)
	{
		m_controller.setParentControl(compoundControl);
	}



	public TCompoundControl getParentControl()
	{
		return m_controller.getParentControl();
	}



	public void commit()
	{
		m_controller.commit();
	}
}



/*** TFloatControl.java ***/
