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

import java.net.*;
import java.io.*;

public class JEsd {

  public static final String version = "0.0.4";

  public static final int ESD_MASK_MODE=0x0F00;
  public static final int ESD_STREAM=0x0000;
  public static final int ESD_SAMPLE=0x0100;
  public static final int ESD_ADPCM=0x0200;

  public static final int ESD_MASK_FUNC=0xF000;
  public static final int ESD_PLAY=0x1000;

  /* functions for streams only */
  public static final int ESD_MONITOR=0x0000;
  public static final int ESD_RECORD=0x2000;

  /* functions for samples only */
  public static final int ESD_STOP=0x0000;
  public static final int ESD_LOOP=0x2000;

  /* length of the audio buffer size */
  public static final int ESD_BUF_SIZE=(4*1024);

  public static final int ESD_NAME_MAX=128;

  /* length of the authorization key, octets */
  public static final int ESD_KEY_LEN=16;

  public static final int ESM_ERROR=0;
  public static final int ESM_ON_STANDBY=1;
  public static final int ESM_ON_AUTOSTANDBY=2;
  public static final int ESM_RUNNING=3;

  public static final int ESD_MASK_CHAN=0x00F0;
  public static final int ESD_MONO=0x0010;
  public static final int ESD_STEREO=0x0020;

  public static final int ESD_MASK_BITS=0x000F;
  public static final int ESD_BITS8=0x0000;
  public static final int ESD_BITS16=0x0001;

  public static final int ESD_DEFAULT_PORT=16001;

  public static final int ESD_DEFAULT_RATE=44100;

  // implied on inital client connection
  static final int ESD_PROTO_CONNECT=0; 

  // pseudo "security" functionality
  static final int ESD_PROTO_LOCK=1;
  static final int ESD_PROTO_UNLOCK=2;

  // stream functionality: play, record, monitor
  static final int ESD_PROTO_STREAM_PLAY=3;
  static final int ESD_PROTO_STREAM_REC=4;
  static final int ESD_PROTO_STREAM_MON=5;

  // sample functionality: cache, free, play, loop, EOL, kill
  static final int ESD_PROTO_SAMPLE_CACHE=6; 
  static final int ESD_PROTO_SAMPLE_FREE=7;
  static final int ESD_PROTO_SAMPLE_PLAY=8;
  static final int ESD_PROTO_SAMPLE_LOOP=9;
  static final int ESD_PROTO_SAMPLE_STOP=10;
  static final int ESD_PROTO_SAMPLE_KILL=11;

  // free and reclaim /dev/dsp functionality
  static final int ESD_PROTO_STANDBY=12;
  static final int ESD_PROTO_RESUME=13;

  static final int ESD_PROTO_SAMPLE_GETID=14;
  static final int ESD_PROTO_STREAM_FILT=15;

  // esd remote management
  static final int ESD_PROTO_SERVER_INFO=16;
  static final int ESD_PROTO_ALL_INFO=17;
  static final int ESD_PROTO_SUBSCRIBE=18;
  static final int ESD_PROTO_UNSUBSCRIBE=19;

  // esd remote control
  static final int ESD_PROTO_STREAM_PAN=20;
  static final int ESD_PROTO_SAMPLE_PAN=21;

  // esd status
  static final int ESD_PROTO_STANDBY_MODE=22;

  private static final int ESD_ENDIAN_KEY=(('E'<<24)+('N'<<16)+('D'<<8)+('N'));

  static private byte[] key=null;

  private Socket socket=null;
  private IO io=null;

  public JEsd() throws JEsdException{
    this("localhost", ESD_DEFAULT_PORT); 
  }
  public JEsd(int port) throws JEsdException{
    this("localhost", port); 
  }
  public JEsd(String host) throws JEsdException{
    this(host, ESD_DEFAULT_PORT); 
  }
  public JEsd(String host, int port) throws JEsdException{
    try{
      socket=new Socket(host, port);
      io=new IOMSB();
      io.setSocket(socket);
      send_auth();
    }
    catch(Exception e){
      throw new JEsdException(e.toString());
    }
  }

  private void send_auth() throws IOException{
    if(key==null){
      key=new byte[ESD_KEY_LEN];
      String esd_auth=System.getProperty("user.home",".")+
                      File.separator+".esd_auth";
      try{	
        FileInputStream fis=new FileInputStream(esd_auth);
        fis.read(key);
        fis.close();
      }
      catch(Exception e){
        try{	
          java.util.Random random=new java.util.Random(System.currentTimeMillis());
          random.nextBytes(key);
          FileOutputStream fos=new FileOutputStream(esd_auth);
          fos.write(key);
          fos.close();
        }catch(Exception ee){ }
      }
    }
    io.writeByte(key);
    io.writeInt(ESD_ENDIAN_KEY);
    int reply=io.readInt();
    if(reply==0){
      //io.close();
    }
  }

