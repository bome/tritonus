/*
 *	Compiler.java
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

package	org.tritonus.saol.compiler;


import	java.io.File;
import	java.io.Reader;
import	java.io.FileReader;
import	java.io.BufferedReader;
import	java.io.PushbackReader;
import	java.io.IOException;

import	java.util.HashMap;
import	java.util.Iterator;
import	java.util.Map;

import	org.tritonus.share.TDebug;
import	org.tritonus.saol.sablecc.parser.*;
import	org.tritonus.saol.sablecc.lexer.*;
import	org.tritonus.saol.sablecc.node.*;



public class Compiler
{
	private static final boolean	DEBUG = false;

	private static final int	ACTION_DUMP_TREE = 0;
	private static final int	ACTION_COMPILE_INSTRUMENTS = 1;
	private static final int	ACTION_GENERATE_MP4 = 2;


	private File		m_saolFile;
	private int		m_nAction;
	private Map		m_instrumentMap;


	public Compiler(File saolFile)
	{
		this(saolFile, ACTION_COMPILE_INSTRUMENTS);
	}


	public Compiler(File saolFile, int nAction)
	{
		m_saolFile = saolFile;
		m_nAction = nAction;
	}



	public void compile()
		throws Exception
	{
		Reader	reader = new FileReader(m_saolFile);
		reader = new BufferedReader(reader);
		PushbackReader	pbReader = new PushbackReader(reader, 1024);
		Lexer	lexer = new Lexer(pbReader);
		Parser	parser = new Parser(lexer);
		Start	tree = parser.parse();

		switch (m_nAction)
		{
		case ACTION_DUMP_TREE:
			dumpTree(tree);
			break;

		case ACTION_COMPILE_INSTRUMENTS:
			TDebug.out("compiling instruments...");
			m_instrumentMap = compileInstruments(tree);
			TDebug.out("IM: " + m_instrumentMap);
			break;
		}
	}




	/**	Prints the tree of the given AST on standard output.
	 */
	private static void dumpTree(Start tree)
	{
		PrintWalker	printWalker = new PrintWalker();
		tree.apply(printWalker);
	}


	/**
	   Returns a Map: instrument names (String) -> instrument classes (Class)
	 */
	private static Map compileInstruments(Start tree)
	{
		Map		instrumentMap = new HashMap();
		SAOLGlobals	saolGlobals = new SAOLGlobals();
		GlobalsSearcher	gsearcher = new GlobalsSearcher(saolGlobals);
		tree.apply(gsearcher);
		if (DEBUG)
		{
			TDebug.out("a-rate: " + saolGlobals.getARate());
			TDebug.out("k-rate: " + saolGlobals.getKRate());
			TDebug.out("inchannels: " + saolGlobals.getInChannels());
			TDebug.out("outchannels: " + saolGlobals.getOutChannels());
			TDebug.out("interp: " + saolGlobals.getInterp());
		}
		// TODO: collection of variable tables, semantic checks
		InstrumentCompilation	ic = new InstrumentCompilation(saolGlobals, instrumentMap);
		tree.apply(ic);
		Iterator	it = instrumentMap.keySet().iterator();
		while (it.hasNext())
		{
			TDebug.out("" + it.next());
		}
		return instrumentMap;
	}


	public Map getInstrumentMap()
	{
		if (m_nAction != ACTION_COMPILE_INSTRUMENTS)
		{
			TDebug.out("I.M.: returning null");
			return null;
		}
		TDebug.out("I.M.: " + m_instrumentMap);
		return m_instrumentMap;
	}



	public static void main(String[] arguments)
	{
		int	nAction = ACTION_COMPILE_INSTRUMENTS;
		String	strSaolFilename = arguments[0];
		if (arguments[0].equals("-d"))
		{
			nAction = ACTION_DUMP_TREE;
			strSaolFilename = arguments[1];
		}
		File	saolFile = new File(strSaolFilename);
		Compiler	compiler = new Compiler(saolFile, nAction);
		try
		{
			compiler.compile();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
} 



/*** Compiler.java ***/
