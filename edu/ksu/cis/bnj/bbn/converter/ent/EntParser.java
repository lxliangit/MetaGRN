/* Generated By:JJTree&JavaCC: Do not edit this line. EntParser.java */
package edu.ksu.cis.bnj.bbn.converter.ent;

/*
 * 
 * This file is part of Bayesian Network for Java (BNJ).
 *
 * BNJ is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * BNJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BNJ in LICENSE.txt file; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */

/**
 * @author: Roby Joehanes
 */
import java.util.*;
import java.io.*;

import edu.ksu.cis.bnj.bbn.*;
import edu.ksu.cis.bnj.bbn.converter.*;
import edu.ksu.cis.kdd.util.*;
import edu.ksu.cis.kdd.util.parser.*;

public class EntParser implements/*@bgen(jjtree)*/ EntParserTreeConstants,Converter, EntParserConstants {/*@bgen(jjtree)*/
  protected JJTEntParserState jjtree = new JJTEntParserState();protected Hashtable nodeCache = new Hashtable();
    protected TableList edgeCache = new TableList();
    protected Hashtable probCache = new Hashtable();

        protected static String ln = System.getProperty("line.separator"); // $NON-NLS-1$

    public EntParser()
    {
        // Do nothing, just to provide a stub
    }

        public BBNGraph load(InputStream r) throws ParseException
        {
        EntParser p = new EntParser(r);
        BBNGraph g = p.CompilationUnit();
        System.gc();
        return g;
        }