  public int lock(){ return status_op(ESD_PROTO_LOCK); }
  public int unlock(){ return status_op(ESD_PROTO_UNLOCK); }
  public int standby(){ return status_op(ESD_PROTO_STANDBY); }
  public int resume(){ return status_op(ESD_PROTO_RESUME); }

  private int status_op(int op){
    int reply=0;
    try{
      io.writeInt(op);
      send_auth();
      reply=io.readInt();
    }
    catch(Exception e){
    }
    return reply;
  }

  public static JEsd play_stream(int format, int rate, 
				 String host, String name) throws JEsdException{
    return stream_op(ESD_PROTO_STREAM_PLAY, "play_stream",
		     format, rate, host, name);
  }

  public static JEsd play_stream_fallback(int format, int rate, 
					  String host, String name) throws JEsdException{
    try{
      return play_stream(format, rate, host, name );
    }
    catch(JEsdException e){
      // ??
      throw e;
    }
  }

  static private byte[] genName(String name){
    byte[] tmp=(name==null ? null : name.getBytes());
    if(tmp==null || tmp.length<ESD_NAME_MAX){
      byte[] foo=new byte[ESD_NAME_MAX];
      if(tmp!=null) System.arraycopy(tmp, 0, foo, 0, tmp.length);
      else tmp[0]=0;
      tmp=foo;
    }
    return tmp;
  }

  public static JEsd monitor_stream(int format, int rate, 
				    String host, String name) throws JEsdException{
    return stream_op(ESD_PROTO_STREAM_MON, "filter_stream",
		     format, rate, host, name);
  }

  public static JEsd filter_stream(int format, int rate, 
				   String host, String name) throws JEsdException{
    return stream_op(ESD_PROTO_STREAM_FILT, "filter_stream",
		     format, rate, host, name);
  }

  public static JEsd record_stream(int format, int rate, 
				   String host, String name) throws JEsdException{
    return stream_op(ESD_PROTO_STREAM_REC, "record_stream",
		     format, rate, host, name);
  }

  public static JEsd record_stream_fallback(int format, int rate, 
					    String host, String name) throws JEsdException{
    try{ 
      JEsd jesd=record_stream(format, rate, host, name );
      return jesd;
    }
    catch(JEsdException e){
      // ??
      throw e;
    }
  }

  private static JEsd stream_op(int op, String error, int format, int rate,
			        String host, String name) throws JEsdException{
    JEsd jesd=new JEsd(host);
    try{
      byte[] tmp=genName(name);
      jesd.io.writeInt(op);
      jesd.io.writeInt(format);
      jesd.io.writeInt(rate);
      jesd.io.writeByte(tmp, 0, ESD_NAME_MAX);
    //jesd.set_socket_buffers(format, rate, 44100 );
    }
    catch(Exception e){
      throw new JEsdException(e.toString());
    }
    return jesd;
  }

/*
  private int set_socket_buffers(int src_format, int src_rate, int base_rate){
    int buf_size = ESD_BUF_SIZE;
    if(src_rate>0) buf_size=(buf_size*base_rate)/src_rate;
    if((src_format&ESD_MASK_BITS)==ESD_BITS16) buf_size*=2;
    if(!((src_format&ESD_MASK_CHAN)==ESD_MONO)) buf_size *= 2;
    io.getSocket().setSendBufferSize(buf_size);
    io.getSocket().setReceiveBufferSize(buf_size);
    return buf_size;
  }
*/

  public int sample_cache(int format, int rate, int size, String name){
    try{
      byte[] tmp=genName(name);
      io.writeInt(ESD_PROTO_SAMPLE_CACHE);
      io.writeInt(format);
      io.writeInt(rate);
      io.writeInt(size);
      io.writeByte(tmp, 0, ESD_NAME_MAX);
      int id=io.readInt();
      return id;
    }
    catch(Exception e){
    }
    return -1;
  }
  public int confirm_sample_cache(){
    try{
      int id=io.readInt();
      return id;
    }
    catch(Exception e){
    }
    return -1;
  }

  public int sample_getid(String name){
    try{
      io.writeInt(ESD_PROTO_SAMPLE_GETID);
      byte[] tmp=genName(name);
      io.writeByte(tmp, 0, ESD_NAME_MAX);
      int id=io.readInt();
      return id;
    }
    catch(Exception e){
    }
    return -1;
  }

  public int sample_free(int sample){
    return sample_op(ESD_PROTO_SAMPLE_FREE, "sample_free", sample);
  }
  public int sample_play(int sample){
    return sample_op(ESD_PROTO_SAMPLE_PLAY, "sample_play", sample);
  }
  public int sample_loop(int sample){
    return sample_op(ESD_PROTO_SAMPLE_LOOP, "sample_loop", sample);
  }
  int sample_stop(int sample){ 
    return sample_op(ESD_PROTO_SAMPLE_STOP, "sample_stop", sample);
  }

