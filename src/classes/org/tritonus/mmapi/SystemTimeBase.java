/*
 *	SystemTimeBase.java
 */

package	org.tritonus.mmapi;


import	javax.microedition.media.TimeBase;



public class SystemTimeBase
implements TimeBase
{
	public long getTime()
	{
		return System.currentTimeMillis() * 1000;
	}
}



/*** SystemTimeBase.java ***/