    public void save(OutputStream os, BBNGraph graph)
    {
        Writer stream = new OutputStreamWriter(os);
        try {
            stream.write("ErgoWin1.00.004ent"+ln);  // $NON-NLS-1$
            stream.write("{Network"+ln);  // $NON-NLS-1$

            // Write junk header
            stream.write(getFiller(1)+"{GraphRect 0 0 999 999}"+ln);  // $NON-NLS-1$
            stream.write(getFiller(1)+"{Font \"Courier New\" 10}"+ln);  // $NON-NLS-1$
            stream.write(getFiller(1)+"{ShowGrid T}"+ln);  // $NON-NLS-1$
            stream.write(getFiller(1)+"{SnapToGrid T}"+ln);  // $NON-NLS-1$
            stream.write(getFiller(1)+"{ShowPageBreaks T}"+ln);  // $NON-NLS-1$
            stream.write(getFiller(1)+"{Automatic T}"+ln);  // $NON-NLS-1$
            stream.write(getFiller(1)+"{RecordAll T}"+ln+ln);  // $NON-NLS-1$

            List nodes = graph.topologicalSort();
            for (Iterator i = nodes.iterator(); i.hasNext(); ) {
                BBNNode node = (BBNNode) i.next();
                if (node.isDecision()) {
                    stream.write(getFiller(1)+"{Decision"+ln); // $NON-NLS-1$
                } else if (node.isUtility()) {
                    stream.write(getFiller(1)+"{Utility"+ln); // $NON-NLS-1$
                } else {
                    stream.write(getFiller(1)+"{Node"+ln); // $NON-NLS-1$
                }

                BBNDiscreteValue dval = (BBNDiscreteValue) node.getValues();
                if (dval != null) {
                    assert (!node.isUtility());
                    stream.write(getFiller(2)+"{NStates "+dval.size()+"}"+ln); // $NON-NLS-1$ // $NON-NLS-2$
                }

                LinkedList nodeList = new LinkedList();
                List parents = node.getParents();

                arityCount = dval == null ? 1 : dval.size();
                for (Iterator j = parents.iterator(); j.hasNext(); ) {
                    BBNNode parent = (BBNNode) j.next();
                    if (!parent.isUtility()) {
                        BBNDiscreteValue pdval = (BBNDiscreteValue) parent.getValues();
                        assert (pdval != null);
                        arityCount *= pdval.size();
                        nodeList.add(parent);
                    }
                }

                if (!node.isDecision()) {
                    stream.write(getFiller(2)+"{Probabilities "+arityCount+ln); // $NON-NLS-1$ // $NON-NLS-2$
                    if (!node.isUtility()) {
                        assert (dval != null);
                        arityCount = dval.size();
                        nodeList.add(node);
                    } else {
                        assert(nodeList.size() > 0);
                        BBNNode parent = (BBNNode) nodeList.get(0);
                        BBNDiscreteValue pdval = (BBNDiscreteValue) parent.getValues();
                        arityCount = pdval.size();
                    }
                    counter = 0;
                    String cptEntries = saveCPT(nodeList, node, new Hashtable(), new StringBuffer());
                    if (!cptEntries.endsWith(ln)) cptEntries += ln;
                    stream.write(cptEntries+getFiller(2)+"}"+ln); // $NON-NLS-1$
                }

                stream.write(getFiller(2)+"{Name "+node.getName()+"}"+ln); // $NON-NLS-1$ // $NON-NLS-2$

                if (dval != null) {
                    assert (!node.isUtility());
                    stream.write(getFiller(2)+"{Labels "+dval.size()+ln); // $NON-NLS-1$
                    for (Iterator j = dval.iterator(); j.hasNext(); ) {
                        stream.write(getFiller(3)+j.next()+ln);
                    }
                    stream.write(getFiller(2)+"}"+ln); // $NON-NLS-1$
                }

                stream.write(getFiller(2)+"{Center "); // $NON-NLS-1$
                Hashtable prop = node.getProperty();
                if (prop != null && prop.get("position") != null) {  // $NON-NLS-1$
                     List pos = (List) prop.get("position"); // $NON-NLS-1$
                     if (pos.size() != 2 || !(pos.get(0) instanceof Double) || !(pos.get(1) instanceof Double)) {
                         stream.write("0 0"); // $NON-NLS-1$ // don't know about the positions
                     } else {
                         double xpos = ((Double) pos.get(0)).doubleValue();
                         double ypos = ((Double) pos.get(1)).doubleValue();
                         stream.write(Math.round(xpos)+" "+Math.round(ypos)); // $NON-NLS-1$
                     }
                } else {
                     stream.write("0 0"); // $NON-NLS-1$ // don't know about the positions
                }
                stream.write("}"+ln); // $NON-NLS-1$
                stream.write(getFiller(1)+"}"+ln+ln); // $NON-NLS-1$
            }

            for (Iterator i = nodes.iterator(); i.hasNext(); ) {
                BBNNode node = (BBNNode) i.next();
                String nodeName = node.getName();
                List parents = node.getParents();
                for (Iterator j = parents.iterator(); j.hasNext(); ) {
                    BBNNode parent = (BBNNode) j.next();
                    stream.write(getFiller(1)+"{Edge"+ln); // $NON-NLS-1$
                    stream.write(getFiller(2)+"{Parent "+parent.getName()+"}"+ln); // $NON-NLS-1$ // $NON-NLS-2$
                    stream.write(getFiller(2)+"{Child "+nodeName+"}"+ln); // $NON-NLS-1$ // $NON-NLS-2$
                    stream.write(getFiller(1)+"}"+ln+ln); // $NON-NLS-1$
                }
            }

            stream.write("}"+ln);  // $NON-NLS-1$
            stream.flush();
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
   }

   protected int counter = 0;
   protected int arityCount = 1;

   protected String saveCPT(LinkedList nodes, BBNNode curNode, Hashtable curInst, StringBuffer buffer) {
       BBNNode node = (BBNNode) nodes.removeFirst();
       String nodeName = node.getName();
       BBNDiscreteValue dval = (BBNDiscreteValue) node.getValues();

       for (Iterator i = dval.iterator(); i.hasNext(); ) {
           String value = i.next().toString();
           curInst.put(nodeName, value);
           if (nodes.size() == 0) {
               if ((counter % arityCount) == 0) {
                   buffer.append(getFiller(3));
               }
               buffer.append(curNode.query(curInst));
               counter++;
               if ((counter % arityCount) == 0) {
                   buffer.append(ln);
               } else {
                   buffer.append(" "); // $NON-NLS-1$
               }
           } else {
               saveCPT(nodes, curNode, curInst, buffer);
           }
       }

       nodes.addFirst(node);
       return buffer.toString();
   }

   protected String getFiller(int n)
   {
       String tab = "\t"; // $NON-NLS-1$
       StringBuffer buf = new StringBuffer();
       for (int i = 0; i < n; i++)
           buf.append(tab);
       return buf.toString();
   }

    public void initialize()
    {
    }

        protected BBNGraph processGraph(BBNGraph g)
        {
            if (g == null)
                throw new RuntimeException("Bug! Graph is null at EntParser.processGraph!");

        g.addNodes(nodeCache.values()); // add all nodes created so far

        for (Enumeration e = nodeCache.keys(); e.hasMoreElements(); ) {
            String nodeName = (String) e.nextElement();
            BBNNode node = (BBNNode) nodeCache.get(nodeName);

            // Construct edges
            List parents = edgeCache.get(nodeName);
            LinkedList nodeList = new LinkedList();
            if (parents != null) {
                for (Iterator i = parents.iterator(); i.hasNext(); ) {
                    String parentName = (String) i.next();
                    BBNNode parent = (BBNNode) nodeCache.get(parentName);
                    g.addEdge(parent, node);
                    if (!parent.isUtility()) nodeList.add(parent);
                }
            }
            if (!node.isUtility()) nodeList.add(node);
            if (!node.isDecision()) {
                LinkedList cptEntries = (LinkedList) probCache.get(nodeName);
                Hashtable cpt = processCPT(nodeList, cptEntries, new Hashtable(), new Hashtable());
                node.setCPF(cpt);
            }
        }

        return g;
        }

    protected Hashtable processCPT(LinkedList nodes, LinkedList values, Hashtable curInst, Hashtable cpt) {
        BBNNode node = (BBNNode) nodes.removeFirst();
        String nodeName = node.getName();
        BBNDiscreteValue dval = (BBNDiscreteValue) node.getValues();

        for (Iterator i = dval.iterator(); i.hasNext(); ) {
            String val = i.next().toString();
            curInst.put(nodeName, val);
            if (nodes.size() == 0) {
                double p = ((Double) values.removeFirst()).doubleValue();
                cpt.put(curInst.clone(), new BBNConstant(p));
            } else {
                processCPT(nodes, values, curInst, cpt);
            }
        }
        nodes.addFirst(node);
        return cpt;
    }

    /**
     * This is to reset cache.
     */
    protected void resetCache() {
        nodeCache = new Hashtable();
        edgeCache = new TableList();
        probCache = new Hashtable();
    }

    public static void help()
    {
        System.out.println("Ergo ENT Parser:  Usage is one of:");
        System.out.println("         java EntParser < inputfile");
        System.out.println("OR");
        System.out.println("         java EntParser inputfile");
    }

    public static void main (String [] args) {
        if (args.length > 1) { help(); return; }
        try {
            if (args.length == 0)
            {
                System.out.println("Ergo ENT Parser:  Reading from standard input");
                new EntParser().load(System.in);
            } else if (args.length == 1)
            {
                System.out.println("Ergo ENT Parser:  Reading from file " + args[0]);
                new EntParser().load(new FileInputStream(args[0]));
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Error!");
        }
    }

/*********************************************
 * THE ERGO ENT LANGUAGE GRAMMAR STARTS HERE *
 *********************************************/
  final public BBNGraph CompilationUnit() throws ParseException {
  BBNGraph graph = new BBNGraph();
  Token t = null;
  Hashtable property = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
      jj_consume_token(IDENTIFIER);
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case IDENTIFIER:
        case DOT:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTEGER_LITERAL:
        case IDENTIFIER:
        case DOT:
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case DOT:
            jj_consume_token(DOT);
            break;
          default:
            jj_la1[1] = jj_gen;
            ;
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case IDENTIFIER:
            jj_consume_token(IDENTIFIER);
            break;
          case INTEGER_LITERAL:
            jj_consume_token(INTEGER_LITERAL);
            break;
          default:
            jj_la1[2] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          break;
        case FLOATING_POINT_LITERAL:
          jj_consume_token(FLOATING_POINT_LITERAL);
          break;
        default:
          jj_la1[3] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
    jj_consume_token(LBRACE);
    jj_consume_token(NET);
    label_2:
    while (true) {
      if (jj_2_1(2)) {
        ;
      } else {
        break label_2;
      }
      jj_consume_token(LBRACE);
      jj_consume_token(IDENTIFIER);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTEGER_LITERAL:
          jj_consume_token(INTEGER_LITERAL);
          break;
        case STRING_LITERAL:
          jj_consume_token(STRING_LITERAL);
          break;
        case IDENTIFIER:
          jj_consume_token(IDENTIFIER);
          break;
        default:
          jj_la1[5] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTEGER_LITERAL:
        case STRING_LITERAL:
        case IDENTIFIER:
          ;
          break;
        default:
          jj_la1[6] = jj_gen;
          break label_3;
        }
      }
      jj_consume_token(RBRACE);
    }
    label_4:
    while (true) {
      Node();
      if (jj_2_2(2)) {
        ;
      } else {
        break label_4;
      }
    }
    label_5:
    while (true) {
      Edge();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_5;
      }
    }
    jj_consume_token(RBRACE);
       {if (true) return processGraph(graph);}
    throw new Error("Missing return statement in function");
  }

  final public void Node() throws ParseException {
    Token name = null, t = null;
    LinkedList values = new LinkedList();
    List cptEntries = null;
    Double xpos = null, ypos = null;
    BBNNode node = new BBNNode();
    jj_consume_token(LBRACE);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NODE:
      jj_consume_token(NODE);
      break;
    case DECISION:
      jj_consume_token(DECISION);
                                node.setType(BBNNode.DECISION);
      break;
    case UTILITY:
      jj_consume_token(UTILITY);
                                                                                node.setType(BBNNode.UTILITY);
      break;
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    label_6:
    while (true) {
      jj_consume_token(LBRACE);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NSTATES:
        jj_consume_token(NSTATES);
        jj_consume_token(INTEGER_LITERAL);
        break;
      case PROBABILITIES:
        jj_consume_token(PROBABILITIES);
        jj_consume_token(INTEGER_LITERAL);
        cptEntries = NumberList();
                     assert (!node.isDecision());
        break;
      case NAME:
        jj_consume_token(NAME);
        name = jj_consume_token(IDENTIFIER);
                   node.setName(name.image); node.setLabel(name.image);
        break;
      case LABELS:
        jj_consume_token(LABELS);
        jj_consume_token(INTEGER_LITERAL);
        label_7:
        while (true) {
          t = jj_consume_token(IDENTIFIER);
                       values.add(t.image);
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case IDENTIFIER:
            ;
            break;
          default:
            jj_la1[9] = jj_gen;
            break label_7;
          }
        }
                    assert (!node.isUtility());
                    node.setValues(new BBNDiscreteValue(values));
        break;
      case CENTER:
        jj_consume_token(CENTER);
        xpos = NumberLiteral();
        ypos = NumberLiteral();
                    Hashtable prop = new Hashtable();
                    LinkedList posList = new LinkedList();
                    posList.add(xpos); posList.add(ypos);
                    prop.put("position", posList); // $NON-NLS-1$
                    node.setProperty(prop);
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      jj_consume_token(RBRACE);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_6;
      }
    }
    jj_consume_token(RBRACE);
        if (name == null) {
            {if (true) throw new RuntimeException("Error: The node has no name!");}
        }
        nodeCache.put(name.image, node);
        if (cptEntries != null) probCache.put(name.image, cptEntries);
  }

  final public void Edge() throws ParseException {
  Token parent = null, child = null;
    jj_consume_token(LBRACE);
    jj_consume_token(EDGE);
    jj_consume_token(LBRACE);
    jj_consume_token(PARENT);
    parent = jj_consume_token(IDENTIFIER);
    jj_consume_token(RBRACE);
    jj_consume_token(LBRACE);
    jj_consume_token(CHILD);
    child = jj_consume_token(IDENTIFIER);
    jj_consume_token(RBRACE);
    jj_consume_token(RBRACE);
     edgeCache.put(child.image, parent.image);
  }

  final public String StringLiteral() throws ParseException {
    Token t;
    t = jj_consume_token(STRING_LITERAL);
       {if (true) return t.image.substring(1, t.image.length()-1);}
    throw new Error("Missing return statement in function");
  }

  final public Double NumberLiteral() throws ParseException {
  Token t;
  int sgn = 1;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case MINUS:
      jj_consume_token(MINUS);
            sgn = -1;
      break;
    default:
      jj_la1[12] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FLOATING_POINT_LITERAL:
      t = jj_consume_token(FLOATING_POINT_LITERAL);
      break;
    case INTEGER_LITERAL:
      t = jj_consume_token(INTEGER_LITERAL);
      break;
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
          {if (true) return new Double(sgn*Double.parseDouble(t.image));}
    throw new Error("Missing return statement in function");
  }

  final public List NumberList() throws ParseException {
    LinkedList list = new LinkedList();
    Double number;
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case MINUS:
        ;
        break;
      default:
        jj_la1[14] = jj_gen;
        break label_8;
      }
      number = NumberLiteral();
          list.add(number);
    }
       {if (true) return list;}
    throw new Error("Missing return statement in function");
  }

  final private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_1();
    jj_save(0, xla);
    return retval;
  }

  final private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    boolean retval = !jj_3_2();
    jj_save(1, xla);
    return retval;
  }

  final private boolean jj_3R_10() {
    if (jj_scan_token(NODE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_2() {
    if (jj_3R_9()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_11() {
    if (jj_scan_token(DECISION)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_9() {
    if (jj_scan_token(LBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_10()) {
    jj_scanpos = xsp;
    if (jj_3R_11()) {
    jj_scanpos = xsp;
    if (jj_3R_12()) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    } else if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3R_12() {
    if (jj_scan_token(UTILITY)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  final private boolean jj_3_1() {
    if (jj_scan_token(LBRACE)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    if (jj_scan_token(IDENTIFIER)) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) return false;
    return false;
  }

  public EntParserTokenManager token_source;
  JavaCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  public boolean lookingAhead = false;
  private boolean jj_semLA;
  private int jj_gen;
  final private int[] jj_la1 = new int[15];
  final private int[] jj_la1_0 = {0x44000000,0x0,0x4000000,0x44000000,0x0,0x4000000,0x4000000,0x0,0x38000,0x0,0x7c0000,0x0,0x0,0x44000000,0x44000000,};
  final private int[] jj_la1_1 = {0x1002,0x1000,0x2,0x1002,0x2,0x3,0x3,0x40,0x0,0x2,0x0,0x40,0x40000000,0x0,0x40000000,};
  final private int[] jj_la1_2 = {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
  final private JJCalls[] jj_2_rtns = new JJCalls[2];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  public EntParser(java.io.InputStream stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new EntParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public EntParser(java.io.Reader stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new EntParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public EntParser(EntParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  public void ReInit(EntParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    return (jj_scanpos.kind != kind);
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Enumeration enuma = jj_expentries.elements(); enuma.hasMoreElements();) {
        int[] oldentry = (int[])(enuma.nextElement());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.addElement(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[84];
    for (int i = 0; i < 84; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 15; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
          if ((jj_la1_2[i] & (1<<j)) != 0) {
            la1tokens[64+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 84; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

  final private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 2; i++) {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
          }
        }
        p = p.next;
      } while (p != null);
    }
    jj_rescan = false;
  }

  final private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
