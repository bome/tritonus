/*
 *	HandleFieldHandler.hh
 */

#include	<string>
#include	<jni.h>


template<class _handle_type>
class HandleFieldHandler
{
private:
	string		m_strHandleFieldName;
	jfieldID	m_nFieldID;
	bool		m_bDebug;

public:
	HandleFieldHandler(string strHandleFieldName = "m_lNativeHandle");


private:	jfieldID getNativeHandleFieldID(JNIEnv *env, jobject obj);

public:
	void		setHandle(JNIEnv *env, jobject obj, _handle_type handle);
	_handle_type	getHandle(JNIEnv *env, jobject obj);

	void		setDebug(bool bDebug);
	bool		getDebug();
};



/*** HandleFieldHandler.hh ***/
