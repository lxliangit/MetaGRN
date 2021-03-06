/* Generated By:JJTree&JavaCC: Do not edit this line. ArffParser.java */
package edu.ksu.cis.kdd.data.converter.arff;

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

import edu.ksu.cis.kdd.text.*;
import edu.ksu.cis.kdd.data.*;
import edu.ksu.cis.kdd.data.converter.*;
import edu.ksu.cis.kdd.util.parser.*;


public class ArffParser implements/*@bgen(jjtree)*/ ArffParserTreeConstants,Converter, ArffParserConstants {/*@bgen(jjtree)*/
  protected JJTArffParserState jjtree = new JJTArffParserState();private TableProperty property;

    public ArffParser()
    {
    }

    public void setTableProperty(TableProperty tp)
    {
        property = tp;
    }

    public void initialize() {

    }

    public Database load(InputStream r)
    {
         ArffParser p = new ArffParser(r);
         p.property = property;
         //if (p.property == null) p.property = new TupleProperty();
         return p.CompilationUnit();
    }

 // Creating a Table from array data
    public Database createTableFromArray(int [][] dataDbn){
    	Database db = new Database();
    	Table table = null;
    	Token t = null, t2 = null;
    	Attribute attr = null;
    	List ll;
    	LinkedList attributeValues = new LinkedList();
    	LinkedList sampleValueList;
    	int numGenesTotal;
    	int samples;
    	String atrNode = "node";
    	
    	try {
    		table = new Table(); 
    		table.setName("name");
    	}catch(Exception e){
    		
			e.printStackTrace();
    	}
    	
    	
    	numGenesTotal = dataDbn[0].length;
    	samples = dataDbn.length;
    	
    	// each gene has 3 levels (3 attributes). they are represent by 0,1,2
    	attributeValues.add(new Integer(0));
    	attributeValues.add(new Integer(1));
    	attributeValues.add(new Integer(2));
    	
    	// set attributes for each gene
    	for(int k=0;k<numGenesTotal;k++){
    		String name = atrNode + k;
    		attr = new Attribute(name);
    		attr.setValues(attributeValues);
    		table.addAttribute(attr);
    	}
    	
    	// read dbnData and load gene's value of each sample in to List and then add this List in to the table
    	for(int i =0;i<samples;i++){
    		sampleValueList = new LinkedList();
    		for(int j=0; j<numGenesTotal;j++){
    			Integer val = new Integer(dataDbn[i][j]);
    			sampleValueList.add(val);
    		}
    		table.addTuple(new Tuple(sampleValueList));
    	}
    	
    	db.addTable(table);
    	/*
    	String filePath = "C:\\Users\\devamuni\\Documents\\D_Result\\Input.out";
        File f1 = new File(filePath);
        try {
			OutputStream os = new FileOutputStream(f1);
			save(os, db);
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block	
			e.printStackTrace();
        }  */
    	return db;
    }
    
