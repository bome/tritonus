/*
 *	jstreambuf.cpp
 */

#include	<iostream>
#include	"jstreambuf.hpp"



jstreambuf::jstreambuf(JNIEnv* env, jobject obj)
{
	cerr << "jstreambuf: constructor\n";
	m_pJNIEnv = env;
	m_object = m_pJNIEnv->NewGlobalRef(obj);
	cerr << "global ref: " << m_object << endl;
	m_class = m_pJNIEnv->GetObjectClass(m_object);
	cerr << "class: " << m_class << endl;
	jclass	inputStreamClass = env->FindClass("java/io/InputStream");
	if (inputStreamClass == NULL)
	{
		cerr << "cannot get clas id for InputStream" << endl;
		// throwRuntimeException(env, "cannot get class object for org.tritonus.lowlevel.saint.Saint");
	}
	jclass	outputStreamClass = env->FindClass("java/io/OutputStream");
	if (outputStreamClass == NULL)
	{
		cerr << "cannot get clas id for InputStream" << endl;
		// throwRuntimeException(env, "cannot get class object for org.tritonus.lowlevel.saint.Saint");
	}
	if (m_pJNIEnv->IsInstanceOf(m_object, outputStreamClass) == JNI_TRUE)
	{
	m_writeMethodID = m_pJNIEnv->GetMethodID(m_class, "write", "([BII)V");
	cerr << "write method: " << m_writeMethodID << endl;
	}
	if (m_pJNIEnv->IsInstanceOf(m_object, inputStreamClass) == JNI_TRUE)
	{
	m_readMethodID = m_pJNIEnv->GetMethodID(m_class, "read", "([BII)I");
	cerr << "read method: " << m_writeMethodID << endl;
	}
}



jstreambuf::~jstreambuf()
{
	m_pJNIEnv->DeleteGlobalRef(m_object);
}



int
jstreambuf::sync()
{
	cerr << "jstreambuf: sync()\n";
	cerr << "jstreambuf: pbase: " << pbase() << "\n";
	cerr << "jstreambuf: pptr: " << pptr() << "\n";
	return 0;
}



int
jstreambuf::overflow(int ch)
{
	cerr << "jstreambuf: overflow()\n";
	return 0;
}



streamsize
jstreambuf::xsputn(const char* text, streamsize n)
{
	// cerr << "jstreambuf: xsputn(.., " << n << ") for " << m_object << "\n";
	jbyteArray	byteArray = m_pJNIEnv->NewByteArray(n);
	if (byteArray == NULL)
	{
		cerr << "could not allocate byte array!!!" << endl;
	}
	m_pJNIEnv->SetByteArrayRegion(byteArray, 0, n, (jbyte*) text);
	m_pJNIEnv->CallVoidMethod(m_object, m_writeMethodID, byteArray, 0, n);
	// TODO: check for exceptions; return error values
	return n;
}



/*
int
jstreambuf::underflow()
{
	cerr << "jstreambuf: underflow()\n";
	return EOF;
}


int
jstreambuf::uflow()
{
	cerr << "jstreambuf: uflow()\n";
	return EOF;
}
*/

streamsize
jstreambuf::xsgetn(char* text, streamsize n)
{
	cerr << "jstreambuf: xsgetn(.., " << n << ") for " << m_object << endl;
	jbyteArray	byteArray = m_pJNIEnv->NewByteArray(n);
	if (byteArray == NULL)
	{
		cerr << "byte array not allocated!!" << endl;
	}
	int	nBytesRead = m_pJNIEnv->CallIntMethod(m_object, m_readMethodID, byteArray, 0, n);
	cerr << "nBytesRead: " << nBytesRead << endl;
	// TODO: check for exceptions; return error values
	if (nBytesRead == -1)
	{
		return 0;
	}
	m_pJNIEnv->GetByteArrayRegion(byteArray, 0, n, (jbyte*) text);
	return nBytesRead;
}




// only for testing
int
main()
{
	jstreambuf* jsb = NULL;	// new jstreambuf();
	ostream jstr(jsb);
	jstr << "Hello world\n";
	long* p = new long[25];
	jstr.write(p, 25 * 4);
	jstr << "Hello world\n";
	// jstr.flush();
	// delete jsb;
	jstreambuf* jsb2 = NULL;	// new jstreambuf();
	istream jstr2(jsb2);
	jstr2.read(p, 25 * 4);

	int	i;
	jstr2 >> i;
}



/*** jstreambuf.cpp ***/
