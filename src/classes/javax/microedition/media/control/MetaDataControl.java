/*
 *	MetaDataControl.java
 */

package	javax.microedition.media.control;

import javax.microedition.media.Control;


/**	TODO:
*/
public interface MetaDataControl
extends Control
{
	/**	TODO:
	*/
	public static final String		AUTHOR_KEY = "author";


	/**	TODO:
	*/
	public static final String		COPYRIGHT_KEY = "copyright";


	/**	TODO:
	*/
	public static final String		DATE_KEY = "date";


	/**	TODO:
	*/
	public static final String		TITLE_KEY = "title";


	/**	TODO:
	*/
	public String[] getKeys();


	/**	TODO:
	*/
	public String getKeyValue(String strKey);
}



/*** MetaDataControl.java ***/
