/*
 *	jstreambuf.hpp
 */

#include	<iostream>
#include	<jni.h>


class jstreambuf : public streambuf
{
private:
	JNIEnv*		m_pJNIEnv;
	jobject		m_object;

	jclass		m_class;
	jmethodID	m_writeMethodID;
	jmethodID	m_readMethodID;

public:
	jstreambuf(JNIEnv* env, jobject obj);
	virtual ~jstreambuf();
	int sync();
	int overflow(int ch);
	virtual streamsize xsputn(const char* text, streamsize n);
//	int underflow();
//	int uflow();
	virtual streamsize xsgetn(char* text, streamsize n);
};



/*** jstreambuf.hpp ***/
