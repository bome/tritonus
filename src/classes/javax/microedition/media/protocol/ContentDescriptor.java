/*
 *	ContentDescriptor.java
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

package	javax.microedition.media.protocol;



/**	TODO:
*/
public class ContentDescriptor
{
	/**	TODO:
	*/
	private String		m_strContentType = null;



	/**	TODO:
	*/
	public ContentDescriptor(String strContentType)
	{
		m_strContentType = strContentType;
	}



	/**	TODO:
	*/
	public String getContentType()
	{
		return m_strContentType;
	}
}



/*** ContentDescriptor.java ***/
