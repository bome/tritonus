/*
 *	ASequencer0.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	org.tritonus.lowlevel.alsa;

import	javax.sound.midi.MidiEvent;

import	org.tritonus.TDebug;

/**	The lowest level of interface to the ALSA sequencer.
 */
public class ASequencer0
{
/*                                   	*/
/* definition of sequencer event types 	*/
/*                                   	*/

/* 0-4: system messages
 * event data type = snd_seq_result_t
 */
	public static final int	SND_SEQ_EVENT_SYSTEM =		0;
	public static final int	SND_SEQ_EVENT_RESULT =		1;
/* 2-4: reserved */

/* 5-9: note messages (channel specific)
 * event data type = snd_seq_ev_note
 */
	public static final int	SND_SEQ_EVENT_NOTE =		5;
	public static final int	SND_SEQ_EVENT_NOTEON =		6;
	public static final int	SND_SEQ_EVENT_NOTEOFF =		7;
	public static final int	SND_SEQ_EVENT_KEYPRESS =	8;
/* 9-10: reserved */
	
/* 10-19: control messages (channel specific)
 * event data type = snd_seq_ev_ctrl
 */
	public static final int	SND_SEQ_EVENT_CONTROLLER =	10;
	public static final int	SND_SEQ_EVENT_PGMCHANGE =	11;
	public static final int	SND_SEQ_EVENT_CHANPRESS =	12;
	public static final int	SND_SEQ_EVENT_PITCHBEND =	13;	/* from -8192 to 8191 */
	public static final int	SND_SEQ_EVENT_CONTROL14 =	14;	/* 14 bit controller value */
	public static final int	SND_SEQ_EVENT_NONREGPARAM =	15;	/* 14 bit NRPN */
	public static final int	SND_SEQ_EVENT_REGPARAM =	16;	/* 14 bit RPN */
/* 18-19: reserved */

/* 20-29: synchronisation messages
 * event data type = snd_seq_ev_ctrl
 */
	public static final int	SND_SEQ_EVENT_SONGPOS =		20;	/* Song Position Pointer with LSB and MSB values */
	public static final int	SND_SEQ_EVENT_SONGSEL =		21;	/* Song Select with song ID number */
	public static final int	SND_SEQ_EVENT_QFRAME =		22;	/* midi time code quarter frame */
	public static final int	SND_SEQ_EVENT_TIMESIGN =	23;	/* SMF Time Signature event */
	public static final int	SND_SEQ_EVENT_KEYSIGN =		24;	/* SMF Key Signature event */
/* 25-29: reserved */
	        
/* 30-39: timer messages
 * event data type = snd_seq_ev_queue_control_t
 */
	public static final int	SND_SEQ_EVENT_START =		30;	/* midi Real Time Start message */
	public static final int	SND_SEQ_EVENT_CONTINUE =	31;	/* midi Real Time Continue message */
	public static final int	SND_SEQ_EVENT_STOP =		32;	/* midi Real Time Stop message */	
	public static final int	SND_SEQ_EVENT_SETPOS_TICK =	33;	/* set tick queue position */
	public static final int	SND_SEQ_EVENT_SETPOS_TIME =	34;	/* set realtime queue position */
	public static final int	SND_SEQ_EVENT_TEMPO =		35;	/* (SMF) Tempo event */
	public static final int	SND_SEQ_EVENT_CLOCK =		36;	/* midi Real Time Clock message */
	public static final int	SND_SEQ_EVENT_TICK =		37;	/* midi Real Time Tick message */
/* 38-39: reserved */

/* 40-49: others
 * event data type = none
 */
	public static final int	SND_SEQ_EVENT_TUNE_REQUEST =	40;	/* tune request */
	public static final int	SND_SEQ_EVENT_RESET =		41;	/* reset to power-on state */
	public static final int	SND_SEQ_EVENT_SENSING =		42;	/* "active sensing" event */
/* 43-49: reserved */

/* 50-59: echo back, kernel private messages
 * event data type = any type
 */
	public static final int	SND_SEQ_EVENT_ECHO =		50;	/* echo event */
	public static final int	SND_SEQ_EVENT_OSS =		51;	/* OSS raw event */
/* 52-59: reserved */

/* 60-69: system status messages (broadcast for subscribers)
 * event data type = snd_seq_addr_t
 */
	public static final int	SND_SEQ_EVENT_CLIENT_START =	60;	/* new client has connected */
	public static final int	SND_SEQ_EVENT_CLIENT_EXIT =	61;	/* client has left the system */
	public static final int	SND_SEQ_EVENT_CLIENT_CHANGE =	62;	/* client status/info has changed */
	public static final int	SND_SEQ_EVENT_PORT_START =	63;	/* new port was created */
	public static final int	SND_SEQ_EVENT_PORT_EXIT =	64;	/* port was deleted from system */
	public static final int	SND_SEQ_EVENT_PORT_CHANGE =	65;	/* port status/info has changed */
	public static final int	SND_SEQ_EVENT_PORT_SUBSCRIBED =	66;	/* read port is subscribed */
	public static final int	SND_SEQ_EVENT_PORT_USED =	67;	/* write port is subscribed */
	public static final int	SND_SEQ_EVENT_PORT_UNSUBSCRIBED =	68;	/* read port is released */
	public static final int	SND_SEQ_EVENT_PORT_UNUSED =	69;	/* write port is released */

/* 70-79: synthesizer events
 * event data type = snd_seq_eve_sample_control_t
 */
	public static final int	SND_SEQ_EVENT_SAMPLE =		70;	/* sample select */
	public static final int	SND_SEQ_EVENT_SAMPLE_CLUSTER =	71;	/* sample cluster select */
	public static final int	SND_SEQ_EVENT_SAMPLE_START =	72;	/* voice start */
	public static final int	SND_SEQ_EVENT_SAMPLE_STOP =	73;	/* voice stop */
	public static final int	SND_SEQ_EVENT_SAMPLE_FREQ =	74;	/* playback frequency */
	public static final int	SND_SEQ_EVENT_SAMPLE_VOLUME =	75;	/* volume and balance */
	public static final int	SND_SEQ_EVENT_SAMPLE_LOOP =	76;	/* sample loop */
	public static final int	SND_SEQ_EVENT_SAMPLE_POSITION =	77;	/* sample position */
	public static final int	SND_SEQ_EVENT_SAMPLE_PRIVATE1 =	78;	/* private (hardware dependent) event */

/* 80-89: reserved */

/* 90-99: user-defined events with fixed length
 * event data type = any
 */
	public static final int	SND_SEQ_EVENT_USR0 =		90;
	public static final int	SND_SEQ_EVENT_USR1 =		91;
	public static final int	SND_SEQ_EVENT_USR2 =		92;
	public static final int	SND_SEQ_EVENT_USR3 =		93;
	public static final int	SND_SEQ_EVENT_USR4 =		94;
	public static final int	SND_SEQ_EVENT_USR5 =		95;
	public static final int	SND_SEQ_EVENT_USR6 =		96;
	public static final int	SND_SEQ_EVENT_USR7 =		97;
	public static final int	SND_SEQ_EVENT_USR8 =		98;
	public static final int	SND_SEQ_EVENT_USR9 =		99;

/* 100-129: instrument layer
 * variable length data can be passed directly to the driver
 */
	public static final int	SND_SEQ_EVENT_INSTR_BEGIN =	100;	/* begin of instrument management */
	public static final int	SND_SEQ_EVENT_INSTR_END =	101;	/* end of instrument management */
	public static final int	SND_SEQ_EVENT_INSTR_INFO =	102;	/* instrument interface info */
	public static final int	SND_SEQ_EVENT_INSTR_INFO_RESULT = 103;	/* result */
	public static final int	SND_SEQ_EVENT_INSTR_FINFO =	104;	/* get format info */
	public static final int	SND_SEQ_EVENT_INSTR_FINFO_RESULT = 105;	/* get format info */
	public static final int	SND_SEQ_EVENT_INSTR_RESET =	106;	/* reset instrument memory */
	public static final int	SND_SEQ_EVENT_INSTR_STATUS =	107;	/* instrument interface status */
	public static final int	SND_SEQ_EVENT_INSTR_STATUS_RESULT = 108;	/* result */
	public static final int	SND_SEQ_EVENT_INSTR_PUT =	109;	/* put instrument to port */
	public static final int	SND_SEQ_EVENT_INSTR_GET =	110;	/* get instrument from port */
	public static final int	SND_SEQ_EVENT_INSTR_GET_RESULT =	111;	/* result */
	public static final int	SND_SEQ_EVENT_INSTR_FREE =	112;	/* free instrument(s) */
	public static final int	SND_SEQ_EVENT_INSTR_LIST =	113;	/* instrument list */
	public static final int	SND_SEQ_EVENT_INSTR_LIST_RESULT = 114;	/* result */
	public static final int	SND_SEQ_EVENT_INSTR_CLUSTER =	115;	/* cluster parameters */
	public static final int	SND_SEQ_EVENT_INSTR_CLUSTER_GET =	116;	/* get cluster parameters */
	public static final int	SND_SEQ_EVENT_INSTR_CLUSTER_RESULT = 117;	/* result */
	public static final int	SND_SEQ_EVENT_INSTR_CHANGE =	118;	/* instrument change */
/* 119-129: reserved */

/* 130-139: variable length events
 * event data type = snd_seq_ev_ext
 * (SND_SEQ_EVENT_LENGTH_VARIABLE must be set)
 */
	public static final int	SND_SEQ_EVENT_SYSEX =		130;	/* system exclusive data (variable length) */
	public static final int	SND_SEQ_EVENT_BOUNCE =		131;	/* error event */
/* 132-134: reserved */
	public static final int	SND_SEQ_EVENT_USR_VAR0 =	135;
	public static final int	SND_SEQ_EVENT_USR_VAR1 =	136;
	public static final int	SND_SEQ_EVENT_USR_VAR2 =	137;
	public static final int	SND_SEQ_EVENT_USR_VAR3 =	138;
	public static final int	SND_SEQ_EVENT_USR_VAR4 =	139;

/* 140-149: IPC shared memory events (*NOT SUPPORTED YET*)
 * event data type = snd_seq_ev_ipcshm
 * (SND_SEQ_EVENT_LENGTH_VARIPC must be set)
 */
	public static final int	SND_SEQ_EVENT_IPCSHM =		140;
/* 141-144: reserved */
	public static final int	SND_SEQ_EVENT_USR_VARIPC0 =	145;
	public static final int	SND_SEQ_EVENT_USR_VARIPC1 =	146;
	public static final int	SND_SEQ_EVENT_USR_VARIPC2 =	147;
	public static final int	SND_SEQ_EVENT_USR_VARIPC3 =	148;
	public static final int	SND_SEQ_EVENT_USR_VARIPC4 =	149;

/* 150-191: reserved */

/* 192-254: hardware specific events */

/* 255: special event */
	public static final int	SND_SEQ_EVENT_NONE =		255;


