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


import	java.io.Reader;
import	java.io.FileReader;
import	java.io.BufferedReader;
import	java.io.PushbackReader;

import	saol.parser.*;
import	saol.lexer.*;
import	saol.node.*;



public class Compiler
{
	private static final int	ACTION_DUMP_TREE = 0;
	private static final int	ACTION_COMPILE_INSTRUMENTS = 1;
	private static final int	ACTION_GENERATE_MP4 = 2;


	public static void main(String[] arguments)
	{
		int	nAction = ACTION_COMPILE_INSTRUMENTS;
		String	strInputFilename = arguments[0];
		if (arguments[0].equals("-d"))
		{
			nAction = ACTION_DUMP_TREE;
			strInputFilename = arguments[1];
		}
		try
		{
			//System.out.println("Type an arithmetic expression:");

// Create a Parser instance.
//			Reader	inputStreamReader = new InputStreamReader(System.in);
			Reader	reader = new FileReader(strInputFilename);
			reader = new BufferedReader(reader);
			PushbackReader	pbReader = new PushbackReader(reader, 1024);
			Lexer	lexer = new Lexer(pbReader);
			Parser	parser = new Parser(lexer);
			Start	tree = parser.parse();

// Apply the translation.
			switch (nAction)
			{
			case ACTION_DUMP_TREE:
				dumpTree(tree);
				break;

			case ACTION_COMPILE_INSTRUMENTS:
				compileInstruments(tree);
				break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}



	/**	Prints the tree of the given AST on standard output.
	 */
	private static void dumpTree(Start tree)
	{
		PrintWalker	printWalker = new PrintWalker();
		tree.apply(printWalker);
	}



	private static void compileInstruments(Start tree)
	{
		SAOLGlobals	saolGlobals = new SAOLGlobals();
		GlobalsSearcher	gsearcher = new GlobalsSearcher(saolGlobals);
		tree.apply(gsearcher);
		System.out.println("a-rate: " + saolGlobals.getARate());
		System.out.println("k-rate: " + saolGlobals.getKRate());
		System.out.println("inchannels: " + saolGlobals.getInChannels());
		System.out.println("outchannels: " + saolGlobals.getOutChannels());
		System.out.println("interp: " + saolGlobals.getInterp());
		// TODO: collection of variable tables, semantic checks
		InstrumentCompilation	ic = new InstrumentCompilation(saolGlobals);
		tree.apply(ic);
	}
} 



/*** Compiler.java ***/
