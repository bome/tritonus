/*
 *	RecordControl.java
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

package	javax.microedition.media.control;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.media.Control;
import javax.microedition.media.MediaException;



/**	TODO:
*/
public interface RecordControl
extends Control
{
	/**	TODO:
	*/
	public void setRecordStream(OutputStream stream);


	/**	TODO:
	*/
	public void setRecordLocation(String strLocator)
		throws IOException, MediaException;


	/**	TODO:
	*/
	public String getContentType();


	/**	TODO:
	*/
	public void startRecord();


	/**	TODO:
	*/
	public void stopRecord();


	/**	TODO:
	*/
	public void commit()
		throws IOException;


	/**	TODO:
	*/
	public int setRecordSizeLimit(int nSize)
		throws MediaException;


	/**	TODO:
	*/
	public void reset()
		throws IOException;
}



/*** RecordControl.java ***/
