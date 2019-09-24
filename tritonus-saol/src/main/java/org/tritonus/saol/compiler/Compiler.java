/*
 *	Compiler.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer
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

package org.tritonus.saol.compiler;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PushbackReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tritonus.share.TDebug;
import org.tritonus.saol.sablecc.parser.*;
import org.tritonus.saol.sablecc.lexer.*;
import org.tritonus.saol.sablecc.node.*;



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
		InstrumentTable	instrumentTable = new InstrumentTable();
		UserOpcodeTable	opcodeTable = new UserOpcodeTable();
		TemplateTable	templateTable = new TemplateTable();
		Map		instrumentMap = new HashMap();
		NodeSemanticsTable	nodeSemanticsTable = new NodeSemanticsTable();

		/*	Divide the AST into sections. There is one section
			for global, and one for each instrument, opcode or template.
		*/
		TreeDivider	treeDivider = new TreeDivider(instrumentTable,
							      opcodeTable,
							      templateTable);
		tree.apply(treeDivider);
		AGlobaldeclGlobaldecl	globalNode = treeDivider.getGlobalNode();

		/*	Process the global section.
		 */
		SAOLGlobals	saolGlobals = new SAOLGlobals();
		if (globalNode != null)
		{
			GlobalsSearcher	gsearcher = new GlobalsSearcher(saolGlobals);
			globalNode.apply(gsearcher);
		}
		if (DEBUG)
		{
			TDebug.out("a-rate: " + saolGlobals.getARate());
			TDebug.out("k-rate: " + saolGlobals.getKRate());
			TDebug.out("inchannels: " + saolGlobals.getInChannels());
			TDebug.out("outchannels: " + saolGlobals.getOutChannels());
			TDebug.out("interp: " + saolGlobals.getInterp());
		}

		VariableTable	globalVariableTable = new VariableTable();

		/*	Semantic check on instruments.
		 */
		Iterator	instruments = instrumentTable.values().iterator();
		while (instruments.hasNext())
		{
			InstrumentEntry	entry = (InstrumentEntry) instruments.next();
			AInstrdeclInstrdecl	startNode = entry.getStartNode();
			VariableTable		localVariableTable = entry.getLocalVariableTable();
			InstrumentSemanticsCheck	isc = new InstrumentSemanticsCheck(globalVariableTable, localVariableTable, nodeSemanticsTable);
			startNode.apply(isc);
		}
		// TODO: collection of variable tables, semantic checks

		/*	Compiling the instruments.
		 */
		InstrumentCompilation	ic = new InstrumentCompilation(saolGlobals, instrumentMap);
		instruments = instrumentTable.values().iterator();
		while (instruments.hasNext())
		{
			InstrumentEntry	entry = (InstrumentEntry) instruments.next();
			AInstrdeclInstrdecl	node = entry.getStartNode();
			node.apply(ic);
		}

		if (DEBUG)
		{
			Iterator	it = instrumentMap.keySet().iterator();
			while (it.hasNext())
			{
				TDebug.out("" + it.next());
			}
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
