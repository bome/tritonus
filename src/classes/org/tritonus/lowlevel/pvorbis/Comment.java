/*
 *	Comment.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 - 2005 by Matthias Pfisterer
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.lowlevel.pvorbis;

import java.util.ArrayList;
import java.util.List;

import org.tritonus.lowlevel.pogg.Ogg;
import org.tritonus.share.TDebug;


/** Wrapper for vorbis_comment.
 */
public class Comment
{

	private String m_vendor;
	private List<String> m_comments;

	static
	{
		Ogg.loadNativeLibrary();
		if (TDebug.TraceVorbisNative)
		{
			setTrace(true);
		}
	}


	/**
	 *	Holds the pointer to vorbis_info
	 *	for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	private long	m_lNativeHandle;



	public Comment()
	{
		if (TDebug.TraceVorbisNative) { TDebug.out("Comment.<init>(): begin"); }
		int	nReturn = malloc();
		if (nReturn < 0)
		{
			throw new RuntimeException("malloc of vorbis_comment failed");
		}
		if (TDebug.TraceVorbisNative) { TDebug.out("Comment.<init>(): end"); }
	}



	public void finalize()
	{
		// TODO: call free()
		// call super.finalize() first or last?
		// and introduce a flag if free() has already been called?
	}



	private native int malloc();
	public native void free();


	/** Calls vorbis_comment_init().
	 */
	public void init()
	{
		init_native();
	}
/*
{
m_vendor = null;
m_comments = new ArrayList<String>();
}
 */


	/** Calls vorbis_comment_init().
	 */
	public native void init_native();


	/** Calls vorbis_comment_add().
	 */
	public void addComment(String strComment)
	{
		addComment_native(strComment);
	}
/*
{
m_comments.add(strComment);
}
 */

	/** Calls vorbis_comment_add().
	 */
public native void addComment_native(String strComment);


/** Calls vorbis_comment_add_tag().
 */
public void addTag(String strTag, String strContents)
{
	addTag_native(strTag, strContents);
}

/*
{
addComment(strTag + "=" + strContents);
}
 */

	/** Calls vorbis_comment_add_tag().
	 */
	public native void addTag_native(String strTag, String strContents);


	/** Calls vorbis_comment_query_count().
	 */
	public int queryCount(String strTag)
{
	return queryCount_native(strTag);
}
/*
{
String strFullTag = strTag + "=";
int nCount = 0;
for (int i = 0; i < m_comments.size(); i++)
{
	if (m_comments.get(i).startsWith(strFullTag))
{
nCount++;
}
}
return nCount;
}
 */


	/** Calls vorbis_comment_query_count().
	 */
	public native int queryCount_native(String strTag);


	/** Calls vorbis_comment_query().
	 */
	public String query(String strTag, int nIndex)
	{
		return query_native(strTag, nIndex);
	}


/*
{
String strFullTag = strTag + "=";
int nCount = 0;
for (int i = 0; i < m_comments.size(); i++)
{
	if (i == nIndex)
{
	return m_comments.get(i).substr(strTag.length() + 1);
}
nCount++;
}
return null;
}
 */


	/** Calls vorbis_comment_query().
	 */
	public native String query_native(String strTag, int nIndex);


	/** Accesses user_comments, comment_lengths and comments.
	 */
	public String[] getUserComments()
	{
		return getUserComments_native();
	}


	/** Accesses user_comments, comment_lengths and comments.
	 */
	public native String[] getUserComments_native();



	/** Accesses vendor.
	 */
	public String getVendor()
	{
		return getVendor_native();
	}

/*
  {
return m_vendor;
}
 */

	/** Accesses vendor.
	 */
	public native String getVendor_native();


	/** Calls vorbis_comment_clear().
	 */
	public void clear()
	{
		clear_native();
	}

/*
{
m_comments.clear();
m_vendor = null;
}
 */

	/** Calls vorbis_comment_clear().
	 */
	public native void clear_native();

	private static native void setTrace(boolean bTrace);
}





/*** Comment.java ***/
