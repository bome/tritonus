/*
 *	TControllable.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package	org.tritonus.mmapi;


import javax.microedition.media.Control;
import javax.microedition.media.Controllable;



public abstract class TControllable
implements Controllable
{
	private static final String		DEFAULT_PACKAGE_NAME = "javax.microedition.media.control";

	private Control[]	m_aControls;



	protected TControllable(Control[] aControls)
	{
		super();
		m_aControls = aControls;
	}



	public Control getControl(String strControlType)
	{
		/*	This check is brain-damaged, but is required
			by the specification.
		*/
		if (strControlType == null)
		{
			throw new IllegalArgumentException("control type is null");
		}
		/*	The following check can throw an IllegalStateException.
		 */
		checkState();

		Class	controlClass = getClassFromString(strControlType);
		if (controlClass != null)
		{
			return getControl(controlClass);
		}
		return null;
	}


	/*
	  This is how the signature should look like.
	*/
	private Control getControl(Class controlClass)
	{
		Control	control = null;
		for (int i = 0; i < m_aControls.length; i++)
		{
			if (controlClass.isInstance(m_aControls[i]))
			{
				control = m_aControls[i];
			}
		}
		return control;
	}



	public Control[] getControls()
	{
		/*	The following check can throw an IllegalStateException.
		 */
		checkState();

		Control[]	aControls = new Control[m_aControls.length];
		System.arraycopy(m_aControls, 0, aControls, 0, m_aControls.length);
		return aControls;
	}



	private void checkState()
	{
		if (! isStateLegalForControls())
		{
			throw new IllegalStateException("Controllable is not in a state that is legal to call Controllable methods");
		}
	}


	protected abstract boolean isStateLegalForControls();


	private static Class getClassFromString(String strClassName)
	{
		if (strClassName.indexOf(".") == -1)
		{
			strClassName = DEFAULT_PACKAGE_NAME + strClassName;
		}
		Class	cls = null;
		try
		{
			cls = Class.forName(strClassName);
		}
		catch (ClassNotFoundException e)
		{
			// IGNORED; null will be returned.
		}
		return cls;
	}
}



/*** TControllable.java ***/
