/*
 *	Mp3Encoder.java
 */

/*
 *  Copyright (c) 2000 by Florian Bomers <florian@bome.com>
 *
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */


import	java.io.IOException;
import	java.io.File;
import	java.util.ArrayList;
import	java.util.Iterator;
import	java.util.Collection;

import	javax.sound.sampled.*;


public class Mp3Encoder {

	private static boolean DEBUG = false;
	private static boolean dumpExceptions=false;
	private static boolean traceConverters=false;

	// currently, there is no convenient method in JavaSound to specify non-standard Encodings.
	// using tritonus' proposal to overcome this.
	private static final AudioFormat.Encoding MPEG1L3=org.tritonus.share.sampled.Encodings.getEncoding("MPEG1L3");
	private static final AudioFileFormat.Type MP3=org.tritonus.share.sampled.AudioFileTypes.getType("MP3", "mp3");

	private static AudioInputStream getInStream(String filename) throws IOException {
		File file = new File(filename);
		AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(file);
		} catch (Exception e) {
			if (dumpExceptions) {
				e.printStackTrace();
			} else {
				System.out.println("Error: "+e.getMessage());
			}
		}
		if (ais == null) {
			throw new IOException("Cannot open \""+filename+"\"");
		}
		return ais;
	}

	public static String stripExtension(String filename) {
		int ind = filename.lastIndexOf(".");
		if (ind==-1
		        || ind == filename.length()
		        || filename.lastIndexOf(File.separator)>ind) {
			// when dot is at last position,
			// or a slash is after the dot, there isn't an extension
			return filename;
		}
		return filename.substring(0, ind);
	}

	public static int writeFile(String inFilename) {
		int writtenBytes=-1;
		try {
			AudioInputStream ais=getInStream(inFilename);
			AudioFormat sourceFormat=ais.getFormat();
			AudioFormat.Encoding targetEncoding=MPEG1L3;
			AudioFileFormat.Type targetType=MP3;
			
			System.out.println("Input format: "+sourceFormat);
			
			// construct the target filename
			String outFilename=stripExtension(inFilename)+"."+targetType.getExtension();
			
			// build the output format
			AudioFormat outFormat=new AudioFormat(targetEncoding,
							      sourceFormat.getSampleRate(),
							      AudioSystem.NOT_SPECIFIED,
							      sourceFormat.getChannels(),
							      AudioSystem.NOT_SPECIFIED,
							      AudioSystem.NOT_SPECIFIED,
							      false); // endianness doesn't matter
			// construct a converted stream
			AudioInputStream outStream=null;
			if (!AudioSystem.isConversionSupported(outFormat, sourceFormat)) {
				if (DEBUG) {
					System.out.println("Direct conversion not possible.");
					System.out.println("Trying with intermediate PCM format.");
				}
				AudioFormat intermediateFormat=new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
									       sourceFormat.getSampleRate(),
									       16,
									       sourceFormat.getChannels(),
									       2*sourceFormat.getChannels(), // frameSize
									       sourceFormat.getSampleRate(),
									       false);
				if (AudioSystem.isConversionSupported(intermediateFormat, sourceFormat)) {
					// intermediate conversion is supported
					ais=AudioSystem.getAudioInputStream(intermediateFormat, ais);
				}
			}
			outStream=AudioSystem.getAudioInputStream(outFormat, ais);
			if (outStream==null) {
				throw new Exception("conversion not supported");
			}
			if (DEBUG) {
				System.out.println("Got converted AudioInputStream: "+outStream.getClass().getName());
			}
			System.out.println("Output format: "+outStream.getFormat());
			// write the file
			System.out.println("Writing "+outFilename+"...");
			writtenBytes = AudioSystem.write(outStream, targetType, new File(outFilename));
			if (DEBUG) {
				System.out.println("Effective parameters of output file:");
				try {
					System.out.println("  Quality      = "+System.getProperty
							   ("tritonus.lame.effective.quality", "<none>"));
					System.out.println("  Bitrate      = "+System.getProperty
							   ("tritonus.lame.effective.bitrate", "<none>"));
					System.out.println("  Channel Mode = "+System.getProperty
							   ("tritonus.lame.effective.chmode", "<none>"));
					System.out.println("  VBR mode     = "+System.getProperty
							   ("tritonus.lame.effective.vbr", "<none>"));
					System.out.println("  Sample rate  = "+System.getProperty
							   ("tritonus.lame.effective.samplerate", "<none>"));
					System.out.println("  Encoding     = "+System.getProperty
							   ("tritonus.lame.effective.encoding", "<none>"));
				} catch (Throwable t1) {}
			}
		} catch (Throwable t) {
			if (dumpExceptions) {
				t.printStackTrace();
			} else {
				System.out.println("Error: "+t.getMessage());
			}
		}
		return writtenBytes;
	}

	public static void main(String[] args) {
		int firstFileIndex=-1;

		if (args.length==0) {
			usage();
		}

		// parse options
		try {
			for (int i=0; i<args.length; i++) {
				String arg=args[i];
				if (arg.equals("--help")) {
					usage();
				}

				if (arg.length()>3 || arg.length()<2 || !arg.startsWith("-")) {
					firstFileIndex=i;
					break;
				}
				char cArg=arg.charAt(1);
				// options without parameter
				if (cArg=='v') {
					DEBUG=true;
					continue;
				} else if (cArg=='e') {
					dumpExceptions=true;
					continue;
				} else if (cArg=='t') {
					traceConverters=true;
					continue;
				} else if (cArg=='V') {
					try {
						System.setProperty("tritonus.lame.vbr", "true");
					} catch (Throwable t1) {}
					continue;
				} else if (cArg=='h') {
					usage();
				}
				// options with parameter
				if (args.length<i+2) {
					throw new Exception("Missing parameter or unrecognized option "+arg+".");
				}
				String param=args[i+1];
				i++;
				switch (cArg) {
				case 'q':
					try {
						System.setProperty("tritonus.lame.quality", param);
					} catch (Throwable t2) {}
					break;
				case 'b':
					try {
						System.setProperty("tritonus.lame.bitrate", param);
					} catch (Throwable t3) {}
					break;
				default:
					throw new Exception("Unrecognized option "+arg+".");
				}
			}
			if (firstFileIndex==-1) {
				throw new Exception("No input file(s) are given.");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		if (traceConverters) {
			org.tritonus.share.TDebug.TraceAudioConverter=true;
		}
		int inputFiles=0;
		int success=0;
		for (int i=firstFileIndex; i<args.length; i++) {
			long time=System.currentTimeMillis();
			int bytes=writeFile(args[i]);
			time=System.currentTimeMillis()-time;
			inputFiles++;
			if (bytes>=0) {
				if (bytes>0) {
					success++;
				}
				System.out.println("Wrote "+bytes+" bytes in "
						   +(time / 60000)+"m "+((time/1000) % 60)+"s  ("
						   +(time/1000)+"s).");
			}
		}
		System.out.println("From "+inputFiles+" input file"+(inputFiles==1?"":"s")+", "
				   +success+" file"+(success==1?" was":"s were")+" converted successfully.");
		System.exit(0);
	}


	/**	Display a message of how to call this program.
	 */
	public static void usage() {
		System.out.println("Mp3Encoder - convert audio files to mp3 (layer III of MPEG 1, MPEG 2 or MPEG 2.5");
		System.out.println("java Mp3Encoder <options> <source file> [<source file>...]");
		System.out.println("The output file(s) will be named like the source file(s) but");
		System.out.println("with mp3 file extension.");
		System.out.println("");
		System.out.println("You need LAME 3.88 or later. Get it from http://sourceforge.net/projects/lame");
		System.out.println("");
		System.out.println("<options> may be a combination of the following:");
		System.out.println("-q <quality>  Quality of output mp3 file. In VBR mode, this affects");
		System.out.println("              the size of the mp3 file. (Default middle)");
		System.out.println("              One of: lowest, low, middle, high, highest");
		System.out.println("-b <bitrate>  Bitrate in KBit/s. Useless in VBR mode. (Default 128)");
		System.out.println("              One of: 32 40 48 56 64 80 96 112 128 160 192 224 256 320 (MPEG1)");
		System.out.println("              Or: 8 16 24 32 40 48 56 64 80 96 112 128 144 160 (MPEG2 and MPEG2.5");

		System.out.println("-V            VBR (variable bit rate) mode. Slower, but potentially better");
		System.out.println("              quality. (Default off)");
		System.out.println("-v            Be verbose.");
		System.out.println("-e            Debugging: Dump stack trace of exceptions.");
		System.out.println("-t            Debugging: trace execution of converters.");
		System.out.println("-h | --help   Show this message.");
		System.exit(1);
	}

}

/*** Mp3Encoder.java ***/