	public static final int	SND_SEQ_ADDRESS_UNKNOWN	 =	253;	/* unknown source */
	public static final int	SND_SEQ_ADDRESS_SUBSCRIBERS =	254;	/* send event to all subscribed ports */
	public static final int	SND_SEQ_ADDRESS_BROADCAST =	255;	/* send event to all queues/clients/ports/channels */
	public static final int	SND_SEQ_QUEUE_DIRECT =		253;	/* direct dispatch */

	/* event mode flag - NOTE: only 8 bits available! */
	public static final int	SND_SEQ_TIME_STAMP_TICK	 =	(0<<0); /* timestamp in clock ticks */
	public static final int	SND_SEQ_TIME_STAMP_REAL	 =	(1<<0); /* timestamp in real time */
	public static final int	SND_SEQ_TIME_STAMP_MASK	 =	(1<<0);

	public static final int	SND_SEQ_TIME_MODE_ABS =		(0<<1);	/* absolute timestamp */
	public static final int	SND_SEQ_TIME_MODE_REL =		(1<<1);	/* relative to current time */
	public static final int	SND_SEQ_TIME_MODE_MASK =	(1<<1);

	public static final int	SND_SEQ_EVENT_LENGTH_FIXED =	(0<<2);	/* fixed event size */
	public static final int	SND_SEQ_EVENT_LENGTH_VARIABLE =	(1<<2);	/* variable event size */
	public static final int	SND_SEQ_EVENT_LENGTH_VARUSR =	(2<<2);	/* variable event size - user memory space */
	public static final int	SND_SEQ_EVENT_LENGTH_VARIPC =	(3<<2);	/* variable event size - IPC */
	public static final int	SND_SEQ_EVENT_LENGTH_MASK =	(3<<2);

