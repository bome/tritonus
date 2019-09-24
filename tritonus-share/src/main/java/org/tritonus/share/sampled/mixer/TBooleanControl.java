/*
 *	TBooleanControl.java
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

import javax.sound.sampled.BooleanControl;

import org.tritonus.share.TDebug;




/**	Base class for classes implementing BooleanControl.
 */
public class TBooleanControl
extends BooleanControl
implements TControllable
{
	private TControlController	m_controller;



	public TBooleanControl(BooleanControl.Type type,
			       boolean bInitialValue)
	{
		this(type, bInitialValue, null);
	}



	public TBooleanControl(BooleanControl.Type type,
			       boolean bInitialValue,
			       TCompoundControl parentControl)
	{
		super(type, bInitialValue);
		if (TDebug.TraceControl)
		{
			TDebug.out("TBooleanControl.<init>: begin");
		}
		m_controller = new TControlController();
		if (TDebug.TraceControl)
		{
			TDebug.out("TBooleanControl.<init>: end");
		}
	}



	public TBooleanControl(BooleanControl.Type type,
			       boolean bInitialValue,
			       String strTrueStateLabel,
			       String strFalseStateLabel)
	{
		this(type, bInitialValue, strTrueStateLabel, strFalseStateLabel, null);
	}



	public TBooleanControl(BooleanControl.Type type,
			       boolean bInitialValue,
			       String strTrueStateLabel,
			       String strFalseStateLabel,
			       TCompoundControl parentControl)
	{
		super(type, bInitialValue, strTrueStateLabel, strFalseStateLabel);
		if (TDebug.TraceControl)
		{
			TDebug.out("TBooleanControl.<init>: begin");
		}
		m_controller = new TControlController();
		if (TDebug.TraceControl)
		{
			TDebug.out("TBooleanControl.<init>: end");
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



/*** TBooleanControl.java ***/
