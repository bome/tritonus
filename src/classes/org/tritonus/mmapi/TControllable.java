/*
 *	TControllable.java
 */

package	org.tritonus.mmapi;


import	javax.microedition.media.Control;
import	javax.microedition.media.Controllable;



public class TControllable
implements Controllable
{
	private Control[]	m_aControls;



	public TControllable(Control[] aControls)
	{
		super();
		m_aControls = aControls;
	}



	// SPEC-TODO: should be ..(Class controlType)
	public Control getControl(String strType)
	{
		/*
		  TODO: some passed class object should be used that describes the type of the desired control.
		*/
		Class	controlClass = Control.class;
		for (int i = 0; i < m_aControls.length; i++)
		{
			if (controlClass.isInstance(m_aControls[i]))
			{
				return m_aControls[i];
			}
		}
		return null;
	}



	public Control[] getControls()
	{
		Control[]	aControls = new Control[m_aControls.length];
		System.arraycopy(m_aControls, 0, aControls, 0, m_aControls.length);
		return aControls;
	}

}



/*** TControllable.java ***/
