/*
 *	CddaUtils.java
 */

/*
 *  Copyright (c) 2001 by Matthias Pfisterer
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


package org.tritonus.lowlevel.cdda;


import org.tritonus.lowlevel.cdda.CddaMidLevel;
import org.tritonus.lowlevel.cdda.cdparanoia.CdparanoiaMidLevel;
import org.tritonus.lowlevel.cdda.cooked_ioctl.CookedIoctlMidLevel;
import org.tritonus.share.TDebug;



/** Gets the preferred CDDA implementation.
 */
public class CddaUtils
{
	// TODO: use some automatic lookup mechanism.
	public static CddaMidLevel getCddaMidLevel()
	{
		CddaMidLevel	cddaMidLevel = new CdparanoiaMidLevel();
		// CddaMidLevel	cddaMidLevel = new CookedIoctlMidLevel();
		return cddaMidLevel;
	}
}



/*** CddaUtils.java ***/
