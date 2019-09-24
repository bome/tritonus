/*
 *	TControlController.java
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

import org.tritonus.share.TDebug;




/**	Base class for classes implementing Line.
 */
public class TControlController
implements TControllable
{
	/**	The parent (compound) control.
		In case this control is part of a compound control, the parentControl
		property is set to a value other than null.
	 */
	private TCompoundControl	m_parentControl;


	public TControlController()
	{
	}



	public void setParentControl(TCompoundControl compoundControl)
	{
		m_parentControl = compoundControl;
	}


	public TCompoundControl getParentControl()
	{
		return m_parentControl;
	}


	public void commit()
	{
		if (TDebug.TraceControl)
		{
			TDebug.out("TControlController.commit(): called [" + this.getClass().getName() + "]");
		}
		if (getParentControl() != null)
		{
			getParentControl().commit();
		}
	}
}



/*** TControlController.java ***/