	public static final int	SND_SEQ_PRIORITY_NORMAL	 =	(0<<4);	/* normal priority */
	public static final int	SND_SEQ_PRIORITY_HIGH =		(1<<4);	/* event should be processed before others */
	public static final int	SND_SEQ_PRIORITY_MASK =		(1<<4);





	/* known client numbers */
	public static final int	SND_SEQ_CLIENT_SYSTEM =		0;
	public static final int	SND_SEQ_CLIENT_DUMMY =		62;	/* dummy ports */
	public static final int	SND_SEQ_CLIENT_OSS =		63;	/* oss sequencer emulator */


	/* client types */
	public static final int	NO_CLIENT       = 0;
	public static final int	USER_CLIENT     = 1;
	public static final int	KERNEL_CLIENT   = 2;
                        
	/* event filter flags */
	public static final int	SND_SEQ_FILTER_BROADCAST =	(1<<0);	/* accept broadcast messages */
	public static final int	SND_SEQ_FILTER_MULTICAST =	(1<<1);	/* accept multicast messages */
	public static final int	SND_SEQ_FILTER_BOUNCE =		(1<<2);	/* accept bounce event in error */
	public static final int	SND_SEQ_FILTER_USE_EVENT =	(1<<31);	/* use event filter */


	/* known port numbers */
	public static final int SND_SEQ_PORT_SYSTEM_TIMER =	0;
	public static final int SND_SEQ_PORT_SYSTEM_ANNOUNCE =	1;

