/*
 *	TConfiguration.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
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
 *
 */


package	org.tritonus;

import	java.util.ArrayList;
import	java.util.List;
import	java.util.Locale;
import	java.util.MissingResourceException;
import	java.util.ResourceBundle;
import	java.util.StringTokenizer;



public class TConfiguration
{
	private static final String[]	EMPTY_STRING_ARRAY = new String[0];
	private static ResourceBundle	sm_configuration = ResourceBundle.getBundle("org.tritonus.Configuration");



	public static String getResourceString(String strResourceName)
	{
		// Contract.check(strResourceName != null);
		if (TDebug.TraceConfiguration)
		{
			TDebug.out("org.tritonus.TConfiguration.getResourceString(): looking up resource: " + strResourceName);
		}
		String	strValue = null;
		try
		{
			if (sm_configuration != null)
			{
				strValue = sm_configuration.getString(strResourceName);
			}
		}
		catch (MissingResourceException mre)
		{
		}
		if (TDebug.TraceConfiguration)
		{
			if (strValue != null)
			{
				TDebug.out("org.tritonus.TConfiguration.getResourceString(): found value: " + strValue);
			}
			else
			{
				TDebug.out("org.tritonus.TConfiguration.getResourceString(): value not found, returning null.");
			}
		}
		return strValue;
	}



	/**
	 *	Take the given string and chop it up into a series
	 *	of strings on whitespace boundries.  This is useful
	 *	for trying to get an array of strings out of the
	 *	resource file.
	 */
	public static String[] tokenize(String input)
	{
		List		vTokens = new ArrayList();
		StringTokenizer	tokenizer = new StringTokenizer(input);

		while (tokenizer.hasMoreTokens())
		{
			vTokens.add(tokenizer.nextToken());
		}
		return (String[]) vTokens.toArray(EMPTY_STRING_ARRAY);
	}

}


/*** TConfiguration.java ***/
