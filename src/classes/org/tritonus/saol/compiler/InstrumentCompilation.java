/*
 *	InstrumentCompilation.java
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


import	java.io.IOException;

import	java.util.HashMap;
import	java.util.Map;

import	org.apache.bcel.Constants;
import	org.apache.bcel.classfile.Field;
import	org.apache.bcel.classfile.JavaClass;
import	org.apache.bcel.generic.*;

import	saol.analysis.*;
import	saol.node.*;



public class InstrumentCompilation
extends DepthFirstAdapter
{
	// may become "org.tritonus.saol.generated."
	private static final String	PACKAGE_PREFIX = "";
	private static final String	CLASSFILENAME_PREFIX = "src/";
	private static final String	CLASSFILENAME_SUFFIX = ".class";
	private static final String	SUPERCLASS_NAME = "AbstractInstrument";

	private SAOLGlobals		m_saolGlobals;
	private Map			m_nodeAttributes;
	private String			m_strClassName;
	private ClassGen		m_classGen;
	private ConstantPoolGen		m_constantPoolGen;
	private MethodGen		m_methodGen;
	private InstructionList		m_instructionList;
	private InstructionFactory	m_instructionFactory;
	private BranchInstruction	m_pendingBranchInstruction;

	private boolean			m_bOpvardecls;


	public InstrumentCompilation(SAOLGlobals saolGlobals)
	{
		m_saolGlobals = saolGlobals;
		m_nodeAttributes = new HashMap();
	}



	public void inStart(Start node)
	{
		defaultIn(node);
	}

	public void outStart(Start node)
	{
		defaultOut(node);
	}

	public void defaultIn(Node node)
	{
	}

	public void defaultOut(Node node)
	{
	}

//     public void caseStart(Start node)
//     {
//         inStart(node);
//         node.getPOrcfile().apply(this);
//         node.getEOF().apply(this);
//         outStart(node);
//     }

	public void inAOrcfileOrcfile(AOrcfileOrcfile node)
	{
		defaultIn(node);
	}

	public void outAOrcfileOrcfile(AOrcfileOrcfile node)
	{
		defaultOut(node);
	}

//     public void caseAOrcfileOrcfile(AOrcfileOrcfile node)
//     {
//         inAOrcfileOrcfile(node);
//         {
//             Object temp[] = node.getProc().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PProc) temp[i]).apply(this);
//             }
//         }
//         outAOrcfileOrcfile(node);
//     }

	public void inAInstrdeclProc(AInstrdeclProc node)
	{
		defaultIn(node);
	}

	public void outAInstrdeclProc(AInstrdeclProc node)
	{
		defaultOut(node);
	}

//     public void caseAInstrdeclProc(AInstrdeclProc node)
//     {
//         inAInstrdeclProc(node);
//         if(node.getInstrdecl() != null)
//         {
//             node.getInstrdecl().apply(this);
//         }
//         outAInstrdeclProc(node);
//     }

	public void inAOpcodedeclProc(AOpcodedeclProc node)
	{
		defaultIn(node);
	}

	public void outAOpcodedeclProc(AOpcodedeclProc node)
	{
		defaultOut(node);
	}

//     public void caseAOpcodedeclProc(AOpcodedeclProc node)
//     {
//         inAOpcodedeclProc(node);
//         if(node.getOpcodedecl() != null)
//         {
//             node.getOpcodedecl().apply(this);
//         }
//         outAOpcodedeclProc(node);
//     }

	public void inAGlobaldeclProc(AGlobaldeclProc node)
	{
		defaultIn(node);
	}

	public void outAGlobaldeclProc(AGlobaldeclProc node)
	{
		defaultOut(node);
	}

//     public void caseAGlobaldeclProc(AGlobaldeclProc node)
//     {
//         inAGlobaldeclProc(node);
//         if(node.getGlobaldecl() != null)
//         {
//             node.getGlobaldecl().apply(this);
//         }
//         outAGlobaldeclProc(node);
//     }

	public void inATemplatedeclProc(ATemplatedeclProc node)
	{
		defaultIn(node);
	}

	public void outATemplatedeclProc(ATemplatedeclProc node)
	{
		defaultOut(node);
	}

//     public void caseATemplatedeclProc(ATemplatedeclProc node)
//     {
//         inATemplatedeclProc(node);
//         if(node.getTemplatedecl() != null)
//         {
//             node.getTemplatedecl().apply(this);
//         }
//         outATemplatedeclProc(node);
//     }

	public void inAInstrdeclInstrdecl(AInstrdeclInstrdecl node)
	{
		String	strInstrumentName = node.getIdentifier().getText();
		m_strClassName = PACKAGE_PREFIX + strInstrumentName;
		m_classGen = new ClassGen(m_strClassName,
					  SUPERCLASS_NAME,
					  "<generated>",
					  Constants.ACC_PUBLIC | Constants.ACC_SUPER,
					  null);
		m_constantPoolGen = m_classGen.getConstantPool();
		m_instructionFactory = new InstructionFactory(m_constantPoolGen);
		m_instructionList = new InstructionList();
		m_methodGen = new MethodGen(Constants.ACC_PUBLIC,
					    Type.VOID,
					    new Type[]{new ObjectType("RTSystem")},
					    new String[]{"rtSystem"},
					    "doAPass",
					    m_strClassName,
					    m_instructionList,
					    m_constantPoolGen);
	}

	public void outAInstrdeclInstrdecl(AInstrdeclInstrdecl node)
	{
		appendInstruction(InstructionConstants.RETURN);
		m_methodGen.setMaxStack();
		m_classGen.addMethod(m_methodGen.getMethod());
		m_classGen.addEmptyConstructor(Constants.ACC_PUBLIC);
		JavaClass	javaClass = m_classGen.getJavaClass();
		try
		{
			javaClass.dump("tone.class");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

//     public void caseAInstrdeclInstrdecl(AInstrdeclInstrdecl node)
//     {
//         inAInstrdeclInstrdecl(node);
//         if(node.getInstr() != null)
//         {
//             node.getInstr().apply(this);
//         }
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getIdentlist() != null)
//         {
//             node.getIdentlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getLBrace() != null)
//         {
//             node.getLBrace().apply(this);
//         }
//         {
//             Object temp[] = node.getOpvardecl().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((POpvardecl) temp[i]).apply(this);
//             }
//         }
//         if(node.getBlock() != null)
//         {
//             node.getBlock().apply(this);
//         }
//         if(node.getRBrace() != null)
//         {
//             node.getRBrace().apply(this);
//         }
//         outAInstrdeclInstrdecl(node);
//     }

	public void inAIntListIntList(AIntListIntList node)
	{
		defaultIn(node);
	}

	public void outAIntListIntList(AIntListIntList node)
	{
		defaultOut(node);
	}

//     public void caseAIntListIntList(AIntListIntList node)
//     {
//         inAIntListIntList(node);
//         {
//             Object temp[] = node.getInteger().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((TInteger) temp[i]).apply(this);
//             }
//         }
//         outAIntListIntList(node);
//     }

	public void inAOpcodedeclOpcodedecl(AOpcodedeclOpcodedecl node)
	{
		defaultIn(node);
	}

	public void outAOpcodedeclOpcodedecl(AOpcodedeclOpcodedecl node)
	{
		defaultOut(node);
	}

//     public void caseAOpcodedeclOpcodedecl(AOpcodedeclOpcodedecl node)
//     {
//         inAOpcodedeclOpcodedecl(node);
//         if(node.getOptype() != null)
//         {
//             node.getOptype().apply(this);
//         }
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getParamlist() != null)
//         {
//             node.getParamlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getLBrace() != null)
//         {
//             node.getLBrace().apply(this);
//         }
//         {
//             Object temp[] = node.getOpvardecl().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((POpvardecl) temp[i]).apply(this);
//             }
//         }
//         if(node.getBlock() != null)
//         {
//             node.getBlock().apply(this);
//         }
//         if(node.getRBrace() != null)
//         {
//             node.getRBrace().apply(this);
//         }
//         outAOpcodedeclOpcodedecl(node);
//     }

	public void inAGlobaldeclGlobaldecl(AGlobaldeclGlobaldecl node)
	{
		defaultIn(node);
	}

	public void outAGlobaldeclGlobaldecl(AGlobaldeclGlobaldecl node)
	{
		defaultOut(node);
	}

//     public void caseAGlobaldeclGlobaldecl(AGlobaldeclGlobaldecl node)
//     {
//         inAGlobaldeclGlobaldecl(node);
//         if(node.getGlobal() != null)
//         {
//             node.getGlobal().apply(this);
//         }
//         if(node.getLBrace() != null)
//         {
//             node.getLBrace().apply(this);
//         }
//         {
//             Object temp[] = node.getGlobaldef().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PGlobaldef) temp[i]).apply(this);
//             }
//         }
//         if(node.getRBrace() != null)
//         {
//             node.getRBrace().apply(this);
//         }
//         outAGlobaldeclGlobaldecl(node);
//     }

	public void inATemplatedeclTemplatedecl(ATemplatedeclTemplatedecl node)
	{
		defaultIn(node);
	}

	public void outATemplatedeclTemplatedecl(ATemplatedeclTemplatedecl node)
	{
		defaultOut(node);
	}

//     public void caseATemplatedeclTemplatedecl(ATemplatedeclTemplatedecl node)
//     {
//         inATemplatedeclTemplatedecl(node);
//         if(node.getTemplate() != null)
//         {
//             node.getTemplate().apply(this);
//         }
//         if(node.getLt() != null)
//         {
//             node.getLt().apply(this);
//         }
//         if(node.getIdentlist1() != null)
//         {
//             node.getIdentlist1().apply(this);
//         }
//         if(node.getGt() != null)
//         {
//             node.getGt().apply(this);
//         }
//         if(node.getPresetClause() != null)
//         {
//             node.getPresetClause().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getIdentlist2() != null)
//         {
//             node.getIdentlist2().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getMap() != null)
//         {
//             node.getMap().apply(this);
//         }
//         if(node.getLBrace1() != null)
//         {
//             node.getLBrace1().apply(this);
//         }
//         if(node.getIdentlist3() != null)
//         {
//             node.getIdentlist3().apply(this);
//         }
//         if(node.getRBrace1() != null)
//         {
//             node.getRBrace1().apply(this);
//         }
//         if(node.getWith() != null)
//         {
//             node.getWith().apply(this);
//         }
//         if(node.getLBrace2() != null)
//         {
//             node.getLBrace2().apply(this);
//         }
//         if(node.getMapblock() != null)
//         {
//             node.getMapblock().apply(this);
//         }
//         if(node.getRBrace2() != null)
//         {
//             node.getRBrace2().apply(this);
//         }
//         if(node.getLBrace3() != null)
//         {
//             node.getLBrace3().apply(this);
//         }
//         {
//             Object temp[] = node.getVardecl().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PVardecl) temp[i]).apply(this);
//             }
//         }
//         if(node.getBlock() != null)
//         {
//             node.getBlock().apply(this);
//         }
//         if(node.getRBrace3() != null)
//         {
//             node.getRBrace3().apply(this);
//         }
//         outATemplatedeclTemplatedecl(node);
//     }

	public void inAPresetClausePresetClause(APresetClausePresetClause node)
	{
		defaultIn(node);
	}

	public void outAPresetClausePresetClause(APresetClausePresetClause node)
	{
		defaultOut(node);
	}

//     public void caseAPresetClausePresetClause(APresetClausePresetClause node)
//     {
//         inAPresetClausePresetClause(node);
//         if(node.getPreset() != null)
//         {
//             node.getPreset().apply(this);
//         }
//         if(node.getMapblock() != null)
//         {
//             node.getMapblock().apply(this);
//         }
//         outAPresetClausePresetClause(node);
//     }

	public void inAMapblockMapblock(AMapblockMapblock node)
	{
		defaultIn(node);
	}

	public void outAMapblockMapblock(AMapblockMapblock node)
	{
		defaultOut(node);
	}

//     public void caseAMapblockMapblock(AMapblockMapblock node)
//     {
//         inAMapblockMapblock(node);
//         if(node.getLt() != null)
//         {
//             node.getLt().apply(this);
//         }
//         if(node.getTerminalList() != null)
//         {
//             node.getTerminalList().apply(this);
//         }
//         if(node.getGt() != null)
//         {
//             node.getGt().apply(this);
//         }
//         {
//             Object temp[] = node.getMapblockTail().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PMapblockTail) temp[i]).apply(this);
//             }
//         }
//         outAMapblockMapblock(node);
//     }

	public void inAMapblockTailMapblockTail(AMapblockTailMapblockTail node)
	{
		defaultIn(node);
	}

	public void outAMapblockTailMapblockTail(AMapblockTailMapblockTail node)
	{
		defaultOut(node);
	}

//     public void caseAMapblockTailMapblockTail(AMapblockTailMapblockTail node)
//     {
//         inAMapblockTailMapblockTail(node);
//         if(node.getComma() != null)
//         {
//             node.getComma().apply(this);
//         }
//         if(node.getLt() != null)
//         {
//             node.getLt().apply(this);
//         }
//         if(node.getTerminalList() != null)
//         {
//             node.getTerminalList().apply(this);
//         }
//         if(node.getGt() != null)
//         {
//             node.getGt().apply(this);
//         }
//         outAMapblockTailMapblockTail(node);
//     }

	public void inATerminalListTerminalList(ATerminalListTerminalList node)
	{
		defaultIn(node);
	}

	public void outATerminalListTerminalList(ATerminalListTerminalList node)
	{
		defaultOut(node);
	}

//     public void caseATerminalListTerminalList(ATerminalListTerminalList node)
//     {
//         inATerminalListTerminalList(node);
//         if(node.getTerminal() != null)
//         {
//             node.getTerminal().apply(this);
//         }
//         {
//             Object temp[] = node.getTerminalListTail().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PTerminalListTail) temp[i]).apply(this);
//             }
//         }
//         outATerminalListTerminalList(node);
//     }

	public void inATerminalListTailTerminalListTail(ATerminalListTailTerminalListTail node)
	{
		defaultIn(node);
	}

	public void outATerminalListTailTerminalListTail(ATerminalListTailTerminalListTail node)
	{
		defaultOut(node);
	}

//     public void caseATerminalListTailTerminalListTail(ATerminalListTailTerminalListTail node)
//     {
//         inATerminalListTailTerminalListTail(node);
//         if(node.getComma() != null)
//         {
//             node.getComma().apply(this);
//         }
//         if(node.getTerminal() != null)
//         {
//             node.getTerminal().apply(this);
//         }
//         outATerminalListTailTerminalListTail(node);
//     }

	public void inAIdentifierTerminal(AIdentifierTerminal node)
	{
		defaultIn(node);
	}

	public void outAIdentifierTerminal(AIdentifierTerminal node)
	{
		defaultOut(node);
	}

//     public void caseAIdentifierTerminal(AIdentifierTerminal node)
//     {
//         inAIdentifierTerminal(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         outAIdentifierTerminal(node);
//     }

	public void inAConstantTerminal(AConstantTerminal node)
	{
	}

	public void outAConstantTerminal(AConstantTerminal node)
	{
	}

//     public void caseAConstantTerminal(AConstantTerminal node)
//     {
//         inAConstantTerminal(node);
//         if(node.getConst() != null)
//         {
//             node.getConst().apply(this);
//         }
//         outAConstantTerminal(node);
//     }

	public void inAStringTerminal(AStringTerminal node)
	{
		defaultIn(node);
	}

	public void outAStringTerminal(AStringTerminal node)
	{
		defaultOut(node);
	}

//     public void caseAStringTerminal(AStringTerminal node)
//     {
//         inAStringTerminal(node);
//         if(node.getString() != null)
//         {
//             node.getString().apply(this);
//         }
//         outAStringTerminal(node);
//     }

	public void inARtparamGlobaldef(ARtparamGlobaldef node)
	{
		defaultIn(node);
	}

	public void outARtparamGlobaldef(ARtparamGlobaldef node)
	{
		defaultOut(node);
	}

//     public void caseARtparamGlobaldef(ARtparamGlobaldef node)
//     {
//         inARtparamGlobaldef(node);
//         if(node.getRtparam() != null)
//         {
//             node.getRtparam().apply(this);
//         }
//         outARtparamGlobaldef(node);
//     }

	public void inAVardeclGlobaldef(AVardeclGlobaldef node)
	{
		defaultIn(node);
	}

	public void outAVardeclGlobaldef(AVardeclGlobaldef node)
	{
		defaultOut(node);
	}

//     public void caseAVardeclGlobaldef(AVardeclGlobaldef node)
//     {
//         inAVardeclGlobaldef(node);
//         if(node.getVardecl() != null)
//         {
//             node.getVardecl().apply(this);
//         }
//         outAVardeclGlobaldef(node);
//     }

	public void inARoutedefGlobaldef(ARoutedefGlobaldef node)
	{
		defaultIn(node);
	}

	public void outARoutedefGlobaldef(ARoutedefGlobaldef node)
	{
		defaultOut(node);
	}

//     public void caseARoutedefGlobaldef(ARoutedefGlobaldef node)
//     {
//         inARoutedefGlobaldef(node);
//         if(node.getRoutedef() != null)
//         {
//             node.getRoutedef().apply(this);
//         }
//         outARoutedefGlobaldef(node);
//     }

	public void inASenddefGlobaldef(ASenddefGlobaldef node)
	{
		defaultIn(node);
	}

	public void outASenddefGlobaldef(ASenddefGlobaldef node)
	{
		defaultOut(node);
	}

//     public void caseASenddefGlobaldef(ASenddefGlobaldef node)
//     {
//         inASenddefGlobaldef(node);
//         if(node.getSenddef() != null)
//         {
//             node.getSenddef().apply(this);
//         }
//         outASenddefGlobaldef(node);
//     }

	public void inASeqdefGlobaldef(ASeqdefGlobaldef node)
	{
		defaultIn(node);
	}

	public void outASeqdefGlobaldef(ASeqdefGlobaldef node)
	{
		defaultOut(node);
	}

//     public void caseASeqdefGlobaldef(ASeqdefGlobaldef node)
//     {
//         inASeqdefGlobaldef(node);
//         if(node.getSeqdef() != null)
//         {
//             node.getSeqdef().apply(this);
//         }
//         outASeqdefGlobaldef(node);
//     }

	public void inASrateRtparam(ASrateRtparam node)
	{
		defaultIn(node);
	}

	public void outASrateRtparam(ASrateRtparam node)
	{
		defaultOut(node);
	}

//     public void caseASrateRtparam(ASrateRtparam node)
//     {
//         inASrateRtparam(node);
//         if(node.getSrate() != null)
//         {
//             node.getSrate().apply(this);
//         }
//         if(node.getInteger() != null)
//         {
//             node.getInteger().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outASrateRtparam(node);
//     }

	public void inAKrateRtparam(AKrateRtparam node)
	{
		defaultIn(node);
	}

	public void outAKrateRtparam(AKrateRtparam node)
	{
		defaultOut(node);
	}

//     public void caseAKrateRtparam(AKrateRtparam node)
//     {
//         inAKrateRtparam(node);
//         if(node.getKrate() != null)
//         {
//             node.getKrate().apply(this);
//         }
//         if(node.getInteger() != null)
//         {
//             node.getInteger().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAKrateRtparam(node);
//     }

	public void inAInchannelsRtparam(AInchannelsRtparam node)
	{
		defaultIn(node);
	}

	public void outAInchannelsRtparam(AInchannelsRtparam node)
	{
		defaultOut(node);
	}

//     public void caseAInchannelsRtparam(AInchannelsRtparam node)
//     {
//         inAInchannelsRtparam(node);
//         if(node.getInchannels() != null)
//         {
//             node.getInchannels().apply(this);
//         }
//         if(node.getInteger() != null)
//         {
//             node.getInteger().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAInchannelsRtparam(node);
//     }

	public void inAOutchannelsRtparam(AOutchannelsRtparam node)
	{
		defaultIn(node);
	}

	public void outAOutchannelsRtparam(AOutchannelsRtparam node)
	{
		defaultOut(node);
	}

//     public void caseAOutchannelsRtparam(AOutchannelsRtparam node)
//     {
//         inAOutchannelsRtparam(node);
//         if(node.getOutchannels() != null)
//         {
//             node.getOutchannels().apply(this);
//         }
//         if(node.getInteger() != null)
//         {
//             node.getInteger().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAOutchannelsRtparam(node);
//     }

	public void inAInterpRtparam(AInterpRtparam node)
	{
		defaultIn(node);
	}

	public void outAInterpRtparam(AInterpRtparam node)
	{
		defaultOut(node);
	}

//     public void caseAInterpRtparam(AInterpRtparam node)
//     {
//         inAInterpRtparam(node);
//         if(node.getInterp() != null)
//         {
//             node.getInterp().apply(this);
//         }
//         if(node.getInteger() != null)
//         {
//             node.getInteger().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAInterpRtparam(node);
//     }

	public void inARoutedefRoutedef(ARoutedefRoutedef node)
	{
		defaultIn(node);
	}

	public void outARoutedefRoutedef(ARoutedefRoutedef node)
	{
		defaultOut(node);
	}

//     public void caseARoutedefRoutedef(ARoutedefRoutedef node)
//     {
//         inARoutedefRoutedef(node);
//         if(node.getRoute() != null)
//         {
//             node.getRoute().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getComma() != null)
//         {
//             node.getComma().apply(this);
//         }
//         if(node.getIdentlist() != null)
//         {
//             node.getIdentlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outARoutedefRoutedef(node);
//     }

	public void inASenddefSenddef(ASenddefSenddef node)
	{
		defaultIn(node);
	}

	public void outASenddefSenddef(ASenddefSenddef node)
	{
		defaultOut(node);
	}

//     public void caseASenddefSenddef(ASenddefSenddef node)
//     {
//         inASenddefSenddef(node);
//         if(node.getSend() != null)
//         {
//             node.getSend().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getSemicolon1() != null)
//         {
//             node.getSemicolon1().apply(this);
//         }
//         if(node.getExprlist() != null)
//         {
//             node.getExprlist().apply(this);
//         }
//         if(node.getSemicolon2() != null)
//         {
//             node.getSemicolon2().apply(this);
//         }
//         if(node.getIdentlist() != null)
//         {
//             node.getIdentlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getSemicolon3() != null)
//         {
//             node.getSemicolon3().apply(this);
//         }
//         outASenddefSenddef(node);
//     }

	public void inASeqdefSeqdef(ASeqdefSeqdef node)
	{
		defaultIn(node);
	}

	public void outASeqdefSeqdef(ASeqdefSeqdef node)
	{
		defaultOut(node);
	}

//     public void caseASeqdefSeqdef(ASeqdefSeqdef node)
//     {
//         inASeqdefSeqdef(node);
//         if(node.getSequence() != null)
//         {
//             node.getSequence().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getIdentlist() != null)
//         {
//             node.getIdentlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outASeqdefSeqdef(node);
//     }

	public void inABlockBlock(ABlockBlock node)
	{
		defaultIn(node);
	}

	public void outABlockBlock(ABlockBlock node)
	{
		defaultOut(node);
	}

//     public void caseABlockBlock(ABlockBlock node)
//     {
//         inABlockBlock(node);
//         {
//             Object temp[] = node.getStatement().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PStatement) temp[i]).apply(this);
//             }
//         }
//         outABlockBlock(node);
//     }

	public void inAAssignmentStatement(AAssignmentStatement node)
	{
		appendInstruction(InstructionConstants.ALOAD_0);
	}



	public void outAAssignmentStatement(AAssignmentStatement node)
	{
		String	strVariableName = ((ASimpleLvalue) node.getLvalue()).getIdentifier().getText();
		appendPutField(strVariableName);
	}

//     public void caseAAssignmentStatement(AAssignmentStatement node)
//     {
//         inAAssignmentStatement(node);
//         if(node.getLvalue() != null)
//         {
//             node.getLvalue().apply(this);
//         }
//         if(node.getAssign() != null)
//         {
//             node.getAssign().apply(this);
//         }
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAAssignmentStatement(node);
//     }

	public void inAExpressionStatement(AExpressionStatement node)
	{
		defaultIn(node);
	}

	public void outAExpressionStatement(AExpressionStatement node)
	{
		defaultOut(node);
	}

//     public void caseAExpressionStatement(AExpressionStatement node)
//     {
//         inAExpressionStatement(node);
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAExpressionStatement(node);
//     }

	public void inAIfStatement(AIfStatement node)
	{
	}

	public void outAIfStatement(AIfStatement node)
	{
	}



	public void caseAIfStatement(AIfStatement node)
	{
		inAIfStatement(node);
        if(node.getIf() != null)
        {
            node.getIf().apply(this);
        }
        if(node.getLPar() != null)
        {
            node.getLPar().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getRPar() != null)
        {
            node.getRPar().apply(this);
        }
	appendInstruction(InstructionConstants.FCONST_0);
	appendInstruction(InstructionConstants.FCMPL);
	BranchInstruction	ifeq = new IFEQ(null);
	appendInstruction(ifeq);
        if(node.getLBrace() != null)
        {
            node.getLBrace().apply(this);
        }
        if(node.getBlock() != null)
        {
            node.getBlock().apply(this);
        }
        if(node.getRBrace() != null)
        {
            node.getRBrace().apply(this);
        }
	setPendingBranchInstruction(ifeq);
        outAIfStatement(node);
    }

	public void inAIfElseStatement(AIfElseStatement node)
	{
		defaultIn(node);
	}

	public void outAIfElseStatement(AIfElseStatement node)
	{
		defaultOut(node);
	}

//     public void caseAIfElseStatement(AIfElseStatement node)
//     {
//         inAIfElseStatement(node);
//         if(node.getIf() != null)
//         {
//             node.getIf().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getLBrace1() != null)
//         {
//             node.getLBrace1().apply(this);
//         }
//         if(node.getBlock1() != null)
//         {
//             node.getBlock1().apply(this);
//         }
//         if(node.getRBrace1() != null)
//         {
//             node.getRBrace1().apply(this);
//         }
//         if(node.getElse() != null)
//         {
//             node.getElse().apply(this);
//         }
//         if(node.getLBrace2() != null)
//         {
//             node.getLBrace2().apply(this);
//         }
//         if(node.getBlock2() != null)
//         {
//             node.getBlock2().apply(this);
//         }
//         if(node.getRBrace2() != null)
//         {
//             node.getRBrace2().apply(this);
//         }
//         outAIfElseStatement(node);
//     }

	public void inAWhileStatement(AWhileStatement node)
	{
		defaultIn(node);
	}

	public void outAWhileStatement(AWhileStatement node)
	{
		defaultOut(node);
	}

//     public void caseAWhileStatement(AWhileStatement node)
//     {
//         inAWhileStatement(node);
//         if(node.getWhile() != null)
//         {
//             node.getWhile().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getLBrace() != null)
//         {
//             node.getLBrace().apply(this);
//         }
//         if(node.getBlock() != null)
//         {
//             node.getBlock().apply(this);
//         }
//         if(node.getRBrace() != null)
//         {
//             node.getRBrace().apply(this);
//         }
//         outAWhileStatement(node);
//     }

	public void inAInstrumentStatement(AInstrumentStatement node)
	{
		defaultIn(node);
	}

	public void outAInstrumentStatement(AInstrumentStatement node)
	{
		defaultOut(node);
	}

//     public void caseAInstrumentStatement(AInstrumentStatement node)
//     {
//         inAInstrumentStatement(node);
//         if(node.getInstr() != null)
//         {
//             node.getInstr().apply(this);
//         }
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getExprlist() != null)
//         {
//             node.getExprlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAInstrumentStatement(node);
//     }

	public void inAOutputStatement(AOutputStatement node)
	{
		defaultIn(node);
	}

	public void outAOutputStatement(AOutputStatement node)
	{
		defaultOut(node);
	}

//     public void caseAOutputStatement(AOutputStatement node)
//     {
//         inAOutputStatement(node);
//         if(node.getOutput() != null)
//         {
//             node.getOutput().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getExprlist() != null)
//         {
//             node.getExprlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAOutputStatement(node);
//     }

	public void inASpatializeStatement(ASpatializeStatement node)
	{
		defaultIn(node);
	}

	public void outASpatializeStatement(ASpatializeStatement node)
	{
		defaultOut(node);
	}

//     public void caseASpatializeStatement(ASpatializeStatement node)
//     {
//         inASpatializeStatement(node);
//         if(node.getSpatialize() != null)
//         {
//             node.getSpatialize().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getExprlist() != null)
//         {
//             node.getExprlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outASpatializeStatement(node);
//     }

	public void inAOutbusStatement(AOutbusStatement node)
	{
		defaultIn(node);
	}

	public void outAOutbusStatement(AOutbusStatement node)
	{
		defaultOut(node);
	}

//     public void caseAOutbusStatement(AOutbusStatement node)
//     {
//         inAOutbusStatement(node);
//         if(node.getOutbus() != null)
//         {
//             node.getOutbus().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getComma() != null)
//         {
//             node.getComma().apply(this);
//         }
//         if(node.getExprlist() != null)
//         {
//             node.getExprlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAOutbusStatement(node);
//     }

	public void inAExtendStatement(AExtendStatement node)
	{
		defaultIn(node);
	}

	public void outAExtendStatement(AExtendStatement node)
	{
		defaultOut(node);
	}

//     public void caseAExtendStatement(AExtendStatement node)
//     {
//         inAExtendStatement(node);
//         if(node.getExtend() != null)
//         {
//             node.getExtend().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAExtendStatement(node);
//     }

	public void inATurnoffStatement(ATurnoffStatement node)
	{
		defaultIn(node);
	}

	public void outATurnoffStatement(ATurnoffStatement node)
	{
		defaultOut(node);
	}

//     public void caseATurnoffStatement(ATurnoffStatement node)
//     {
//         inATurnoffStatement(node);
//         if(node.getTurnoff() != null)
//         {
//             node.getTurnoff().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outATurnoffStatement(node);
//     }

	public void inAReturnStatement(AReturnStatement node)
	{
		defaultIn(node);
	}

	public void outAReturnStatement(AReturnStatement node)
	{
		defaultOut(node);
	}

//     public void caseAReturnStatement(AReturnStatement node)
//     {
//         inAReturnStatement(node);
//         if(node.getReturn() != null)
//         {
//             node.getReturn().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outAReturnStatement(node);
//     }

	public void inASimpleLvalue(ASimpleLvalue node)
	{
		defaultIn(node);
	}

	public void outASimpleLvalue(ASimpleLvalue node)
	{
		defaultOut(node);
	}

//     public void caseASimpleLvalue(ASimpleLvalue node)
//     {
//         inASimpleLvalue(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         outASimpleLvalue(node);
//     }

	public void inAIndexedLvalue(AIndexedLvalue node)
	{
		defaultIn(node);
	}

	public void outAIndexedLvalue(AIndexedLvalue node)
	{
		defaultOut(node);
	}

//     public void caseAIndexedLvalue(AIndexedLvalue node)
//     {
//         inAIndexedLvalue(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLBracket() != null)
//         {
//             node.getLBracket().apply(this);
//         }
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         if(node.getRBracket() != null)
//         {
//             node.getRBracket().apply(this);
//         }
//         outAIndexedLvalue(node);
//     }

	public void inAIdentlistIdentlist(AIdentlistIdentlist node)
	{
		defaultIn(node);
	}

	public void outAIdentlistIdentlist(AIdentlistIdentlist node)
	{
		defaultOut(node);
	}

//     public void caseAIdentlistIdentlist(AIdentlistIdentlist node)
//     {
//         inAIdentlistIdentlist(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         {
//             Object temp[] = node.getIdentlistTail().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PIdentlistTail) temp[i]).apply(this);
//             }
//         }
//         outAIdentlistIdentlist(node);
//     }

	public void inAIdentlistTailIdentlistTail(AIdentlistTailIdentlistTail node)
	{
		defaultIn(node);
	}

	public void outAIdentlistTailIdentlistTail(AIdentlistTailIdentlistTail node)
	{
		defaultOut(node);
	}

//     public void caseAIdentlistTailIdentlistTail(AIdentlistTailIdentlistTail node)
//     {
//         inAIdentlistTailIdentlistTail(node);
//         if(node.getComma() != null)
//         {
//             node.getComma().apply(this);
//         }
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         outAIdentlistTailIdentlistTail(node);
//     }

	public void inAParamlistParamlist(AParamlistParamlist node)
	{
		defaultIn(node);
	}

	public void outAParamlistParamlist(AParamlistParamlist node)
	{
		defaultOut(node);
	}

//     public void caseAParamlistParamlist(AParamlistParamlist node)
//     {
//         inAParamlistParamlist(node);
//         if(node.getParamdecl() != null)
//         {
//             node.getParamdecl().apply(this);
//         }
//         {
//             Object temp[] = node.getParamlistTail().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PParamlistTail) temp[i]).apply(this);
//             }
//         }
//         outAParamlistParamlist(node);
//     }

	public void inAParamlistTailParamlistTail(AParamlistTailParamlistTail node)
	{
		defaultIn(node);
	}

	public void outAParamlistTailParamlistTail(AParamlistTailParamlistTail node)
	{
		defaultOut(node);
	}

//     public void caseAParamlistTailParamlistTail(AParamlistTailParamlistTail node)
//     {
//         inAParamlistTailParamlistTail(node);
//         if(node.getComma() != null)
//         {
//             node.getComma().apply(this);
//         }
//         if(node.getParamdecl() != null)
//         {
//             node.getParamdecl().apply(this);
//         }
//         outAParamlistTailParamlistTail(node);
//     }

	public void inASigvarVardecl(ASigvarVardecl node)
	{
		defaultIn(node);
	}

	public void outASigvarVardecl(ASigvarVardecl node)
	{
		defaultOut(node);
	}

//     public void caseASigvarVardecl(ASigvarVardecl node)
//     {
//         inASigvarVardecl(node);
//         if(node.getTaglist() != null)
//         {
//             node.getTaglist().apply(this);
//         }
//         if(node.getStype() != null)
//         {
//             node.getStype().apply(this);
//         }
//         if(node.getNamelist() != null)
//         {
//             node.getNamelist().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outASigvarVardecl(node);
//     }

	public void inATablemapVardecl(ATablemapVardecl node)
	{
		defaultIn(node);
	}

	public void outATablemapVardecl(ATablemapVardecl node)
	{
		defaultOut(node);
	}

//     public void caseATablemapVardecl(ATablemapVardecl node)
//     {
//         inATablemapVardecl(node);
//         if(node.getTablemap() != null)
//         {
//             node.getTablemap().apply(this);
//         }
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getIdentlist() != null)
//         {
//             node.getIdentlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outATablemapVardecl(node);
//     }

	public void inASigvarOpvardecl(ASigvarOpvardecl node)
	{
		m_bOpvardecls = true;
	}


	public void outASigvarOpvardecl(ASigvarOpvardecl node)
	{
		m_bOpvardecls = false;
	}



//     public void caseASigvarOpvardecl(ASigvarOpvardecl node)
//     {
//         inASigvarOpvardecl(node);
//         if(node.getTaglist() != null)
//         {
//             node.getTaglist().apply(this);
//         }
//         if(node.getOtype() != null)
//         {
//             node.getOtype().apply(this);
//         }
//         if(node.getNamelist() != null)
//         {
//             node.getNamelist().apply(this);
//         }
//         if(node.getSemicolon() != null)
//         {
//             node.getSemicolon().apply(this);
//         }
//         outASigvarOpvardecl(node);
//     }

	public void inAParamdeclParamdecl(AParamdeclParamdecl node)
	{
		defaultIn(node);
	}

	public void outAParamdeclParamdecl(AParamdeclParamdecl node)
	{
		defaultOut(node);
	}

//     public void caseAParamdeclParamdecl(AParamdeclParamdecl node)
//     {
//         inAParamdeclParamdecl(node);
//         if(node.getOtype() != null)
//         {
//             node.getOtype().apply(this);
//         }
//         if(node.getName() != null)
//         {
//             node.getName().apply(this);
//         }
//         outAParamdeclParamdecl(node);
//     }

	public void inANamelistNamelist(ANamelistNamelist node)
	{
		defaultIn(node);
	}

	public void outANamelistNamelist(ANamelistNamelist node)
	{
		defaultOut(node);
	}

//     public void caseANamelistNamelist(ANamelistNamelist node)
//     {
//         inANamelistNamelist(node);
//         if(node.getName() != null)
//         {
//             node.getName().apply(this);
//         }
//         {
//             Object temp[] = node.getNamelistTail().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PNamelistTail) temp[i]).apply(this);
//             }
//         }
//         outANamelistNamelist(node);
//     }

	public void inANamelistTailNamelistTail(ANamelistTailNamelistTail node)
	{
		defaultIn(node);
	}

	public void outANamelistTailNamelistTail(ANamelistTailNamelistTail node)
	{
		defaultOut(node);
	}

//     public void caseANamelistTailNamelistTail(ANamelistTailNamelistTail node)
//     {
//         inANamelistTailNamelistTail(node);
//         if(node.getComma() != null)
//         {
//             node.getComma().apply(this);
//         }
//         if(node.getName() != null)
//         {
//             node.getName().apply(this);
//         }
//         outANamelistTailNamelistTail(node);
//     }

	public void inASimpleName(ASimpleName node)
	{
		defaultIn(node);
	}

	public void outASimpleName(ASimpleName node)
	{
		if (m_bOpvardecls)
		{
			String	strVariableName = node.getIdentifier().getText();
			addLocalVariable(strVariableName);
		}
	}

//     public void caseASimpleName(ASimpleName node)
//     {
//         inASimpleName(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         outASimpleName(node);
//     }

	public void inAIndexedName(AIndexedName node)
	{
		defaultIn(node);
	}

	public void outAIndexedName(AIndexedName node)
	{
		defaultOut(node);
	}

//     public void caseAIndexedName(AIndexedName node)
//     {
//         inAIndexedName(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLBracket() != null)
//         {
//             node.getLBracket().apply(this);
//         }
//         if(node.getInteger() != null)
//         {
//             node.getInteger().apply(this);
//         }
//         if(node.getRBracket() != null)
//         {
//             node.getRBracket().apply(this);
//         }
//         outAIndexedName(node);
//     }

	public void inAInchannelsName(AInchannelsName node)
	{
		defaultIn(node);
	}

	public void outAInchannelsName(AInchannelsName node)
	{
		defaultOut(node);
	}

//     public void caseAInchannelsName(AInchannelsName node)
//     {
//         inAInchannelsName(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLBracket() != null)
//         {
//             node.getLBracket().apply(this);
//         }
//         if(node.getInchannels() != null)
//         {
//             node.getInchannels().apply(this);
//         }
//         if(node.getRBracket() != null)
//         {
//             node.getRBracket().apply(this);
//         }
//         outAInchannelsName(node);
//     }

	public void inAOutchannelsName(AOutchannelsName node)
	{
		defaultIn(node);
	}

	public void outAOutchannelsName(AOutchannelsName node)
	{
		defaultOut(node);
	}

//     public void caseAOutchannelsName(AOutchannelsName node)
//     {
//         inAOutchannelsName(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLBracket() != null)
//         {
//             node.getLBracket().apply(this);
//         }
//         if(node.getOutchannels() != null)
//         {
//             node.getOutchannels().apply(this);
//         }
//         if(node.getRBracket() != null)
//         {
//             node.getRBracket().apply(this);
//         }
//         outAOutchannelsName(node);
//     }

	public void inAIvarStype(AIvarStype node)
	{
		defaultIn(node);
	}

	public void outAIvarStype(AIvarStype node)
	{
		defaultOut(node);
	}

//     public void caseAIvarStype(AIvarStype node)
//     {
//         inAIvarStype(node);
//         if(node.getIvar() != null)
//         {
//             node.getIvar().apply(this);
//         }
//         outAIvarStype(node);
//     }

	public void inAKsigStype(AKsigStype node)
	{
		defaultIn(node);
	}

	public void outAKsigStype(AKsigStype node)
	{
		defaultOut(node);
	}

//     public void caseAKsigStype(AKsigStype node)
//     {
//         inAKsigStype(node);
//         if(node.getKsig() != null)
//         {
//             node.getKsig().apply(this);
//         }
//         outAKsigStype(node);
//     }

	public void inAAsigStype(AAsigStype node)
	{
		defaultIn(node);
	}

	public void outAAsigStype(AAsigStype node)
	{
		defaultOut(node);
	}

//     public void caseAAsigStype(AAsigStype node)
//     {
//         inAAsigStype(node);
//         if(node.getAsig() != null)
//         {
//             node.getAsig().apply(this);
//         }
//         outAAsigStype(node);
//     }

	public void inATableStype(ATableStype node)
	{
		defaultIn(node);
	}

	public void outATableStype(ATableStype node)
	{
		defaultOut(node);
	}

//     public void caseATableStype(ATableStype node)
//     {
//         inATableStype(node);
//         if(node.getTable() != null)
//         {
//             node.getTable().apply(this);
//         }
//         outATableStype(node);
//     }

	public void inAOparrayStype(AOparrayStype node)
	{
		defaultIn(node);
	}

	public void outAOparrayStype(AOparrayStype node)
	{
		defaultOut(node);
	}

//     public void caseAOparrayStype(AOparrayStype node)
//     {
//         inAOparrayStype(node);
//         if(node.getOparray() != null)
//         {
//             node.getOparray().apply(this);
//         }
//         outAOparrayStype(node);
//     }

	public void inAXsigOtype(AXsigOtype node)
	{
		defaultIn(node);
	}

	public void outAXsigOtype(AXsigOtype node)
	{
		defaultOut(node);
	}

//     public void caseAXsigOtype(AXsigOtype node)
//     {
//         inAXsigOtype(node);
//         if(node.getXsig() != null)
//         {
//             node.getXsig().apply(this);
//         }
//         outAXsigOtype(node);
//     }

	public void inAStypeOtype(AStypeOtype node)
	{
		defaultIn(node);
	}

	public void outAStypeOtype(AStypeOtype node)
	{
		defaultOut(node);
	}

//     public void caseAStypeOtype(AStypeOtype node)
//     {
//         inAStypeOtype(node);
//         if(node.getStype() != null)
//         {
//             node.getStype().apply(this);
//         }
//         outAStypeOtype(node);
//     }

	public void inATabledeclTabledecl(ATabledeclTabledecl node)
	{
		defaultIn(node);
	}

	public void outATabledeclTabledecl(ATabledeclTabledecl node)
	{
		defaultOut(node);
	}

//     public void caseATabledeclTabledecl(ATabledeclTabledecl node)
//     {
//         inATabledeclTabledecl(node);
//         if(node.getTable() != null)
//         {
//             node.getTable().apply(this);
//         }
//         if(node.getIdentifier1() != null)
//         {
//             node.getIdentifier1().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getIdentifier2() != null)
//         {
//             node.getIdentifier2().apply(this);
//         }
//         if(node.getComma() != null)
//         {
//             node.getComma().apply(this);
//         }
//         if(node.getExprstrlist() != null)
//         {
//             node.getExprstrlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         outATabledeclTabledecl(node);
//     }

	public void inAImportsTaglist(AImportsTaglist node)
	{
		defaultIn(node);
	}

	public void outAImportsTaglist(AImportsTaglist node)
	{
		defaultOut(node);
	}

//     public void caseAImportsTaglist(AImportsTaglist node)
//     {
//         inAImportsTaglist(node);
//         if(node.getImports() != null)
//         {
//             node.getImports().apply(this);
//         }
//         outAImportsTaglist(node);
//     }

	public void inAExportsTaglist(AExportsTaglist node)
	{
		defaultIn(node);
	}

	public void outAExportsTaglist(AExportsTaglist node)
	{
		defaultOut(node);
	}

//     public void caseAExportsTaglist(AExportsTaglist node)
//     {
//         inAExportsTaglist(node);
//         if(node.getExports() != null)
//         {
//             node.getExports().apply(this);
//         }
//         outAExportsTaglist(node);
//     }

	public void inAImportsexportsTaglist(AImportsexportsTaglist node)
	{
		defaultIn(node);
	}

	public void outAImportsexportsTaglist(AImportsexportsTaglist node)
	{
		defaultOut(node);
	}

//     public void caseAImportsexportsTaglist(AImportsexportsTaglist node)
//     {
//         inAImportsexportsTaglist(node);
//         if(node.getImports() != null)
//         {
//             node.getImports().apply(this);
//         }
//         if(node.getExports() != null)
//         {
//             node.getExports().apply(this);
//         }
//         outAImportsexportsTaglist(node);
//     }

	public void inAExportsimportsTaglist(AExportsimportsTaglist node)
	{
		defaultIn(node);
	}

	public void outAExportsimportsTaglist(AExportsimportsTaglist node)
	{
		defaultOut(node);
	}

//     public void caseAExportsimportsTaglist(AExportsimportsTaglist node)
//     {
//         inAExportsimportsTaglist(node);
//         if(node.getExports() != null)
//         {
//             node.getExports().apply(this);
//         }
//         if(node.getImports() != null)
//         {
//             node.getImports().apply(this);
//         }
//         outAExportsimportsTaglist(node);
//     }

	public void inAAopcodeOptype(AAopcodeOptype node)
	{
		defaultIn(node);
	}

	public void outAAopcodeOptype(AAopcodeOptype node)
	{
		defaultOut(node);
	}

//     public void caseAAopcodeOptype(AAopcodeOptype node)
//     {
//         inAAopcodeOptype(node);
//         if(node.getAopcode() != null)
//         {
//             node.getAopcode().apply(this);
//         }
//         outAAopcodeOptype(node);
//     }

	public void inAKopcodeOptype(AKopcodeOptype node)
	{
		defaultIn(node);
	}

	public void outAKopcodeOptype(AKopcodeOptype node)
	{
		defaultOut(node);
	}

//     public void caseAKopcodeOptype(AKopcodeOptype node)
//     {
//         inAKopcodeOptype(node);
//         if(node.getKopcode() != null)
//         {
//             node.getKopcode().apply(this);
//         }
//         outAKopcodeOptype(node);
//     }

	public void inAIopcodeOptype(AIopcodeOptype node)
	{
		defaultIn(node);
	}

	public void outAIopcodeOptype(AIopcodeOptype node)
	{
		defaultOut(node);
	}

//     public void caseAIopcodeOptype(AIopcodeOptype node)
//     {
//         inAIopcodeOptype(node);
//         if(node.getIopcode() != null)
//         {
//             node.getIopcode().apply(this);
//         }
//         outAIopcodeOptype(node);
//     }

	public void inAOpcodeOptype(AOpcodeOptype node)
	{
		defaultIn(node);
	}

	public void outAOpcodeOptype(AOpcodeOptype node)
	{
		defaultOut(node);
	}

//     public void caseAOpcodeOptype(AOpcodeOptype node)
//     {
//         inAOpcodeOptype(node);
//         if(node.getOpcode() != null)
//         {
//             node.getOpcode().apply(this);
//         }
//         outAOpcodeOptype(node);
//     }


	public void inAAltExpr(AAltExpr node)
	{
		// TODO:
	}


	public void outAAltExpr(AAltExpr node)
	{
		// TODO:
	}


//     public void caseAAltExpr(AAltExpr node)
//     {
//         inAAltExpr(node);
//         if(node.getOrexpr1() != null)
//         {
//             node.getOrexpr1().apply(this);
//         }
//         if(node.getQuestMark() != null)
//         {
//             node.getQuestMark().apply(this);
//         }
//         if(node.getOrexpr2() != null)
//         {
//             node.getOrexpr2().apply(this);
//         }
//         if(node.getColon() != null)
//         {
//             node.getColon().apply(this);
//         }
//         if(node.getOrexpr3() != null)
//         {
//             node.getOrexpr3().apply(this);
//         }
//         outAAltExpr(node);
//     }



	public void outAOrOrexpr(AOrOrexpr node)
	{
		// TODO:
	}



	public void outAAndAndexpr(AAndAndexpr node)
	{
		// TODO:
	}



	public void outANeqEqualityexpr(ANeqEqualityexpr node)
	{
		BranchInstruction	branch = new IFNE(null);
		appendRelationalOperation(branch);
	}



	public void outAEqEqualityexpr(AEqEqualityexpr node)
	{
		BranchInstruction	branch = new IFEQ(null);
		appendRelationalOperation(branch);
	}



	public void inAGtRelationalexpr(AGtRelationalexpr node)
	{
		defaultIn(node);
	}

	public void outAGtRelationalexpr(AGtRelationalexpr node)
	{
		BranchInstruction	branch = new IFGT(null);
		appendRelationalOperation(branch);
	}



	public void outALtRelationalexpr(ALtRelationalexpr node)
	{
		BranchInstruction	branch = new IFLT(null);
		appendRelationalOperation(branch);
	}



	public void outALteqRelationalexpr(ALteqRelationalexpr node)
	{
		BranchInstruction	branch = new IFLE(null);
		appendRelationalOperation(branch);
	}



	public void outAGteqRelationalexpr(AGteqRelationalexpr node)
	{
		BranchInstruction	branch = new IFGE(null);
		appendRelationalOperation(branch);
	}



	public void outAPlusAddexpr(APlusAddexpr node)
	{
		appendInstruction(InstructionConstants.FADD);
	}



	public void outAMinusAddexpr(AMinusAddexpr node)
	{
		appendInstruction(InstructionConstants.FSUB);
	}



	public void outAMultFactor(AMultFactor node)
	{
		appendInstruction(InstructionConstants.FMULT);
	}



	public void outADivFactor(ADivFactor node)
	{
		appendInstruction(InstructionConstants.FDIV);
	}



	public void outANotUnaryminusterm(ANotUnaryminusterm node)
	{
		appendInstruction(InstructionConstants.FNEG);
	}



	public void outANotNotterm(ANotNotterm node)
	{
		appendInstruction(InstructionConstants.FCONST_0);
		appendInstruction(InstructionConstants.FCMPL);
		BranchInstruction	branch0 = new IFNE(null);
		appendInstruction(branch0);
		appendInstruction(InstructionConstants.FCONST_1);
		BranchInstruction	branch1 = new GOTO(null);
		appendInstruction(branch1);
		setPendingBranchInstruction(branch0);
		appendInstruction(InstructionConstants.FCONST_0);
		setPendingBranchInstruction(branch1);
	}



	public void outAIdentifierTerm(AIdentifierTerm node)
	{
		String	strVariableName = node.getIdentifier().getText();
		appendGetField(strVariableName);
	}


	public void outAConstantTerm(AConstantTerm node)
	{
		Object	constant = getNodeAttribute(node.getConst());
		if (constant instanceof Integer ||
		    constant instanceof Float)
		{
			float	fValue = ((Number) constant).floatValue();
			appendFloatConstant(fValue);
		}
		else
		{
			throw new RuntimeException("constant is neither int nor float");
		}
	}

//     public void caseAConstantTerm(AConstantTerm node)
//     {
//         inAConstantTerm(node);
//         if(node.getConst() != null)
//         {
//             node.getConst().apply(this);
//         }
//         outAConstantTerm(node);
//     }

	public void inAIndexedTerm(AIndexedTerm node)
	{
		defaultIn(node);
	}

	public void outAIndexedTerm(AIndexedTerm node)
	{
		defaultOut(node);
	}

//     public void caseAIndexedTerm(AIndexedTerm node)
//     {
//         inAIndexedTerm(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLBracket() != null)
//         {
//             node.getLBracket().apply(this);
//         }
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         if(node.getRBracket() != null)
//         {
//             node.getRBracket().apply(this);
//         }
//         outAIndexedTerm(node);
//     }

	public void inASasbfTerm(ASasbfTerm node)
	{
		defaultIn(node);
	}

	public void outASasbfTerm(ASasbfTerm node)
	{
		defaultOut(node);
	}

//     public void caseASasbfTerm(ASasbfTerm node)
//     {
//         inASasbfTerm(node);
//         if(node.getSasbf() != null)
//         {
//             node.getSasbf().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getExprlist() != null)
//         {
//             node.getExprlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         outASasbfTerm(node);
//     }

    public void inAFunctionTerm(AFunctionTerm node)
    {
        defaultIn(node);
    }

    public void outAFunctionTerm(AFunctionTerm node)
    {
        defaultOut(node);
    }

//     public void caseAFunctionTerm(AFunctionTerm node)
//     {
//         inAFunctionTerm(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getExprlist() != null)
//         {
//             node.getExprlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         outAFunctionTerm(node);
//     }

    public void inAIndexedfunctionTerm(AIndexedfunctionTerm node)
    {
        defaultIn(node);
    }

    public void outAIndexedfunctionTerm(AIndexedfunctionTerm node)
    {
        defaultOut(node);
    }

//     public void caseAIndexedfunctionTerm(AIndexedfunctionTerm node)
//     {
//         inAIndexedfunctionTerm(node);
//         if(node.getIdentifier() != null)
//         {
//             node.getIdentifier().apply(this);
//         }
//         if(node.getLBracket() != null)
//         {
//             node.getLBracket().apply(this);
//         }
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         if(node.getRBracket() != null)
//         {
//             node.getRBracket().apply(this);
//         }
//         if(node.getLPar() != null)
//         {
//             node.getLPar().apply(this);
//         }
//         if(node.getExprlist() != null)
//         {
//             node.getExprlist().apply(this);
//         }
//         if(node.getRPar() != null)
//         {
//             node.getRPar().apply(this);
//         }
//         outAIndexedfunctionTerm(node);
//     }


    public void inAExprlistExprlist(AExprlistExprlist node)
    {
        defaultIn(node);
    }


    public void outAExprlistExprlist(AExprlistExprlist node)
    {
        defaultOut(node);
    }


//     public void caseAExprlistExprlist(AExprlistExprlist node)
//     {
//         inAExprlistExprlist(node);
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         {
//             Object temp[] = node.getExprlistTail().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PExprlistTail) temp[i]).apply(this);
//             }
//         }
//         outAExprlistExprlist(node);
//     }

    public void inAExprlistTailExprlistTail(AExprlistTailExprlistTail node)
    {
        defaultIn(node);
    }

    public void outAExprlistTailExprlistTail(AExprlistTailExprlistTail node)
    {
        defaultOut(node);
    }

//     public void caseAExprlistTailExprlistTail(AExprlistTailExprlistTail node)
//     {
//         inAExprlistTailExprlistTail(node);
//         if(node.getComma() != null)
//         {
//             node.getComma().apply(this);
//         }
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         outAExprlistTailExprlistTail(node);
//     }

    public void inAExprstrlistExprstrlist(AExprstrlistExprstrlist node)
    {
        defaultIn(node);
    }

    public void outAExprstrlistExprstrlist(AExprstrlistExprstrlist node)
    {
        defaultOut(node);
    }

//     public void caseAExprstrlistExprstrlist(AExprstrlistExprstrlist node)
//     {
//         inAExprstrlistExprstrlist(node);
//         if(node.getExprOrString() != null)
//         {
//             node.getExprOrString().apply(this);
//         }
//         {
//             Object temp[] = node.getExprstrlistTail().toArray();
//             for(int i = 0; i < temp.length; i++)
//             {
//                 ((PExprstrlistTail) temp[i]).apply(this);
//             }
//         }
//         outAExprstrlistExprstrlist(node);
//     }

    public void inAExprstrlistTailExprstrlistTail(AExprstrlistTailExprstrlistTail node)
    {
        defaultIn(node);
    }

    public void outAExprstrlistTailExprstrlistTail(AExprstrlistTailExprstrlistTail node)
    {
        defaultOut(node);
    }

//     public void caseAExprstrlistTailExprstrlistTail(AExprstrlistTailExprstrlistTail node)
//     {
//         inAExprstrlistTailExprstrlistTail(node);
//         if(node.getComma() != null)
//         {
//             node.getComma().apply(this);
//         }
//         if(node.getExprOrString() != null)
//         {
//             node.getExprOrString().apply(this);
//         }
//         outAExprstrlistTailExprstrlistTail(node);
//     }

    public void inAExprExprOrString(AExprExprOrString node)
    {
        defaultIn(node);
    }

    public void outAExprExprOrString(AExprExprOrString node)
    {
        defaultOut(node);
    }

//     public void caseAExprExprOrString(AExprExprOrString node)
//     {
//         inAExprExprOrString(node);
//         if(node.getExpr() != null)
//         {
//             node.getExpr().apply(this);
//         }
//         outAExprExprOrString(node);
//     }

    public void inAStringExprOrString(AStringExprOrString node)
    {
        defaultIn(node);
    }

    public void outAStringExprOrString(AStringExprOrString node)
    {
        defaultOut(node);
    }

//     public void caseAStringExprOrString(AStringExprOrString node)
//     {
//         inAStringExprOrString(node);
//         if(node.getString() != null)
//         {
//             node.getString().apply(this);
//         }
//         outAStringExprOrString(node);
//     }

	public void inAIntegerConst(AIntegerConst node)
	{
	}

    public void outAIntegerConst(AIntegerConst node)
    {
		String	strInteger = node.getInteger().getText();
		Integer	integer = new Integer(strInteger);
		setNodeAttribute(node, integer);
    }

//     public void caseAIntegerConst(AIntegerConst node)
//     {
//         inAIntegerConst(node);
//         if(node.getInteger() != null)
//         {
//             node.getInteger().apply(this);
//         }
//         outAIntegerConst(node);
//     }

	public void inANumberConst(ANumberConst node)
	{
	}



	public void outANumberConst(ANumberConst node)
	{
		String	strNumber = node.getNumber().getText();
		Float	number = new Float(strNumber);
		setNodeAttribute(node, number);
	}



//     public void caseANumberConst(ANumberConst node)
//     {
//         inANumberConst(node);
//         if(node.getNumber() != null)
//         {
//             node.getNumber().apply(this);
//         }
//         outANumberConst(node);
//     }


////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////





	// helper methods



	private void setNodeAttribute(Node node, Object attribute)
	{
		m_nodeAttributes.put(node, attribute);
	}



	private Object getNodeAttribute(Node node)
	{
		return m_nodeAttributes.get(node);
	}



	private void addLocalVariable(String strVariableName)
	{
		FieldGen	fieldGen;
		fieldGen = new FieldGen(Constants.ACC_PRIVATE,
					Type.FLOAT,
					strVariableName,
					m_constantPoolGen);
		m_classGen.addField(fieldGen.getField());

	}



	/**	Append an instruction to the global Instruction list.
		If a BranchInstruction is pending, it is targetted here.
	*/
	private InstructionHandle appendInstruction(Instruction instruction)
	{
		// System.out.println("instruction: " + instruction);
		InstructionHandle	target = null;
		if (instruction instanceof BranchInstruction)
		{
			target = m_instructionList.append((BranchInstruction) instruction);
		}
		else if (instruction instanceof CompoundInstruction)
		{
			target = m_instructionList.append((CompoundInstruction) instruction);
		}
		else
		{
			target = m_instructionList.append(instruction);
		}
		if (m_pendingBranchInstruction != null)
		{
			m_pendingBranchInstruction.setTarget(target);
			m_pendingBranchInstruction = null;
		}
		return target;
	}


	/**	Set the 'pending' BranchInstruction.
		This is a mechanism to avoid NOPs. If a BranchInstruction
		has to be targeted at an instruction that is immediately
		following, but has not been generated yet, set this
		BranchInstruction as the pending BranchInstruction.
		The next instruction added to the global InstructionList
		with appendInstruction() will become the target of
		the pending BranchInstruction.
	*/
	private void setPendingBranchInstruction(BranchInstruction branchInstruction)
	{
		if (m_pendingBranchInstruction != null)
		{
			throw new RuntimeException("pending branch instruction already set");
		}
		m_pendingBranchInstruction = branchInstruction;
	}


	private void appendGetField(String strVariableName)
	{
		// System.out.println("class name: " + m_strClassName);
		// System.out.println("var name: " + strVariableName);
		appendInstruction(InstructionConstants.ALOAD_0);
		Instruction	instruction = m_instructionFactory.createGetField(m_strClassName, strVariableName, Type.FLOAT);
		appendInstruction(instruction);
	}


	/**
	   NOTE: this method does not append an ALOAD_0 instruction!
	 */
	private void appendPutField(String strVariableName)
	{
		// System.out.println("class name: " + m_strClassName);
		// System.out.println("var name: " + strVariableName);
		Instruction	instruction = m_instructionFactory.createPutField(m_strClassName, strVariableName, Type.FLOAT);
		appendInstruction(instruction);
	}