	/* port capabilities (32 bits) */
	public static final int	SND_SEQ_PORT_CAP_READ =		(1<<0);	/* readable from this port */
	public static final int	SND_SEQ_PORT_CAP_WRITE =	(1<<1);	/* writable to this port */

	public static final int	SND_SEQ_PORT_CAP_SYNC_READ =	(1<<2);
	public static final int	SND_SEQ_PORT_CAP_SYNC_WRITE =	(1<<3);

	public static final int	SND_SEQ_PORT_CAP_DUPLEX =	(1<<4);

	public static final int	SND_SEQ_PORT_CAP_SUBS_READ =	(1<<5);	/* allow read subscription */
	public static final int	SND_SEQ_PORT_CAP_SUBS_WRITE =	(1<<6);	/* allow write subscription */
	public static final int	SND_SEQ_PORT_CAP_NO_EXPORT =	(1<<7);	/* routing not allowed */

	/* port type */
	public static final int	SND_SEQ_PORT_TYPE_SPECIFIC =	(1<<0);	/* hardware specific */
	public static final int	SND_SEQ_PORT_TYPE_MIDI_GENERIC =(1<<1);	/* generic MIDI device */
	public static final int	SND_SEQ_PORT_TYPE_MIDI_GM =	(1<<2);	/* General MIDI compatible device */
	public static final int	SND_SEQ_PORT_TYPE_MIDI_GS =	(1<<3);	/* GS compatible device */
	public static final int	SND_SEQ_PORT_TYPE_MIDI_XG =	(1<<4);	/* XG compatible device */
	public static final int	SND_SEQ_PORT_TYPE_MIDI_MT32 =	(1<<5);	/* MT-32 compatible device */

/* other standards...*/
	public static final int	SND_SEQ_PORT_TYPE_SYNTH =	(1<<10);	/* Synth device */
	public static final int	SND_SEQ_PORT_TYPE_DIRECT_SAMPLE =(1<<11);	/* Sampling device (support sample download) */
	public static final int	SND_SEQ_PORT_TYPE_SAMPLE =	(1<<12);	/* Sampling device (sample can be downloaded at any time) */
/*...*/
	public static final int	SND_SEQ_PORT_TYPE_APPLICATION =	(1<<20);	/* application (sequencer/editor) */

/* standard group names */
	public static final String SND_SEQ_GROUP_SYSTEM =	"system";
	public static final String SND_SEQ_GROUP_DEVICE =	"device";
	public static final String SND_SEQ_GROUP_APPLICATION =	"application";