    public void save(OutputStream r, Database db) {
        String ln = System.getProperty("line.separator"); // $NON-NLS-1$
        Writer w = new OutputStreamWriter(r);
        try {
            List tables = db.getTables();
            for (Iterator idb = tables.iterator(); idb.hasNext(); ) {
                Table t = (Table) idb.next();
                if (!t.isFixedLength())
                    throw new RuntimeException("Tuple is not fixed length!");
                String name = t.getName();
                if (name == null || name.equals("")) // $NON-NLS-1$
                    name = "untitled"; // $NON-NLS-1$
                w.write("@RELATION "+name+ln+ln); // $NON-NLS-1$
                int ctr = 0;
                List attrs = t.getAttributes();
                for (Iterator i = attrs.iterator(); i.hasNext(); ) {
                    Attribute attr = (Attribute) i.next();
                    name = attr.getName();
                    if (name == null || name.equals("")) { // $NON-NLS-1$
                        name = "untitled"+ctr; ctr++; // $NON-NLS-1$
                    }
                    w.write("@ATTRIBUTE "+name+" "); // $NON-NLS-1$ // $NON-NLS-2$
                    switch (attr.getType()) {
                        case Attribute.DISCRETE:
                            w.write("{ "); // $NON-NLS-1$
                            for (Iterator j = attr.getValues().iterator(); j.hasNext(); ) {
                                w.write("\""+j.next().toString()+"\""); // $NON-NLS-1$ // $NON-NLS-2$
                                if (j.hasNext()) w.write(", "); // $NON-NLS-1$
                            }
                            w.write(" }"); // $NON-NLS-1$
                            break;
                        case Attribute.INTEGER:
                            w.write("integer"); // $NON-NLS-1$
                            break;
                        case Attribute.REAL:
                            w.write("real"); // $NON-NLS-1$
                            break;
                        case Attribute.STRING:
                            w.write("string"); // $NON-NLS-1$
                            break;
                    }
                    if (attr.isPrimaryKey()) {
                        w.write(" @PRIMARY"); // $NON-NLS-1$
                    } else if (attr.isReferenceKey()) {
                        w.write(" @REFERENCE "+attr.getReferencedTableName()+ // $NON-NLS-1$
                            "."+attr.getReferencedAttributeName()); // $NON-NLS-1$
                    }
                    w.write(ln);
                }

                w.write(ln+"@DATA"+ln); // $NON-NLS-1$
                for (Iterator i = t.getTuples().iterator(); i.hasNext(); ) {
                    Tuple tuple = (Tuple) i.next();
                    int idx = 0;
                    for (Iterator j = tuple.getValues().iterator(); j.hasNext(); idx++) {
                        Object val = j.next();
                        if (((Attribute) attrs.get(idx)).isNumeric())
                            w.write(val.toString());
                        else w.write("\""+val+"\""); // $NON-NLS-1$ // $NON-NLS-2$
                        if (j.hasNext()) w.write(", "); // $NON-NLS-1$
                    }
                    w.write(ln);
                }
                w.write(ln);
            }
            w.write(ln);
            w.flush();
            w.close();
        } catch(Exception e) {
            try {
                w.close();
            } catch (Exception ee) {
            }
            throw new RuntimeException(e);
        }
    }

    public static void help()
    {
        System.out.println("Arff Parser:  Usage is one of:");
        System.out.println("         java ArffParser < inputfile");
        System.out.println("OR");
        System.out.println("         java ArffParser inputfile");
    }

