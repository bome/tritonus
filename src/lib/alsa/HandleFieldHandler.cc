/*
 *	HandleFieldHandler.cc
 */

#include	"HandleFieldHandler.hh"
#include	"common.h"
#include	<sys/asoundlib.h>




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




template class HandleFieldHandler<snd_seq_t*>;
template class HandleFieldHandler<snd_ctl_t*>;
template class HandleFieldHandler<snd_mixer_t*>;
template class HandleFieldHandler<snd_pcm_t*>;
template class HandleFieldHandler<snd_pcm_hw_params_t*>;
template class HandleFieldHandler<snd_pcm_format_mask_t*>;
template class HandleFieldHandler<snd_pcm_sw_params_t*>;



/*** HandleFieldHandler.cc ***/
