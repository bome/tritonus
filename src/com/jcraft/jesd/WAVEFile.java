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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class WAVEFile{
  private WAVEHeader wheader;
  InputStream is=null;
  public WAVEFile(String name) throws IOException{
    wheader=WAVEHeader.parse(name);
    try{
      is=new FileInputStream(name);
      is.skip(wheader.getHeader().length);
    }
    catch(Exception e){
    }
  }
  WAVEFile(int format, int rate, int datasize) throws IOException{
    wheader=new WAVEHeader(format, rate, datasize);
  }

  public byte[] getHeader(){
    return wheader.getHeader();
  }

  public int readFrame(byte[] buf, int fcount){
    int i;
    try{ i=is.read(buf, 0, fcount*getFrameSize()); }
    catch(Exception e){ return -1; }
    return i/getFrameSize();
  }

  public int getFrameCount(){ return wheader.datasize/getFrameSize(); }
  public int getFrameSize(){ return wheader.channels*(wheader.bits/8); }
  public int getChannels(){ return wheader.channels; }
  public int getRate(){ return wheader.rate; }
  public int getSampleFormatWidth(){ return wheader.bits; }
  public int getTrackBytes(){ return wheader.datasize; }

  public void close() throws IOException{ is.close(); }
}

class WAVEHeader{
  static final int HEADERSIZE=44;
  private static byte[] RIFF="RIFF".getBytes();
  private static byte[] WAVE="WAVE".getBytes();
  private static byte[] fmt_="fmt ".getBytes();
  private static byte[] data="data".getBytes();
    
  int format, rate, datasize;
  int channels=1;
  int bits=8;

  byte[] header=null;

  WAVEHeader(int format, int rate, int datasize){
    this.format=format;
    this.rate=rate;
    this.datasize=datasize;
  }

  static WAVEHeader parse(String name){
    try{
      int format=0;
      int rate=0;
      int datasize=0;
      int channels=0;
      int bps=0;              // averate byte per second
      int balign=0;           // block align
      int bits=0;             // 8/16
	
      FileInputStream fis=new FileInputStream(name);
      byte[] tmp=new byte[4];
      fis.read(tmp, 0, 4);
      IO io=null;
//    if(riff[0]=='R') io=new IOLSB(); else io=new IOMSB();
      io=new IOLSB();
      io.setInputStream(fis);
      int header_data_size=io.readInt();
      int len=header_data_size;
      io.readByte(tmp, 0, 4); len-=4;         // WAVE
      while(len>0){
	io.readByte(tmp, 0, 4); len-=4;       // chunk id;
	String s=new String(tmp);
	int size=io.readInt(); len-=4;        // chunk size
	if(s.equals("fmt ")){
          int tag=io.readShort();           // format tag
	  size-=2; len-=2;
	  channels=io.readShort();          // channel count
	  rate=io.readInt();                // sample rate
	  bps=io.readInt();                 // averate byte per second
	  balign=io.readShort();            // block align
	  size-=12; len-=12;
	  if(tag==1){                       // WAVE_FORMAT_PCM 
	    bits=io.readShort();            // bits per sample
	    size-=2; len-=2;
	  }
	  continue;
	}
	if(s.equals("data")){
	  datasize=size;          // size
	  len-=datasize;
	  continue;
	}
	//System.out.println("unknown chunk: "+s);
        io.readPad(size);
        len-=size;
      }
      try{ io.close(); }catch(Exception e){};

      //fis.close();

      WAVEHeader wh=new WAVEHeader(format, rate, datasize);
      wh.channels=channels;
      wh.bits=bits;

      fis=new FileInputStream(name);
      wh.header=new byte[header_data_size-datasize];
      fis.read(wh.header, 0, wh.header.length);
      fis.close();

      return wh;
    }
    catch(Exception e){ return null;}
  }

  byte[] getHeader(){
    if(header!=null) return header;

    if((format & JEsd.ESD_MASK_CHAN)==JEsd.ESD_STEREO) channels=2;
    if((format & JEsd.ESD_MASK_BITS)==JEsd.ESD_BITS16) bits=16;

    try{
      ByteArrayOutputStream baos=new ByteArrayOutputStream(HEADERSIZE);
      IOLSB io=new IOLSB();
      io.setOutputStream(baos);
      
      io.writeByte(RIFF);
      io.writeInt(datasize+(HEADERSIZE-8));
      io.writeByte(WAVE);

      io.writeByte(fmt_);         // format chunk
      io.writeInt(16);            // size
      io.writeShort(1);           // format tag
      io.writeShort(channels);    // channel count
      io.writeInt(rate);          // sample rate
      io.writeInt(rate*(bits/8)); // averate byte per second
      io.writeShort(1);           // block align
      io.writeShort(bits);        // bits per sample

      io.writeByte(data);         // data chunk
      io.writeInt(datasize);          // size

      header=baos.toByteArray();
      return header;
    }
    catch(Exception e){
      //System.out.println(e);
    }
    return null;
  }
}