	public static final boolean	DEBUG = false;


	static
	{
		if (TDebug.TraceASequencer)
		{
			System.out.println("ASequencer0.<clinit>(): loading native library tritonusalsa");
		}
		System.loadLibrary("tritonusalsa");
	}

	/*
	 *	This holds a pointer for the native code - do not touch!
	 */
	private long		m_lNativeSeq;
	
	/*
	 *	The client Id assigned by the sequencer to this objects
	 *	connection.
	 */
	private int		m_nClientId;



	public ASequencer0()
	{
		if (DEBUG)
		{
			System.out.println("ASequencer0.<init>:");
			Thread.dumpStack();
		}
		m_nClientId = open();
	}



	public int getClientId()
	{
		return m_nClientId;
	}


	/**	Opens the sequencer.
	 *	Calls snd_seq_open() and snd_seq_client_id(). Returns the
	 *	client id.
	 */
	public native int open();

	/**	Closes the sequencer.
	 *	Calls snd_seq_close().
	 */
	public native void close();



	public native int createPort(String strName, int nCapabilities, int nGroupPermissions, int nType, int nMidiChannels, int nMidiVoices, int nSynthVoices);


	public native int allocQueue();


	/*
	 *	nResolution: ticks/beat
	 *	nTempo: microseconds/beat
	 */
	public native void setQueueTempo(int nQueue, int nResolution, int nTempo);


	/**	Get the current tempo of a queue.
	 *	Calls snd_seq_get_queue_tempo()
	 *	and put the data elements of the returned struct
	 *	into the passed arrays.
	 *	anValues[0]	tempo (us/tick)
	 *	anValues[1]	resolution (ticks/quarter)
	 */
	public native void getQueueTempo(int nQueue,
					  int[] anValues);


	/**	Get information about a queue.
	 *	Calls snd_seq_get_queue_status()
	 *	and put the data elements of the returned struct
	 *	into the passed arrays.
	 *	anValues[0]	number of events (= queue size)
	 *	anValues[1]	running flag
	 *	anValues[2]	flags
	 *
	 *	alValues[0]	tick
	 *	alValues[1]	time
	 */
	public native void getQueueStatus(int nQueue,
					  int[] anValues,
					  long[] alValues);


	public native void subscribePort(
		int nSenderClient, int nSenderPort,
		int nDestClient, int nDestPort,
		int nQueue, boolean bExclusive, boolean bRealtime, boolean bConvertTime,
		int nMidiChannels, int nMidiVoices, int nSynthVoices);


	public native void sendNoteEvent(
		int nType, int nFlags, int nTag, int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nNote, int nVelocity, int nOffVelocity, int nDuration);


	public native void sendControlEvent(
		int nType, int nFlags, int nTag, int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nChannel, int nParam, int nValue);


	public native void sendQueueControlEvent(
		int nType, int nFlags, int nTag, int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nControlQueue, int nControlValue, long lControlTime);


	/**
	 *	Encapsulates a Java object reference in an ALSA event.
	 *	As type, use SND_SEQ_EVENT_USR9. This one is detected by
	 *	getEvent() and the object reference is returned in the
	 *	passed object array.
	 */
	public native void sendObjectEvent(
		int nType, int nFlags, int nTag, int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		Object objectReference);


	/**
	 *	Transmits arbitrary data in an ALSA event.
	 *	In getEvent(), a reference to a byte array is returned
	 *	in the passed object array.
	 */
	public native void sendVarEvent(
		int nType, int nFlags, int nTag, int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		byte[] abData, int nOffset, int nLength);


