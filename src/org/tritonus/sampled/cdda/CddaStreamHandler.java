/*
 *	CddaStreamHandler.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.sampled.cdda;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.tritonus.share.TDebug;



public class CddaStreamHandler
extends	URLStreamHandler
{
	public URLConnection openConnection(URL url)
	{
		if (TDebug.TraceCdda) { TDebug.out("CddaStreamHandler.openConnection():begin"); }
		URLConnection	connection = null;
		if (url.getFile().equals(""))
		{
			connection = new CddaDriveListConnection(url);
		}
		else if (url.getRef() == null)
		{
			connection = new CddaTocConnection(url);
		}
		else
		{
			connection = new CddaDataConnection(url);
		}
		if (TDebug.TraceCdda) { TDebug.out("CddaStreamHandler.openConnection():end"); }
		return connection;
	}
}



/*** CddaStreamHandler.java ****/
