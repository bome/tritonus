/*
 *	ContentDescriptor.java
 */

package	javax.microedition.media.protocol;



public class ContentDescriptor
{
	private String		m_strContentType = null;



	public ContentDescriptor(String strContentType)
	{
		m_strContentType = strContentType;
	}



	public String getContentType()
	{
		return m_strContentType;
	}
}



/*** ContentDescriptor.java ***/
