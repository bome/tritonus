/*
 *	HandleFieldHandler.hh
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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

#ifndef _HANDLE_FIELD_HANDLER_HH
#define _HANDLE_FIELD_HANDLER_HH

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

#endif /* _HANDLE_FIELD_HANDLER_HH */


/*** HandleFieldHandler.hh ***/
