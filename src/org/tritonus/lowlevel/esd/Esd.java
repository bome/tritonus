/*
 *	Esd.java
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


package	org.tritonus.lowlevel.esd;




public interface Esd
{
    public static final int		ESD_STREAM	= 0x0000;
    public static final int		ESD_PLAY	= 0x1000;
    public static final int		ESD_BITS8	= 0x0000;
    public static final int		ESD_BITS16	= 0x0001;
    public static final int		ESD_MONO	= 0x0010;
    public static final int		ESD_STEREO	= 0x0020;
}



/*** Esd.java ***/