    public static void main (String [] args) {
        if (args.length > 1) { help(); return; }
        try {
            if (args.length == 0)
            {
                System.out.println("Arff Parser:  Reading from standard input");
                new ArffParser().load(System.in);
            } else if (args.length == 1)
            {
                System.out.println("Arff Parser:  Reading from file " + args[0]);
                new ArffParser().load(new FileInputStream(args[0]));
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Error!");
        }
    }

  final public Database CompilationUnit() throws ParseException {
  Database db = new Database();
  Table table;
  Token t = null, t2 = null;
  Attribute attr = null;
  List ll;
  Integer _i;
  Double _d;
    label_1:
    while (true) {
      jj_consume_token(RELATION);
      t = jj_consume_token(IDENTIFIER);
                                    table = new Table(); table.setName(t.image);
      label_2:
      while (true) {
        jj_consume_token(ATTRIBUTE);
        t = jj_consume_token(IDENTIFIER);
                                    attr = new Attribute(t.image);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTEGER:
          jj_consume_token(INTEGER);
                      attr.setType(Attribute.INTEGER);
          break;
        case REAL:
          jj_consume_token(REAL);
                   attr.setType(Attribute.REAL);
          break;
        case STRING:
          jj_consume_token(STRING);
                     attr.setType(Attribute.STRING);
          break;
        case LBRACE:
          jj_consume_token(LBRACE);
          ll = Discrete();
          jj_consume_token(RBRACE);
                                    attr.setValues(ll);
          break;
        default:
          jj_la1[0] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
         table.addAttribute(attr);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case PRIMARY:
        case REFERENCE:
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case PRIMARY:
            jj_consume_token(PRIMARY);
                       attr.setPrimaryKey();
            break;
          case REFERENCE:
            jj_consume_token(REFERENCE);
            t = jj_consume_token(IDENTIFIER);
            jj_consume_token(DOT);
            t2 = jj_consume_token(IDENTIFIER);
             attr.setReference(t.image, t2.image);
            break;
          default:
            jj_la1[1] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          break;
        default:
          jj_la1[2] = jj_gen;
          ;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ATTRIBUTE:
          ;
          break;
        default:
          jj_la1[3] = jj_gen;
          break label_2;
        }
      }
      jj_consume_token(DATA);
      label_3:
      while (true) {
        ll = Discrete();
        if (ll.size() != table.getAttributes().size())
           {if (true) throw new RuntimeException("data size is not equal to the attribute size.");}
        LinkedList newll = new LinkedList();
        int max = ll.size();
        for (int i = 0; i < max; i++)
        {
           attr = (Attribute) table.getAttributes().get(i);
           String val = (String) ll.get(i);
           if (attr.isNumeric())
           {
               try {
                  _i = new Integer(val);
                  newll.add(_i);
               } catch (Exception e)
               {
                  try {
                     _d = new Double(val);
                     newll.add(_d);
                  } catch (NumberFormatException ee)
                  {
                     {if (true) throw ee;}
                  }
               }
           } else
           {
              List values = attr.getValues();
              int idx = values.indexOf(val);
              if (idx == -1) {
                 if (attr.isString()) {
                     idx = values.size();
                     values.add(val);
                 } else {if (true) throw new RuntimeException("Value "+val+" is not found.");}
              }
              newll.add(new Integer(idx));
           }
        }
        table.addTuple(new Tuple(ll));
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTEGER_LITERAL:
        case FLOATING_POINT_LITERAL:
        case CHARACTER_LITERAL:
        case STRING_LITERAL:
        case IDENTIFIER:
        case MINUS:
          ;
          break;
        default:
          jj_la1[4] = jj_gen;
          break label_3;
        }
      }
      table.setClassAttribute(table.getAttributes().size()-1);
      db.addTable(table);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case RELATION:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_1;
      }
    }
      {if (true) return db;}
    throw new Error("Missing return statement in function");
  }

  final public List Discrete() throws ParseException {
   Token t;
   LinkedList ll = new LinkedList();
   boolean isNegative = false;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CHARACTER_LITERAL:
      t = jj_consume_token(CHARACTER_LITERAL);
      break;
    case STRING_LITERAL:
      t = jj_consume_token(STRING_LITERAL);
      break;
    case IDENTIFIER:
      t = jj_consume_token(IDENTIFIER);
      break;
    case INTEGER_LITERAL:
    case FLOATING_POINT_LITERAL:
    case MINUS:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MINUS:
        jj_consume_token(MINUS);
                                                                                isNegative = true;
        break;
      default:
        jj_la1[6] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INTEGER_LITERAL:
        t = jj_consume_token(INTEGER_LITERAL);
        break;
      case FLOATING_POINT_LITERAL:
        t = jj_consume_token(FLOATING_POINT_LITERAL);
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
         if (isNegative) { t.image = "-"+t.image; }
         ll.add(t.image); isNegative = false;
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[9] = jj_gen;
        break label_4;
      }
      jj_consume_token(COMMA);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CHARACTER_LITERAL:
        t = jj_consume_token(CHARACTER_LITERAL);
        break;
      case STRING_LITERAL:
        t = jj_consume_token(STRING_LITERAL);
        break;
      case IDENTIFIER:
        t = jj_consume_token(IDENTIFIER);
        break;
      case INTEGER_LITERAL:
      case FLOATING_POINT_LITERAL:
      case MINUS:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MINUS:
          jj_consume_token(MINUS);
                                                                                      isNegative = true;
          break;
        default:
          jj_la1[10] = jj_gen;
          ;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case INTEGER_LITERAL:
          t = jj_consume_token(INTEGER_LITERAL);
          break;
        case FLOATING_POINT_LITERAL:
          t = jj_consume_token(FLOATING_POINT_LITERAL);
          break;
        default:
          jj_la1[11] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_la1[12] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
         if (isNegative) { t.image = "-"+t.image; }
         ll.add(t.image); isNegative = false;
    }
      {if (true) return ll;}
    throw new Error("Missing return statement in function");
  }

  public ArffParserTokenManager token_source;
  JavaCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[13];
  final private int[] jj_la1_0 = {0x1c000,0x300000,0x300000,0x40000,0x34400000,0x20000,0x0,0x4400000,0x34400000,0x0,0x0,0x4400000,0x34400000,};
  final private int[] jj_la1_1 = {0x200,0x0,0x0,0x0,0x10,0x0,0x0,0x0,0x10,0x4000,0x0,0x0,0x10,};
  final private int[] jj_la1_2 = {0x0,0x0,0x0,0x0,0x2,0x0,0x2,0x0,0x2,0x0,0x2,0x0,0x2,};

  public ArffParser(java.io.InputStream stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new ArffParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public ArffParser(java.io.Reader stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new ArffParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public ArffParser(ArffParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public void ReInit(ArffParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
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

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[87];
    for (int i = 0; i < 87; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 13; i++) {
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
    for (int i = 0; i < 87; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
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

}