// 	private void appendIntegerConstant(int nValue)
// 	{
// 		Instruction	instruction = null;
// 		switch (nValue)
// 		{
// 		case -1:
// 			instruction = InstructionConstants.ICONST_M1;
// 			break;

// 		case 0:
// 			instruction = InstructionConstants.ICONST_0;
// 			break;

// 		case 1:
// 			instruction = InstructionConstants.ICONST_1;
// 			break;

// 		case 2:
// 			instruction = InstructionConstants.ICONST_2;
// 			break;

// 		case 3:
// 			instruction = InstructionConstants.ICONST_3;
// 			break;

// 		case 4:
// 			instruction = InstructionConstants.ICONST_4;
// 			break;

// 		case 5:
// 			instruction = InstructionConstants.ICONST_5;
// 			break;

// 		default:
// 			int	nConstantIndex = m_constantPoolGen.addInteger(nValue);
// 			instruction = new LDC(nConstantIndex);
// 		}
// 		appendInstruction(instruction);
// 	}


	private void appendFloatConstant(float fValue)
	{
		Instruction	instruction = null;
		if (fValue == 0.0)
		{
			instruction = InstructionConstants.FCONST_0;
		}
		else if (fValue == 1.0)
		{
			instruction = InstructionConstants.FCONST_1;
		}
		else if (fValue == 2.0)
		{
			instruction = InstructionConstants.FCONST_2;
		}
		else
		{
			int	nConstantIndex = m_constantPoolGen.addFloat(fValue);
			instruction = new LDC(nConstantIndex);
		}
		appendInstruction(instruction);
	}



	private void appendRelationalOperation(BranchInstruction branch0)
	{
		appendInstruction(InstructionConstants.FCMPL);
		// BranchInstruction	branch0 = new IFEQ(null);
		appendInstruction(branch0);
		appendInstruction(InstructionConstants.FCONST_0);
		BranchInstruction	branch1 = new GOTO(null);
		appendInstruction(branch1);
		setPendingBranchInstruction(branch0);
		appendInstruction(InstructionConstants.FCONST_1);
		setPendingBranchInstruction(branch1);
	}
}



/*** InstrumentCompilation.java ***/
