/*
 *	HandleFieldHandler.cc
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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
 */


#include	"HandleFieldHandler.hh"
#include	"common.h"
#include	<alsa/asoundlib.h>




template<typename _handle_type>
HandleFieldHandler<_handle_type>::
HandleFieldHandler(string strHandleFieldName)
{
	m_strHandleFieldName = strHandleFieldName;
	m_nFieldID = NULL;
}



template<typename _handle_type>
jfieldID
HandleFieldHandler<_handle_type>::
getNativeHandleFieldID(JNIEnv *env, jobject obj)
{
	if (m_nFieldID == NULL)
	{
		jclass	cls = env->GetObjectClass(obj);
		if (cls == NULL)
		{
			throwRuntimeException(env, "cannot get class");
		}
		m_nFieldID = env->GetFieldID(cls, "m_lNativeHandle", "J");
		if (m_nFieldID == NULL)
		{
			throwRuntimeException(env, "cannot get field ID for m_lNativeHandle");
		}
	}
	return m_nFieldID;
}



template<typename _handle_type>
void
HandleFieldHandler<_handle_type>::
setHandle(JNIEnv *env, jobject obj, _handle_type handle)
{
	jfieldID	fieldID = getNativeHandleFieldID(env, obj);
	env->SetLongField(obj, fieldID, (jlong) handle);
}



template<typename _handle_type>
_handle_type
HandleFieldHandler<_handle_type>::
getHandle(JNIEnv *env, jobject obj)
{
	jfieldID	fieldID = getNativeHandleFieldID(env, obj);
	_handle_type	handle = (_handle_type) env->GetLongField(obj, fieldID);
	return handle;
}



template<typename _handle_type>
void
HandleFieldHandler<_handle_type>::
setDebug(bool bDebug)
{
	m_bDebug = bDebug;
}



template<typename _handle_type>
bool
HandleFieldHandler<_handle_type>::
getDebug()
{
	return m_bDebug;
}




template class HandleFieldHandler<snd_ctl_t*>;
template class HandleFieldHandler<snd_mixer_t*>;
template class HandleFieldHandler<snd_mixer_elem_t*>;
template class HandleFieldHandler<snd_pcm_t*>;
template class HandleFieldHandler<snd_pcm_hw_params_t*>;
template class HandleFieldHandler<snd_pcm_format_mask_t*>;
template class HandleFieldHandler<snd_pcm_sw_params_t*>;
template class HandleFieldHandler<snd_seq_t*>;
template class HandleFieldHandler<snd_seq_client_info_t*>;
template class HandleFieldHandler<snd_seq_event_t*>;
template class HandleFieldHandler<snd_seq_port_info_t*>;
template class HandleFieldHandler<snd_seq_port_subscribe_t*>;
template class HandleFieldHandler<snd_seq_queue_info_t*>;
template class HandleFieldHandler<snd_seq_queue_status_t*>;
template class HandleFieldHandler<snd_seq_queue_tempo_t*>;
template class HandleFieldHandler<snd_seq_queue_timer_t*>;
template class HandleFieldHandler<snd_seq_remove_events_t*>;
template class HandleFieldHandler<snd_seq_system_info_t*>;



/*** HandleFieldHandler.cc ***/
