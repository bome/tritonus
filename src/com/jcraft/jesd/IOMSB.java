/* JEsd
 * Copyright (C) 1999 JCraft Inc.
 *  
 * Written by: 1999 ymnk
 *   
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
   
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.jcraft.jesd;

final class IOMSB extends IO {
  int readInt() throws java.io.IOException{
    int i=in.read(ia, 0, 4);
    if(i==-1){ throw new java.io.IOException(); }
    i=ia[0]&0xff;
    i=((i<<8)&0xffff)|(ia[1]&0xff);
    i=((i<<8)&0xffffff)|(ia[2]&0xff);
    i=(i<<8)|(ia[3]&0xff);
    return i;
  }
  int readShort() throws java.io.IOException{
    int i=in.read(sa, 0, 2);
    if(i==-1){ throw new java.io.IOException(); }
    i=sa[0]&0xff;
    i=((i<<8)&0xffff)|(sa[1]&0xff);
    return i;
  }
  void writeInt(int val) throws java.io.IOException{
    ia[0]=(byte)((val >> 24) & 0xff);
    ia[1]=(byte)((val >> 16) & 0xff);
    ia[2]=(byte)((val >> 8) & 0xff);
    ia[3]=(byte)((val) & 0xff);
    out.write(ia, 0, 4);
  }
  void writeShort(int val) throws java.io.IOException{
    sa[0]=(byte)((val >> 8) & 0xff);
    sa[1]=(byte)((val) & 0xff);
    out.write(sa, 0, 2);
  }
}
