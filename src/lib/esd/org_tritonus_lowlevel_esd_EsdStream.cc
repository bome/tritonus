/* 
 *	org_tritonus_lowlevel_esd_EsdStream.cc
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


#include	<assert.h>
#include	<errno.h>
#include	<stdio.h>
#include	<unistd.h>
#include	<esd.h>
#include	"../common/HandleFieldHandler.hh"
#include	"org_tritonus_lowlevel_esd_EsdStream.h"


static int
get_player_id(const char* name);

static int	DEBUG = 0;

static HandleFieldHandler<int>	fd_handler;
static HandleFieldHandler<int>	playerid_handler;



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_esd_EsdStream_close
(JNIEnv *env, jobject obj)
{
	int		nFd = fd_handler.getHandle(env, obj);
	close(nFd);
	fd_handler.setHandle(env, obj, -1);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    open
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_esd_EsdStream_open
(JNIEnv *env, jobject obj, jint nFormat, jint nSampleRate)
{
	// cerr << "Java_org_tritonus_lowlevel_esd_EsdStream_write: Format: " << nFormat << "\n";
	// cerr << "Java_org_tritonus_lowlevel_esd_EsdStream_write: Sample Rate: " << nSampleRate << "\n";
	int		nFd;
	int		nPlayerId;
	char		name[20];
	sprintf(name, "trit%d", (int) obj);	// DANGEROUS!!
	// printf("name: %s\n", name);
	errno = 0;
	//perror("abc");
	nFd = esd_play_stream/*_fallback*/(nFormat, nSampleRate, NULL, name);
	if (nFd < 0)
	{
		// TODO: error message
		printf("Java_org_tritonus_lowlevel_esd_EsdStream_open: cannot create esd stream\n");
		perror("abc");
		// jclass	cls = env->FindClass("org/tritonus_lowlevel/nas/DeviceAttributes");
		// env->ThrowNew(cls, "exc: cannot create esd stream");
	}
	// printf("fd: %d\n", nFd);
	//perror("abc");
	fd_handler.setHandle(env, obj, nFd);
	nPlayerId = get_player_id(name);
	if (nPlayerId < 0)
	{
		// TODO: error message
	}
	playerid_handler.setHandle(env, obj, nPlayerId);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    setVolume
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_esd_EsdStream_setVolume
(JNIEnv *env, jobject obj, jint nLeft, jint nRight)
{
	int	esd = esd_open_sound(NULL);
	// int		nFd = fd_handler.getHandle(env, obj);
	int		nPlayerId = playerid_handler.getHandle(env, obj);
	esd_set_stream_pan(esd, nPlayerId, nLeft, nRight);
	close(esd);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    write
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_esd_EsdStream_write
(JNIEnv *env, jobject obj, jbyteArray abData, jint nOffset, jint nLength)
{
	int		nFd = fd_handler.getHandle(env, obj);
	signed char*	data = env->GetByteArrayElements(abData, NULL);
	int		nWritten = write(nFd, data + nOffset, nLength);
	env->ReleaseByteArrayElements(abData, data, JNI_ABORT);
	if (DEBUG)
	{
		printf("Java_org_tritonus_lowlevel_esd_EsdStream_write: Length: %d\n", (int) nLength);
		printf("Java_org_tritonus_lowlevel_esd_EsdStream_write: Written: %d\n", nWritten);
	}
	return nWritten;
}



static int
get_player_id(const char* name)
{
	int			esd;
	int			player = -1;
	esd_info_t*		all_info;
	esd_player_info_t*	player_info;

	esd = esd_open_sound(NULL);
	if (esd < 0)
	{
		return -1;
	}
    
	all_info = esd_get_all_info(esd);
	if (all_info)
	{
		for (player_info = all_info->player_list;
		     player_info;
		     player_info = player_info->next)
		{
			if (!strcmp(player_info->name, name))
			{
				player = player_info->source_id;
				break;
			}
		}

		esd_free_all_info (all_info);
	}
	close(esd);
	return player;
}



/*** org_tritonus_lowlevel_esd_EsdStream.cc ***/
