/* Generated By:JJTree&JavaCC: Do not edit this line. DscParserConstants.java */
package edu.ksu.cis.bnj.bbn.converter.dsc;

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
public interface DscParserConstants {

  int EOF = 0;
  int SINGLE_LINE_COMMENT = 8;
  int MULTI_LINE_COMMENT = 9;
  int NETWORK = 11;
  int NODE = 12;
  int TYPE = 13;
  int PROPERTIES = 14;
  int ARRAY = 15;
  int OF = 16;
  int STRING = 17;
  int REAL = 18;
  int CHOICE = 19;
  int SERVICE = 20;
  int NAME = 21;
  int POSITION = 22;
  int PROBABILITY = 23;
  int FUNCTION = 24;
  int DISCRETE = 25;
  int UTILITY = 26;
  int DECISION = 27;
  int _DEFAULT = 28;
  int INTEGER_LITERAL = 29;
  int DECIMAL_LITERAL = 30;
  int HEX_LITERAL = 31;
  int OCTAL_LITERAL = 32;
  int FLOATING_POINT_LITERAL = 33;
  int EXPONENT = 34;
  int CHARACTER_LITERAL = 35;
  int STRING_LITERAL = 36;
  int NON_ESCAPED_LITERAL = 38;
  int ESCAPE_SEQ = 39;
  int ESCAPE_OCTAL = 40;
  int ESCAPE_UNICODE = 41;
  int IDENTIFIER = 43;
  int LETTER = 44;
  int DIGIT = 45;
  int LPAREN = 46;
  int RPAREN = 47;
  int LBRACE = 48;
  int RBRACE = 49;
  int LBRACKET = 50;
  int RBRACKET = 51;
  int SEMICOLON = 52;
  int COMMA = 53;
  int DOT = 54;
  int ASSIGN = 55;
  int GT = 56;
  int LT = 57;
  int BANG = 58;
  int TILDE = 59;
  int HOOK = 60;
  int COLON = 61;
  int EQ = 62;
  int LE = 63;
  int GE = 64;
  int NE = 65;
  int SC_OR = 66;
  int SC_AND = 67;
  int SC_XOR = 68;
  int INCR = 69;
  int DECR = 70;
  int PLUS = 71;
  int MINUS = 72;
  int STAR = 73;
  int SLASH = 74;
  int BIT_AND = 75;
  int BIT_OR = 76;
  int BIT_XOR = 77;
  int LSHIFT = 78;
  int RSIGNEDSHIFT = 79;
  int RUNSIGNEDSHIFT = 80;
  int PLUSASSIGN = 81;
  int MINUSASSIGN = 82;
  int STARASSIGN = 83;
  int SLASHASSIGN = 84;
  int ANDASSIGN = 85;
  int ORASSIGN = 86;
  int XORASSIGN = 87;
  int REMASSIGN = 88;
  int LSHIFTASSIGN = 89;
  int RSIGNEDSHIFTASSIGN = 90;
  int RUNSIGNEDSHIFTASSIGN = 91;
  int TOWARD = 92;
  int ASG_OPR = 93;

  int DEFAULT = 0;
  int IN_SINGLE_LINE_COMMENT = 1;
  int IN_MULTI_LINE_COMMENT = 2;
  int CHAR_LIT = 3;
  int ESCAPED_CHAR_LIT = 4;
  int CHAR_LIT_CLOSE = 5;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "\"//\"",
    "\"/*\"",
    "<SINGLE_LINE_COMMENT>",
    "\"*/\"",
    "<token of kind 10>",
    "\"network\"",
    "\"node\"",
    "\"type\"",
    "\"properties\"",
    "\"array\"",
    "\"of\"",
    "\"string\"",
    "\"real\"",
    "\"choice\"",
    "\"service\"",
    "\"name\"",
    "\"position\"",
    "\"probability\"",
    "\"function\"",
    "\"discrete\"",
    "\"utility\"",
    "\"decision\"",
    "\"default\"",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<OCTAL_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "\"\\\'\"",
    "<STRING_LITERAL>",
    "\"\\\\\"",
    "<NON_ESCAPED_LITERAL>",
    "<ESCAPE_SEQ>",
    "<ESCAPE_OCTAL>",
    "<ESCAPE_UNICODE>",
    "\"\\\'\"",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"=\"",
    "\">\"",
    "\"<\"",
    "\"!\"",
    "\"~\"",
    "\"?\"",
    "\":\"",
    "\"==\"",
    "\"<=\"",
    "\">=\"",
    "\"!=\"",
    "\"||\"",
    "\"&&\"",
    "\"^^\"",
    "\"++\"",
    "\"--\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"&\"",
    "\"|\"",
    "\"^\"",
    "\"<<\"",
    "\">>\"",
    "\">>>\"",
    "\"+=\"",
    "\"-=\"",
    "\"*=\"",
    "\"/=\"",
    "\"&=\"",
    "\"|=\"",
    "\"^=\"",
    "\"%=\"",
    "\"<<=\"",
    "\">>=\"",
    "\">>>=\"",
    "\"->\"",
    "<ASG_OPR>",
  };

}
