/*
 *	BaseTestCase.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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


import	java.io.File;
import	java.io.FileInputStream;
import	java.io.InputStream;

import	java.net.URL;

import	java.util.MissingResourceException;
import	java.util.ResourceBundle;

import	javax.ejb.FinderException;
import	javax.ejb.CreateException;
import	javax.naming.Context;
import	javax.naming.InitialContext;
import	javax.naming.NamingException;
import	javax.rmi.PortableRemoteObject;

import	junit.framework.TestCase;

import	org.jsresources.apps.esemiso.ejb.interfaces.InstrumentRepository;
import	org.jsresources.apps.esemiso.ejb.interfaces.InstrumentRepositoryHome;



public class BaseTestCase
extends TestCase
{
	protected static final Long	ZERO_LONG = new Long(0);
	protected static final String	TEST_NAME = "inserted by test";

	private InstrumentRepository	m_instrumentRepository;



	public BaseTestCase(String strName)
	{
		super(strName);
	}



	protected void setUp()
		throws Exception
	{
		Context	jndiContext = new InitialContext();
		Object	ref = jndiContext.lookup("esemiso/InstrumentRepository");
		InstrumentRepositoryHome	instrumentRepositoryHome =
			(InstrumentRepositoryHome)
			PortableRemoteObject.narrow(ref, InstrumentRepositoryHome.class);
		m_instrumentRepository = instrumentRepositoryHome.create();
	}



	protected void tearDown()
		throws Exception
	{
		m_instrumentRepository.remove();
		m_instrumentRepository = null;
	}



	protected InstrumentRepository getInstrumentRepository()
	{
		return m_instrumentRepository;
	}
}



/*** BaseTestCase.java ***/
