/*
 *	Service.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 by Matthias Pfisterer
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

package org.tritonus.core;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;

import org.tritonus.share.TDebug;
import org.tritonus.share.ArraySet;



public class Service
{
	private static final String	BASE_NAME = "META-INF/services/";


	/**	Determines if the order of service providers is reversed.
		If this is true, the Iterator returned by providers(Class)
		iterates through the service provider classes backwards.
		This means that service providers that are in the user class
		path are first, then service providers in the extension class
		path, then those in the boot class path.
		This behaviour has the advantage that 'built-in' providers
		(those in the boot class path) can be 'shadowed' by
		providers in the extension and user class path.
	 */
	private static final boolean	REVERSE_ORDER = true;



	public static Iterator providers(Class cls)
	{
		if (TDebug.TraceService) { TDebug.out("Service.providers(): begin"); }
		String	strFullName = BASE_NAME + cls.getName();
		if (TDebug.TraceService) { TDebug.out("Service.providers(): full name: " + strFullName); }
		List	instancesList = createInstancesList(strFullName);
		Iterator	iterator = instancesList.iterator();
		if (TDebug.TraceService) { TDebug.out("Service.providers(): end"); }
		return iterator;
	}



	private static List<Object> createInstancesList(String strFullName)
	{
		if (TDebug.TraceService) { TDebug.out("Service.createInstancesList(): begin"); }
		List<Object>	providers = new ArrayList<Object>();
		Iterator	classNames = createClassNames(strFullName);
		if (classNames != null)
		{
			while (classNames.hasNext())
			{
				String	strClassName = (String) classNames.next();
				if (TDebug.TraceService) { TDebug.out("Service.createInstancesList(): Class name: " + strClassName); }
				try
				{
					ClassLoader	systemClassLoader = ClassLoader.getSystemClassLoader();
					Class	cls = Class.forName(strClassName, true, systemClassLoader);
					if (TDebug.TraceService) { TDebug.out("Service.createInstancesList(): now creating instance of " + cls); }
					Object	instance = cls.newInstance();
					if (REVERSE_ORDER)
					{
						providers.add(0, instance);
					}
					else
					{
						providers.add(instance);
					}
				}
				catch (ClassNotFoundException e)
				{
					if (TDebug.TraceService || TDebug.TraceAllExceptions) { TDebug.out(e); }
				}
				catch (InstantiationException e)
				{
					if (TDebug.TraceService || TDebug.TraceAllExceptions) { TDebug.out(e); }
				}
				catch (IllegalAccessException e)
				{
					if (TDebug.TraceService || TDebug.TraceAllExceptions) { TDebug.out(e); }
				}
				catch (Throwable e)
				{
					if (TDebug.TraceService || TDebug.TraceAllExceptions) { TDebug.out(e); }
				}
			}
		}
		if (TDebug.TraceService) { TDebug.out("Service.createInstancesList(): end"); }
		return providers;
	}



	private static Iterator<String> createClassNames(String strFullName)
	{
		if (TDebug.TraceService) TDebug.out("Service.createClassNames(): begin");
		Set<String>	providers = new ArraySet<String>();
		Enumeration	configs = null;
		try
		{
			configs = ClassLoader.getSystemResources(strFullName);
		}
		catch (IOException e)
		{
			if (TDebug.TraceService || TDebug.TraceAllExceptions) { TDebug.out(e); }
		}
		if (configs != null)
		{
			while (configs.hasMoreElements())
			{
				URL	configFileUrl = (URL) configs.nextElement();
				if (TDebug.TraceService) { TDebug.out("Service.createClassNames(): config: " + configFileUrl); }
				InputStream	input = null;
				try
				{
					input = configFileUrl.openStream();
				}
				catch (IOException e)
				{
					if (TDebug.TraceService || TDebug.TraceAllExceptions) { TDebug.out(e); }
				}
				if (input != null)
				{
					BufferedReader	reader = new BufferedReader(new InputStreamReader(input));
					try
					{
						String	strLine = reader.readLine();
						while (strLine != null)
						{
							strLine = strLine.trim();
							int	nPos = strLine.indexOf('#');
							if (nPos >= 0)
							{
								strLine = strLine.substring(0, nPos);
							}
							if (strLine.length() > 0)
							{
								providers.add(strLine);
								if (TDebug.TraceService) { TDebug.out("Service.createClassNames(): adding class name: " + strLine); }
							}
							strLine = reader.readLine();
						}
					}
					catch (IOException e)
					{
						if (TDebug.TraceService || TDebug.TraceAllExceptions) { TDebug.out(e); }
					}
				}
			}
		}
		Iterator<String> iterator = providers.iterator();
		if (TDebug.TraceService) TDebug.out("Service.createClassNames(): end");
		return iterator;
	}
}



/*** Service.java ***/
