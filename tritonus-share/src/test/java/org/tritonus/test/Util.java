/*
 *	Util.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer
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

package org.tritonus.test;

import	java.io.File;
import	java.io.FileInputStream;
import	java.io.IOException;



public class Util
{
	public static void dumpByteArray(byte[] ab)
	{
		for (int i = 0; i < ab.length; i++)
		{
			System.out.print(" " + ab[i]);
		}
		System.out.println("");
	}



	// returns true if equal
	public static boolean compareByteArrays(byte[] ab1, int nOffset1, byte[] ab2, int nOffset2, int nLength)
	{
		for (int i = 0; i < nLength; i++)
		{
			if (ab1[i + nOffset1] != ab2[i +  nOffset2])
			{
				return false;
			}
		}
		return true;
	}



	public static byte[] getByteArrayFromFile(File file)
		throws IOException
	{
		long	lLength = file.length();
		byte[]	abData = new byte[(int) lLength];
		FileInputStream	fis = new FileInputStream(file);
		int	nBytesRemaining = (int) lLength;
		int	nWriteStart = 0;
		while (nBytesRemaining > 0)
		{
			int	nBytesRead = fis.read(abData, nWriteStart, nBytesRemaining);
			nBytesRemaining -= nBytesRead;
			nWriteStart += nBytesRead;
		}
		fis.close();
		return abData;
	}



	public static void sleep(long milliseconds)
	{
		try
		{
			Thread.sleep(milliseconds);
		}
		catch (InterruptedException e)
		{
		}
	}
}



/*** Util.java ***/
