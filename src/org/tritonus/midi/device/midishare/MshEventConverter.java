/*
 *	MshEventConverter.java
 */

/*
 *   Copyright © Grame 2000 for the Tritonus project by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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
 *  Grame Research Laboratory, 9, rue du Garet 69001 Lyon - France
 *  grame@grame.fr
 *
 */


package	org.tritonus.midi.device.midishare;

import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.SysexMessage;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.InvalidMidiDataException;

import	org.tritonus.TDebug;
import	org.tritonus.midi.*;


import	grame.midishare.*;


final public class MshEventConverter {

	//---------------------------------------------
	// Java MidiEvent to MidiShare event conversion
	//---------------------------------------------
	
	static public int decodeEvent(MidiEvent event) throws InvalidMidiDataException, MidiException
	{
		return decodeMessage(event.getMessage(), event.getTick());
	}

	static public int decodeMessage(MidiMessage event, long lTick) throws InvalidMidiDataException, MidiException
	{
		if (event instanceof ShortMessage)
		{
			return decodeShortMessage((ShortMessage) event, lTick);
		}
		else if (event instanceof SysexMessage)
		{
			return  decodeSysexMessage((SysexMessage) event, lTick);
		}
		else if (event instanceof MetaMessage)
		{
			return  decodeMetaMessage((MetaMessage) event, lTick);
		}
		throw new InvalidMidiDataException("MshEventConverter.decodeMessage(): UNKNOWN MidiMessage class");
	}
	
	static  private int decodeShortMessage(ShortMessage shortMessage, long lTime) throws InvalidMidiDataException, MidiException
	{
		int nChannel = shortMessage.getChannel();
		
		switch (shortMessage.getCommand())
		{
		case 0x80:	// note off
			return  makeKeyOffEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
		
		case 0x90:	// note on
			return  makeKeyOnEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
		
		case 0xa0:	// polyphonic key pressure
			return  makeKeyPressureEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
		
		case 0xb0:	// controller/channel mode
			return makeControlChangeEvent(lTime, nChannel, shortMessage.getData1(), shortMessage.getData2());
		
		case 0xc0:	// program change
			return makeProgramChangeEvent(lTime, nChannel, shortMessage.getData1());
		
		case 0xd0:	// channel  aftertouch
			return makeChannelPressureEvent(lTime, nChannel, shortMessage.getData1());
			
		case 0xe0:	// pitch change
			return  makePitchBendEvent(lTime, nChannel, MidiUtils.get14bitValue(shortMessage.getData1(), shortMessage.getData2()));
		
		case 0xF0:
			//system realtime/system exclusive
			// these should not occur in a ShortMessage
			// Hey, system realtime are short messages!
			throw new InvalidMidiDataException("MshEventConverter.decodeShortMessage(): F0 Byte");
			
		default:
			TDebug.out("MshEventConverter.decodeShortMessage(): UNKNOWN EVENT TYPE!");
			throw new InvalidMidiDataException("MshEventConverter.decodeShortMessage(): UNKNOWN EVENT TYPE!");
		}
	}

	
	static private int decodeMetaMessage(MetaMessage message, long lTick) throws InvalidMidiDataException, MidiException
	{
		byte[]	abMessageData = message.getData();
		System.out.println("Meta "+ message.getType());
		
		switch (message.getType()) {
		
			case 0:
				return  makeNumSeqEvent(lTick,abMessageData);
			case 1:
				return  makeTextEvent(lTick,abMessageData);
			case 2:
				return  makeCopyrightEvent(lTick,abMessageData);
			case 3:
				return  makeSeqNameEvent(lTick,abMessageData);
			case 4:
				return  makeInstrNameEvent(lTick,abMessageData);
			case 5:
				return  makeLyricEvent(lTick,abMessageData);
			case 6:
				return  makeMarkerEvent(lTick,abMessageData);
			case 7:
				return  makeCuePointEvent(lTick,abMessageData);
			case 0x20:
				return  makeChanPrefEvent(lTick,abMessageData);
			case 0x2F:
				return  makeEndTrackEvent(lTick,abMessageData);
			case 0x51:
				return  makeTempoEvent(lTick,abMessageData);
			case 0x54:
				return  makeSMPTEOffsetEvent(lTick,abMessageData);
			case 0x58:
				return  makeTimeSignEvent(lTick,abMessageData);
			case 0x59:
				return  makeKeySignEvent(lTick,abMessageData);
			case 0x7F:
				return  makeSpecificEvent(lTick,abMessageData);
					
			default:
				TDebug.out("MshEventConverter.decodeMetaMessage(): UNKNOWN EVENT TYPE!");
				throw new InvalidMidiDataException("MshEventConverter.decodeMetaMessage(): UNKNOWN EVENT TYPE!");
		}
	}


