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
	char*		m_pcBuffer;

public:
	jstreambuf(JNIEnv* env, jobject obj, int nBufferSize = 4096);
	virtual ~jstreambuf();
	virtual int sync();
	virtual int overflow(int ch);
//	virtual streamsize xsputn(const char* text, streamsize n);
//	int underflow();
//	int uflow();
	virtual streamsize xsgetn(char* text, streamsize n);
private:
	int flushBuffer();
	streamsize doWrite(const char* text, streamsize n);
};



/*** jstreambuf.hpp ***/
