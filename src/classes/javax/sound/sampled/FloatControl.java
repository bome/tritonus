/*
 *	FloatControl.java
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

import org.tritonus.share.TDebug;



public abstract class FloatControl
extends Control
{
	private float		m_fValue;
	private float		m_fMinimum;
	private float		m_fMaximum;
	private float		m_fPrecision;
	private int		m_nUpdatePeriod;
	private String		m_strUnits;
	private String		m_strMinLabel;
	private String		m_strMidLabel;
	private String		m_strMaxLabel;




	protected FloatControl(Type type,
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
		super(type);
		if (TDebug.TraceControl)
		{
			TDebug.out("FloatControl.<init>: begin");
		}
		m_fMinimum = fMinimum;
		m_fMaximum = fMaximum;
		m_fPrecision = fPrecision;
		m_nUpdatePeriod = nUpdatePeriod;

		//$$fb 2000-12-02: incompatible with Sun implementation...
		//setValue(fInitialValue);
		m_fValue = fInitialValue;

		m_nUpdatePeriod = nUpdatePeriod;
		m_strMinLabel = strMinLabel;
		m_strMidLabel = strMidLabel;
		m_strMaxLabel = strMaxLabel;
		if (TDebug.TraceControl)
		{
			TDebug.out("FloatControl.<init>: end");
		}
	}



	protected FloatControl(Type type,
			       float fMinimum,
			       float fMaximum,
			       float fPrecision,
			       int nUpdatePeriod,
			       float fInitialValue,
			       String strUnits)
	{
		this(type,
		     fMinimum,
		     fMaximum,
		     fPrecision,
		     nUpdatePeriod,
		     fInitialValue,
		     strUnits,
		     "Left",
		     "Center",
		     "Right");
	}


	/** Set the value of the control.
	    If subclasses need to override this method, super.setValue()
	    should be called at the beginning of the overriding
	    implementation.
	*/
	public void setValue(float fValue)
	{
		m_fValue = fValue;
	}



	public float getValue()
	{
		return m_fValue;
	}



	public float getMaximum()
	{
		return m_fMaximum;
	}



	public float getMinimum()
	{
		return m_fMinimum;
	}



	public String getUnits()
	{
		return m_strUnits;
	}



	public String getMinLabel()
	{
		return m_strMinLabel;
	}



	public String getMidLabel()
	{
		return m_strMidLabel;
	}



	public String getMaxLabel()
	{
		return m_strMaxLabel;
	}



	public float getPrecision()
	{
		return m_fPrecision;
	}



	public int getUpdatePeriod()
	{
		return m_nUpdatePeriod;
	}



	public void shift(float fFrom,
			  float fTo,
			  int nMicroseconds)
	{
		/* This default implementation is required by the
		   specification. */
		setValue(fTo);
	}



	public String toString()
	{
		return super.toString() + " [value = " + getValue() + "]";
	}





	public static class Type
	extends Control.Type
	{
		public static final Type	MASTER_GAIN = new Type("MASTER_GAIN");
		public static final Type	AUX_SEND = new Type("AUX_SEND");
		public static final Type	AUX_RETURN = new Type("AUX_RETURN");
		public static final Type	REVERB_SEND = new Type("REVERB_SEND");
		public static final Type	REVERB_RETURN = new Type("REVERB_RETURN");
		public static final Type	VOLUME = new Type("VOLUME");
		public static final Type	PAN = new Type("PAN");
		public static final Type	BALANCE = new Type("BALANCE");
		//$$fb 2000-12-02: SAMPLE_RATE had been missing
		public static final Type        SAMPLE_RATE = new Type("SAMPLE RATE"); 



		protected Type(String strName)
		{
			super(strName);
		}
	}
}



/*** FloatControl.java ***/