  private int sample_op(int op, String error, int sample){
    try{
      io.writeInt(op);
      io.writeInt(sample);
      int is_ok=io.readInt();
      return is_ok;
    }
    catch(Exception e){
    }
    return -1;
  }

  public int file_cache(String name_prefix, String filename)
    throws java.io.IOException{
    WAVEFile wave=new WAVEFile(filename);
    int frame_count=wave.getFrameCount();
    int in_channels=wave.getChannels();
    int in_rate=wave.getRate();
    int in_width=wave.getSampleFormatWidth();
    int length=wave.getTrackBytes();

    int out_bits, out_channels, out_rate;
    int out_mode=JEsd.ESD_STREAM, out_func=JEsd.ESD_PLAY;

    if(in_width==8) out_bits=JEsd.ESD_BITS8; 
    else if(in_width==16) out_bits=JEsd.ESD_BITS16; 
    else{
      return -1;
    }

    int bytes_per_frame=(in_width*in_channels)/8;

    if(in_channels==1) out_channels=JEsd.ESD_MONO;
    else if(in_channels==2) out_channels=JEsd.ESD_STEREO;
    else{
      return -1;
    }

    int out_format=out_bits|out_channels|out_mode|out_func;
    out_rate=in_rate;

    String name="";
    if(name_prefix!=null){
      if(name_prefix.length()<=ESD_NAME_MAX-2) name=name_prefix;
      else name=name_prefix.substring(0, ESD_NAME_MAX-3);
      name=name+":";
    }
    name=name+((filename.length()<ESD_NAME_MAX-name.length()) ?
                filename :
                filename.substring(0, ESD_NAME_MAX-name.length()-1));

    int sample_id=sample_cache(out_format, out_rate, length, name);

    send_file(wave, bytes_per_frame);
    wave.close();
    int confirm_id=confirm_sample_cache();

    if(confirm_id!=sample_id) return -1;

    return sample_id;
  }

  public static void play_file(String name_prefix, 
			       String filename, boolean fallback)
    throws java.io.IOException{
    WAVEFile wave=new WAVEFile(filename);
    int frame_count=wave.getFrameCount();
    int in_channels=wave.getChannels();
    int in_rate=wave.getRate();
    int in_width=wave.getSampleFormatWidth();

    int out_bits, out_channels, out_rate;
    int out_mode=JEsd.ESD_STREAM, out_func=JEsd.ESD_PLAY;

    if(in_width==8) out_bits=JEsd.ESD_BITS8; 
    else if(in_width==16) out_bits=JEsd.ESD_BITS16; 
    else{
      return;
    }

    int bytes_per_frame=(in_width*in_channels)/8;

    if(in_channels==1) out_channels=JEsd.ESD_MONO;
    else if(in_channels==2) out_channels=JEsd.ESD_STEREO;
    else{
      return;
    }

    int out_format=out_bits|out_channels|out_mode|out_func;
    out_rate=in_rate;

    String name="";
    if(name_prefix!=null){
      if(name_prefix.length()<=ESD_NAME_MAX-2) name=name_prefix;
      else name=name_prefix.substring(0, ESD_NAME_MAX-3);
      name=name+":";
    }
    name=name+(filename.length()<ESD_NAME_MAX-name.length() ?
               filename :
               filename.substring(0, ESD_NAME_MAX-name.length()-1));

    JEsd jesd=null;
    try{
      if(fallback)
        jesd=JEsd.play_stream_fallback(out_format, out_rate, null, name);
      else
        jesd=JEsd.play_stream(out_format, out_rate, null, filename);
    }
    catch(Exception e){
      System.out.println(e);
      return;
    }
    jesd.send_file(wave, bytes_per_frame);
    jesd.close();
  }

  public void send_file(WAVEFile file, int bytes_per_frame){
    byte[] buf=new byte[JEsd.ESD_BUF_SIZE];
    int buf_frames=JEsd.ESD_BUF_SIZE/bytes_per_frame;
    int frames_read=0, bytes_written=0;
    while((frames_read=file.readFrame(buf, buf_frames))>0){
      if(write(buf, frames_read * bytes_per_frame)<=0) return;
    }
  }
  public int write(byte[] array, int size){ return write(array, 0, size); }
  public int write(byte[] array, int off, int size){
    try{
      io.writeByte(array, off, size);
      return size;
    }
    catch(Exception e){
    }
    return -1;
  }
  public int read(byte[] array, int size){
    try{
      io.readByte(array, 0, size);
      return size;
    }
    catch(Exception e){
    }
    return -1;
  }
  public void close(){
    try{ io.close(); }
    catch(Exception e){}
  }
}