	//------------------------------------------
	// MidiShare event to Java event conversion
	//------------------------------------------
	
	static public MidiEvent encodeEvent(int event) throws InvalidMidiDataException
	{
		MidiMessage message = encodeMessage(event);
		return new MidiEvent(message, Midi.GetDate(event));
	}

	
	static public MidiMessage encodeMessage(int event) throws InvalidMidiDataException
	{
		switch (Midi.GetType(event)) {
		
			case Midi.typeNote: // should never occur
					TDebug.out("MshEventConverter.encodeMessage(): TYPE NOTE!");
					throw new InvalidMidiDataException("MshEventConverter.encodeMessage(): TYPE NOTE!");
			case Midi.typeKeyOn:
					return makeKeyOnMessage(event);
			case Midi.typeKeyOff:
					return makeKeyOffMessage(event);
			case Midi.typeKeyPress:
					return makeKeyPressureMessage(event);
			case Midi.typeCtrlChange:
					return makeControlChangeMessage(event);
			case Midi.typeProgChange:
					return makeProgChangeMessage(event);
			case Midi.typeChanPress:
					return makeChannelPressureMessage(event);
			case Midi.typePitchWheel:
					return makePitchBendMessage(event);
			case Midi.typeSeqNum:
					return makeTextMessageAux(event, 0);
			case Midi.typeText:
					return makeTextMessageAux(event, 1);
			case Midi.typeCopyright:
					return makeTextMessageAux(event, 2);
			case Midi.typeSeqName:
					return makeTextMessageAux(event, 3);
			case Midi.typeInstrName:
					return makeTextMessageAux(event, 4);
			case Midi.typeLyric:
					return makeTextMessageAux(event, 5);
			case Midi.typeMarker:
					return makeTextMessageAux(event, 6);
			case Midi.typeCuePoint:
					return makeTextMessageAux(event, 7);
			case Midi.typeChanPrefix:
					return makeChanPrefixMessage(event);
			case Midi.typeEndTrack:
					return makeEndTrackMessage(event);
			case Midi.typeTempo:
					return makeTempoMessage(event);
			case Midi.typeSMPTEOffset:
					return makeSMPTEOffsetMessage(event);
			case Midi.typeTimeSign:
					return makeTimeSignMessage(event);
			case Midi.typeKeySign:
					return makeKeySignMessage(event);
			case Midi.typeSpecific:
					return makeTextMessageAux(event, 0x7F);
			case Midi.typeSysEx:
					return makeSysexMessage(event);
		
			default:
					throw new InvalidMidiDataException("MshEventConverter.encodeMessage()");
		}
	}

	
	//----------
	// Key Off
	//----------
	
