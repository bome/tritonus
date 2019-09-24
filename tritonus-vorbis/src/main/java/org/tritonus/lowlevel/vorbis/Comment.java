/*
 *	Comment.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 - 2001 by Matthias Pfisterer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.lowlevel.vorbis;

import org.tritonus.lowlevel.ogg.Ogg;
import org.tritonus.share.TDebug;


/** Wrapper for vorbis_info.
 */
public class Comment
{
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
	@SuppressWarnings("unused")
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
	public native void init();


	/** Calls vorbis_comment_add().
	 */
	public native void addComment(String strComment);


	/** Calls vorbis_comment_add_tag().
	 */
	public native void addTag(String strTag, String strComment);



	/** Calls vorbis_comment_query_count().
	 */
	public native int queryCount(String strTag);


	/** Calls vorbis_comment_query().
	 */
	public native String query(String strTag, int nIndex);


	/** Accesses user_comments, comment_lengths and comments.
	 */
	public native String[] getUserComments();



	/** Accesses vendor.
	 */
	public native String getVendor();



	/** Calls vorbis_comment_clear().
	 */
	public native void clear();


// 	/** Calls vorbis_commentheader_out().
// 	 */
// 	public native void headerOut(Packet packet);

	private static native void setTrace(boolean bTrace);
}





/*** Comment.java ***/
