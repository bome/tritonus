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

abstract class IO{
  java.io.InputStream in=null;
  java.io.OutputStream out=null;
  java.net.Socket socket=null;

  byte[] ba;
  byte[] sa;
  byte[] ia;
  IO(){ ba=new byte[1]; sa=new byte[2]; ia=new byte[8]; }

  abstract int readInt() throws java.io.IOException;
  abstract int readShort() throws java.io.IOException;
  abstract  void writeInt(int val) throws java.io.IOException;
  abstract  void writeShort(int val) throws java.io.IOException;

  void setInputStream(java.io.InputStream in){this.in=in; }
  void setOutputStream(java.io.OutputStream out){ this.out=out; }
  void setSocket(java.net.Socket socket) throws java.io.IOException{
    this.socket=socket; 
    setInputStream(socket.getInputStream()); 
    setOutputStream(socket.getOutputStream()); 
  }
  java.net.Socket getSocket(){ return socket; }

  int readByte() throws java.io.IOException{
    int i=in.read(ba, 0, 1);
    if(i==-1){ throw new java.io.IOException(); }
    return ba[0]&0xff;
  }
  void readByte(byte[] array) throws java.io.IOException{
    readByte(array, 0, array.length);
  }
  void readByte(byte[] array, int begin, int length) throws java.io.IOException{
    int i=0;
    while(true){
      i=in.read(array, begin, length);
      if(i==-1){ throw new java.io.IOException(); }
      length-=i;
      begin+=i;
      if(length<=0) return;
    }
  }

  void readPad(int n) throws java.io.IOException{
    int i;
    while (n > 0){
      i=in.read(ba, 0, 1);
      if(i==-1){ throw new java.io.IOException(); }
      n--;
    }
  }

  void writeByte(byte val) throws java.io.IOException{
    ba[0]=val;
    out.write(ba, 0, 1);
  }

  void writeByte(int val) throws java.io.IOException{
    writeByte((byte)val);
  }

  void writeByte(byte[] array) throws java.io.IOException{
    writeByte(array, 0, array.length);
  }

  void writeByte(byte[] array, int begin, int length) throws java.io.IOException{
    out.write(array, begin, length);
  }

  void writePad(int n) throws java.io.IOException{
    int i;
    ba[0]=0;
    while(0<n){
      out.write(ba, 0, 1);
      n--;
    }
  }
  //void flush() throws java.io.IOException{ }
  void close() throws java.io.IOException{
    in.close(); out.close(); 
  }
}