	static private int makeKeyOffEvent(long lTime, int nChannel, int nNote, int nVelocity) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeKeyOff);
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTime);
		Midi.SetChan(ev,nChannel);
		Midi.SetField(ev,0,nNote);
		Midi.SetField(ev,1,nVelocity);
		return ev;	
	}	

	static private MidiMessage makeKeyOffMessage(int ev) throws InvalidMidiDataException
	{
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0x80, Midi.GetChan(ev), Midi.GetField(ev,0),  Midi.GetField(ev,1));
		return shortMessage;
	}	

	//----------
	// Key On
	//----------

	static private int makeKeyOnEvent(long lTime, int nChannel, int nNote, int nVelocity) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeKeyOn);	
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTime);
		Midi.SetChan(ev,nChannel);
		Midi.SetField(ev,0,nNote);
		Midi.SetField(ev,1,nVelocity);
		return ev;	
	}

	static private MidiMessage makeKeyOnMessage(int ev) throws InvalidMidiDataException
	{
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0x90, Midi.GetChan(ev), Midi.GetField(ev,0),  Midi.GetField(ev,1));
		return shortMessage;
	}	

	//----------
	// Key Press
	//----------
	
	static private int makeKeyPressureEvent(long lTime, int nChannel, int nNote, int nPressure) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeKeyPress);
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTime);
		Midi.SetChan(ev,nChannel);
		Midi.SetField(ev,0,nNote);
		Midi.SetField(ev,1,nPressure);
		return ev;	
	}

	static private MidiMessage makeKeyPressureMessage(int ev) throws InvalidMidiDataException
	{
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0xA0, Midi.GetChan(ev), Midi.GetField(ev,0),  Midi.GetField(ev,1));
		return shortMessage;
	}	

	//------------
	// Ctrl Change
	//------------

	static private int makeControlChangeEvent(long lTime, int nChannel, int nControl, int nValue) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeCtrlChange);
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTime);
		Midi.SetChan(ev,nChannel);
		Midi.SetField(ev,0,nControl);
		Midi.SetField(ev,1,nValue);
		return ev;	
	}

	static private MidiMessage makeControlChangeMessage(int ev) throws InvalidMidiDataException
	{
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0xB0, Midi.GetChan(ev), Midi.GetField(ev,0),  Midi.GetField(ev,1));
		return shortMessage;
	}	
	
	//------------
	// Prog Change
	//------------

	static private int makeProgramChangeEvent(long lTime, int nChannel, int nProgram) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeProgChange);
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTime);
		Midi.SetChan(ev,nChannel);
		Midi.SetField(ev,0,nProgram);
		return ev;	
	}

	static private MidiMessage makeProgChangeMessage(int ev) throws	InvalidMidiDataException
	{
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0xC0, Midi.GetChan(ev), Midi.GetField(ev,0), 0);
		return shortMessage;
	}	

	//------------
	// Chan Press
	//------------

	static private int makeChannelPressureEvent(long lTime, int nChannel, int nPressure) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeChanPress);
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTime);
		Midi.SetChan(ev,nChannel);
		Midi.SetField(ev,0,nPressure);
		return ev;	
	}

	static private MidiMessage makeChannelPressureMessage(int ev) throws InvalidMidiDataException
	{
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0xD0, Midi.GetChan(ev), Midi.GetField(ev,0), 0);
		return shortMessage;
	}	

	//------------
	// Pitch Bend
	//------------

	static private int makePitchBendEvent(long lTime, int nChannel, int nPitch) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typePitchWheel);
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTime);
		Midi.SetChan(ev,nChannel);
		Midi.SetField(ev,0,MidiUtils.get14bitLSB(nPitch));
		Midi.SetField(ev,1,MidiUtils.get14bitMSB(nPitch));
		return ev;	
	}

	static private MidiMessage makePitchBendMessage(int ev) throws	InvalidMidiDataException
	{
		ShortMessage shortMessage = new ShortMessage();
		shortMessage.setMessage(0xE0, Midi.GetChan(ev), Midi.GetField(ev,0), Midi.GetField(ev,1));
		return shortMessage;
	}	

	//-----------------
	// Systeme Exclusif
	//-----------------

	static private int decodeSysexMessage(SysexMessage event, long lTime) throws MidiException
	{
		byte[]	abMessageData = event.getData();
		int ev = Midi.NewEv(Midi.typeSysEx);
		if (ev==0) throw new MidiException("No more MidiShare events");
		
		// the last byte of abMessageData is 0xF7, it should not be put in the MidiShare event
	
		Midi.SetDate(ev,(int)lTime);
		for (int i = 0; i < abMessageData.length-1; i++) {Midi.AddField(ev,abMessageData[i]);}
		 	
		if (Midi.CountFields(ev) != abMessageData.length-1) { // check event
		 	Midi.FreeEv(ev);	;
		 	throw new MidiException("No more MidiShare events");
		}
		return ev;	
	}
	
	
	static private MidiMessage makeSysexMessage(int ev) throws InvalidMidiDataException
	{
		int n = Midi.CountFields(ev);
		byte[] abMessageData = new byte[n+1];
		SysexMessage sysexMessage = new SysexMessage();
		
		for (int i = 0; i < n; i++) {abMessageData[i] = (byte)Midi.GetField(ev,i);}
		
		// the last byte of abMessageData must be 0xF7
		
		abMessageData[n] =  (byte)0xF7;
		sysexMessage.setMessage(0xF0,abMessageData, abMessageData.length);
		return  sysexMessage;
	}
		
	//------------------------
	// Meta events (MIDIFiles)
	//------------------------

	// A vÈrifier
	
	static private int makeNumSeqEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeSeqNum);
		int num;
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTick);
		Midi.SetChan(ev,0);
		num = (int)abMessageData[0];
		num <<= 8;
		num|= (int)abMessageData[1];
		Midi.SetField(ev,0, num);
		return ev;	
	}
	
	// A vÈrifier
	
	static private int makeTextEventAux(int ev, long lTick, byte[] abMessageData) throws MidiException 
	{
		if (ev==0) throw new MidiException("No more MidiShare events");
		
		Midi.SetDate(ev,(int)lTick);
		Midi.SetChan(ev,0);
		for (int i = 0; i< abMessageData.length; i++) Midi.AddField(ev, abMessageData[i]);
		return ev;
	}
	
	static private MetaMessage makeTextMessageAux(int event, int type) throws InvalidMidiDataException
	{
		MetaMessage metaMessage = new MetaMessage();
		int len = Midi.CountFields(event);
		byte[] data = new byte[len];
		for (int i = 0; i<len; i++) {data[i] = (byte)Midi.GetField(event,i);}
		metaMessage.setMessage(type,data, len);
		return metaMessage;
	}
	
	static private int makeTextEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		return makeTextEventAux(Midi.NewEv(Midi.typeText),lTick,abMessageData);
	}
		
	static private int makeCopyrightEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		return makeTextEventAux(Midi.NewEv(Midi.typeCopyright),lTick,abMessageData);
	}
	
	static private int makeSeqNameEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		return makeTextEventAux(Midi.NewEv(Midi.typeSeqName),lTick,abMessageData);
	}
	
	static private int makeInstrNameEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		return makeTextEventAux(Midi.NewEv(Midi.typeInstrName),lTick,abMessageData);
	}
	
	static private int makeLyricEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		return makeTextEventAux(Midi.NewEv(Midi.typeLyric),lTick,abMessageData);
	}
	
	static private int makeMarkerEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		return makeTextEventAux(Midi.NewEv(Midi.typeMarker),lTick,abMessageData);
	}
	
	static private int makeCuePointEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		return makeTextEventAux(Midi.NewEv(Midi.typeCuePoint),lTick,abMessageData);
	}
	
	static private int makeChanPrefEvent(long lTick,byte[] abMessageData) throws MidiException
	{	
		int ev = Midi.NewEv(Midi.typeChanPrefix);
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTick);
		Midi.SetChan(ev,0);
		Midi.SetField(ev,0, abMessageData[0]);
		return ev;	
	}
	
	static private MidiMessage makeChanPrefixMessage(int event) throws InvalidMidiDataException
	{
		MetaMessage metaMessage = new MetaMessage();
		byte[] data = new byte[1];
		data[0] = (byte)Midi.GetField(event,0);
		metaMessage.setMessage(0x20, data, 1);
		return metaMessage;
	}

	
	static private int makeEndTrackEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeEndTrack);
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTick);
		Midi.SetChan(ev,0);
		return ev;	
	}
	
	static private MidiMessage makeEndTrackMessage(int event) throws InvalidMidiDataException
	{
		MetaMessage metaMessage = new MetaMessage();
		metaMessage.setMessage(0x2F, new byte[0], 0);
		return metaMessage;
	}
	
	static private int makeTempoEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeTempo);
		int tempo;
		if (ev==0) throw new MidiException("No more MidiShare events");
		
		 
		Midi.SetDate(ev,(int)lTick);
		Midi.SetChan(ev,0);
		tempo = signedByteToUnsigned(abMessageData[0]);
		tempo <<= 8;
		tempo|=  signedByteToUnsigned(abMessageData[1]);
		tempo <<= 8;
		tempo|=  signedByteToUnsigned(abMessageData[2]);
		Midi.SetField(ev, 0, tempo);
		return ev;	
	}
	
	static private MidiMessage makeTempoMessage(int event) throws InvalidMidiDataException
	{
		MetaMessage metaMessage = new MetaMessage();
		int tempo = Midi.GetField(event,0);
		short s = (short)tempo;
		byte[] data = new byte[3];
		data[0] = (byte)(tempo>>16);
		data[1] = (byte)(s>>8);
		data[2] = (byte)(s & 0xFF);
		metaMessage.setMessage(0x51, data, 3);
		return metaMessage;
	}
	
	static private int makeSMPTEOffsetEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeSMPTEOffset);
		int tmp;
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTick);
		Midi.SetChan(ev,0);
		tmp =  abMessageData[0]*3600;        	/* hours -> sec.       */
        	tmp = tmp + abMessageData[1]*30;	/* min.  -> sec.       */
          	tmp+= abMessageData[2];          	/* sec.                */
          	Midi.AddField( ev,tmp);
          	tmp= abMessageData[3]* 100;              /* frame count *100    */
          	tmp+= abMessageData[4];                  /* fractionnal frame   */
          	Midi.AddField( ev, tmp);
		return ev;	
	}
	
	static private MidiMessage makeSMPTEOffsetMessage(int event) throws InvalidMidiDataException
	{
		MetaMessage metaMessage = new MetaMessage();
		int l = Midi.GetField(event,0);
		byte[] data = new byte[5];
		data[0] = (byte)(l/3600);
		l%= 3600;
		data[1] = (byte)(l/60);
		data[2] = (byte)(l%60);
		l= (int)Midi.GetField(event, 1);
		data[3] = (byte)(l/100);
		data[4] = (byte)(l%100);
		metaMessage.setMessage(0x54, data, 5);
		return metaMessage;
	}
	
	static private int makeTimeSignEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeTimeSign);
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTick);
		Midi.SetChan(ev,0);
		Midi.SetField(ev,0, abMessageData[0]);
		Midi.SetField(ev,1, abMessageData[1]);
		Midi.SetField(ev,2, abMessageData[2]);
		Midi.SetField(ev,3, abMessageData[3]);
		return ev;	
	}
	
	static private MidiMessage makeTimeSignMessage(int event) throws InvalidMidiDataException
	{
		MetaMessage metaMessage = new MetaMessage();
		int l = Midi.GetField(event,0);
		byte[] data = new byte[4];
		data[0] = (byte)Midi.GetField(event,0);
		data[1] = (byte)Midi.GetField(event,1);
		data[2] = (byte)Midi.GetField(event,2);
		data[3] = (byte)Midi.GetField(event,3);
		metaMessage.setMessage(0x58, data, 4);
		return metaMessage;
	}
	
	static private int makeKeySignEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		int ev = Midi.NewEv(Midi.typeKeySign);
		if (ev==0) throw new MidiException("No more MidiShare events");
		 
		Midi.SetDate(ev,(int)lTick);
		Midi.SetChan(ev,0);
		Midi.SetField(ev,0, abMessageData[0]);
		Midi.SetField(ev,1, abMessageData[1]);
		return ev;	
	}
	
	static private MidiMessage makeKeySignMessage(int event) throws	InvalidMidiDataException
	{
		MetaMessage metaMessage = new MetaMessage();
		int l = Midi.GetField(event,0);
		byte[] data = new byte[2];
		data[0] = (byte)Midi.GetField(event,0);
		data[1] = (byte)Midi.GetField(event,1);
		metaMessage.setMessage(0x59, data, 2);
		return metaMessage;
	}
	
	static private int makeSpecificEvent(long lTick,byte[] abMessageData) throws MidiException
	{
		return makeTextEventAux(Midi.NewEv(Midi.typeSpecific),lTick,abMessageData);
	}
	
	
	private static int signedByteToUnsigned(byte b){return (b >= 0) ? (int) b :  256 + (int) b;}
	
}
