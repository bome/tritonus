/*
 *	Saint.java
 */

/*
#include	<iostream>
#include	<fstream>
#include	<string>
#include	<cstdio>
#include	"Saint.hpp"

using	std::string;

// HACK! should be part of a header file
extern "C" int error_count;

extern "C" void init_scanner(char *name);
extern "C" void get_token(void);
extern "C" void orchestra(void);
extern "C" void pre_scan(void);
extern "C" void free_orchestra(void);
*/

package	org.tritonus.lowlevel.saint;


import	java.io.InputStream;
import	java.io.OutputStream;



public class Saint
{
	static
	{
		System.loadLibrary("tritonussaint");
	}


	private long	m_lNativeHandle;



	public Saint(InputStream bitStream,
		     OutputStream outputStream,
		     int nOutputFormat)
	{
		init(bitStream,
		     outputStream,
		     nOutputFormat);
	}



	public Saint(InputStream orchestraStream,
		     InputStream scoreStream,
		     OutputStream outputStream,
		     int nOutputFormat)
	{
		init(orchestraStream,
		     scoreStream,
		     outputStream,
		     nOutputFormat);
	}


	private native void init(
		InputStream bitStream,
		OutputStream outputStream,
		int nOutputFormat);

	private native void init(
		InputStream orchestraStream,
		InputStream scoreStream,
		OutputStream outputStream,
		int nOutputFormat);


	// TODO: finalizer??
/*
Saint::~Saint()
{
	copyFromTempfile(m_strOutputTempFilename,
			 m_pOutputStream);
	if (m_pScheduler)
	{
		// with saint 1.33 (prerelease), a segfault occures if the destructor is called
		// delete m_pScheduler;
	}
	// TODO: remove temporary files
}
*/



	public native int getSamplingRate();



	public native int getChannelCount();


	public native void run();
}


/*** Saint.cpp ***/
