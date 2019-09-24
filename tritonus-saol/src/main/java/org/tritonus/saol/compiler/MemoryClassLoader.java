/*
 *	MemoryClassLoader.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer
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
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.saol.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;



public class MemoryClassLoader
extends ClassLoader
{
	public Class findClass(String strName,
			       byte[] classData)
	{
		Class	cls = defineClass(strName, classData, 0, classData.length);
		return cls;
	}


	/*	For testing
	 */
	public static void main(String[] args)
	{
		try
		{
			FileInputStream	fis = new FileInputStream(new File("Instrument.class"));
			ByteArrayOutputStream	baos = new ByteArrayOutputStream();
			byte[]	buffer = new byte[4096];
			while (true)
			{
				int	nRead = fis.read(buffer);
				if (nRead == -1)
				{
					break;
				}
				baos.write(buffer, 0, nRead);
			}
			MemoryClassLoader	mcl = new MemoryClassLoader();
			Class	cls = mcl.findClass("Instrument", baos.toByteArray());
			System.out.println("class loaded: " + cls.getName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


/*** MemoryClassLoader.java ***/