	/**	Wait for an event.
	 *	Calls snd_seq_event_input().
	 *	and put the data elements of the returned event
	 *	into the passed arrays.
	 *	anValues[0]	type
	 *	anValues[1]	flags
	 *	anValues[2]	tag
	 *
	 *	anValues[3]	queue
	 *
	 *	anValues[4]	source client
	 *	anValues[5]	source port
	 *
	 *	anValues[6]	dest client
	 *	anValues[7]	dest port
	 *
	 *	The values starting with index 8 depend on the type of event.
	 *
	 *	SND_SEQ_EVENT_NOTE,
	 *	SND_SEQ_EVENT_NOTEON,
	 *	SND_SEQ_EVENT_NOTEOFF:
	 *	anValues[08]	channel
	 *	anValues[09]	note
	 *	anValues[10]	velocity
	 *	anValues[11]	off_velocity
	 *	anValues[12]	duration
	 *
	 *	SND_SEQ_EVENT_KEYPRESS,
	 *	SND_SEQ_EVENT_CONTROLLER,
	 *	SND_SEQ_EVENT_PGMCHANGE,
	 *	SND_SEQ_EVENT_CHANPRESS,
	 *	SND_SEQ_EVENT_PITCHBEND,
	 *	SND_SEQ_EVENT_CONTROL14, ??
	 *	SND_SEQ_EVENT_NONREGPARAM, ??
	 *	SND_SEQ_EVENT_REGPARAM: ??
	 *	anValues[08]	channel
	 *	anValues[09]	param
	 *	anValues[10]	values
	 *
	 *	SND_SEQ_EVENT_USR9:
	 *	aValues[0]	object reference
	 *
	 *	alValues[0]	(schedule) time (in ticks or nanoseconds)
	 *
	 *
	 *	returns true if an event was received. Above values are
	 *	only valid if this is true!
	 *
	 *	Throws RuntimeExceptions in certain cases.
	 */
	public native boolean getEvent(int[] anValues, long[] alValues, Object[] aValues);

	/**	Gets "system" information.
	 *	Calls snd_seq_system_info() and puts the relevant values into
	 *	the passed array.
	 */
	public native void getSystemInfo(int[] anValues);

	/**	Gets information about this client.
	 *	Calls snd_seq_get_client_info() [nClient <= -1]
	 *	or snd_seq_get_any_client_info() [nClient >= 0]
	 *	and put the returned values
	 *	into the passed arrays.
	 *	anValues[0]	client id
	 *	anValues[1]	client type
	 *	anValues[2]	filter flags
	 *	anValues[3]	num ports
	 *	astrValues[0]	name
	 *	astrValues[1]	group name
	 *
	 *	Returns 0 if successful.
	 */
	public native int getClientInfo(int nClient, int[] anValues, String[] astrValues);



	/**	Gets information about the next client.
	 *	Calls snd_seq_query_next_client().
	 *	and put the returned values
	 *	into the passed arrays.
	 *
	 *	nClient has to be -1 to start, or a client id returned by
	 *	a previous call to this method.
	 *
	 *	anValues[0]	client id
	 *	anValues[1]	client type
	 *	anValues[2]	filter flags
	 *	anValues[3]	num ports
	 *	astrValues[0]	name
	 *	astrValues[1]	group name
	 *
	 *	Returns 0 if successful.
	 */
	public native int getNextClientInfo(int nClient, int[] anValues, String[] astrValues);



	/**	Gets information about the next port.
	 *	Calls snd_seq_query_next_port().
	 *	and put the returned values
	 *	into the passed arrays.
	 *
	 *	nClient has to be a valid client.
	 *	nPort has to be -1 to start, or a port returned by
	 *	a previous call to this method.
	 *
	 *	anValues[0]	client
	 *	anValues[1]	port
	 *	anValues[2]	capabilities
	 *	anValues[3]	group capabilities
	 *	anValues[4]	type
	 *	anValues[5]	midi channels
	 *	anValues[6]	midi voices
	 *	anValues[7]	synth voices
	 *	anValues[8]	read use
	 *	anValues[9]	write use
	 *	astrValues[0]	name
	 *	astrValues[1]	group name
	 *
	 *	Returns 0 if successful.
	 */
	public native int getNextPortInfo(int nClient, int nPort, int[] anValues, String[] astrValues);


	public native void setQueueLocked(int nQueue, boolean bLocked);


	public native void setClientName(String strName);
}



/*** ASequencer0.java ***/
