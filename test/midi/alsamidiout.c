/*
 *	alsamidiout.c
 */

#include	<errno.h>
#include	<stdio.h>
#include	<sys/asoundlib.h>



static void sendEvent(snd_seq_t* seq, snd_seq_event_t* pEvent);


static snd_seq_t* seq;
static int	nClientId;
static int	nPort;
static int	nDestClient = 64;
static int	nDestPort = 0;



int
main(int argc, char** argv)
{
	int	nReturn;
	snd_seq_port_info_t	portInfo;
	const char*		name = "alsamidiout";
	snd_seq_event_t	event;


	printf("alsamidiout 0.1.0\n");
	printf("Copyright (c) 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>\n");
	printf("This program is free software. You are allowed to use, modify and redistribute it under the term of the GNU General Public License.\n");
	printf("-\n");

	nReturn = snd_seq_open(&seq, SND_SEQ_OPEN);
	if (nReturn < 0)
	{
		printf("snd_seq_open failed.");
		exit(1);
	}
	snd_seq_block_mode(seq, 1);
	nClientId = snd_seq_client_id(seq);
	if (nClientId < 0)
	{
		printf("snd_seq_client_id failed.");
		exit(1);
	}

	memset(&portInfo, 0, sizeof(portInfo));
	strncpy(portInfo.name, name, 63);
	portInfo.name[64] = 0;
	portInfo.capability = SND_SEQ_PORT_CAP_WRITE | SND_SEQ_PORT_CAP_SUBS_WRITE | SND_SEQ_PORT_CAP_READ;
	// portInfo.capability = nCapabilities;
	portInfo.cap_group = 0;	// nGroupPermissions;
	// portInfo.type =  SND_SEQ_PORT_TYPE_MIDI_GENERIC;
/*
  portInfo.type =  nType;
  portInfo.midi_channels = nMidiChannels;
  portInfo.midi_voices = nMidiVoices;
  portInfo.synth_voices = nSynthVoices;
*/
	//portInfo.write_use = 1;	// R/O attrs?
	//portInfo.read_use = 1;

	// errno = 0;
	nReturn = snd_seq_create_port(seq, &portInfo);
	if (nReturn < 0)
	{
		printf("snd_seq_create_port failed.");
		exit(1);
	}
	nPort = portInfo.port;
	printf("own port: %d:%d\n", nClientId, nPort);
	// printf("argc: %d\n", argc);

	// exit(0);
	if (argc == 3)
	{
		nDestClient = atoi(argv[1]);
		nDestPort = atoi(argv[2]);
	}
	printf("destination port: %d:%d\n", nDestClient, nDestPort);

	////////////////////////////////

	memset(&event, 0, sizeof(event));

	event.type = 6;
	event.flags = 3;
	event.tag = 0;
	event.queue = 253;
	event.time.time.tv_sec = 0;
	event.time.time.tv_nsec = 0;


	// is set by the sequencer to sending client
	//event.source.client = nSourceClient;
	event.source.port = nPort;
	event.dest.client = nDestClient;
	event.dest.port = nDestPort;

	event.data.note.channel = 0;
	event.data.note.note = 34;
	event.data.note.velocity = 104;
	event.data.note.off_velocity = 0;
	event.data.note.duration = 0;

	sendEvent(seq, &event);

}


static void
sendEvent(snd_seq_t*		seq,
	  snd_seq_event_t*	pEvent)
{
	int	nReturn = 0;

	nReturn = snd_seq_event_output(seq, pEvent);
	// IDEA: execute flush only if event wants to circumvent queues?
	if (nReturn < 0)
	{
		// throwRuntimeException(env, "snd_seq_event_output failed");
	}
	// we have to restart this call in case it is interrupted by a signal.
	do
	{
		errno = 0;
		printf("before flushing output\n");
		nReturn = snd_seq_flush_output(seq);
		printf("after flushing output\n");
	}
	while (nReturn == -1 && errno == EINTR);
	if (nReturn < 0)
	{
		printf("return: %d\n", nReturn);
		printf("errno: %d\n", errno);
		perror("abc");
		// throwRuntimeException(env, "snd_seq_flush_output failed");
	}
}








/*** alsamidiout.c ***/
