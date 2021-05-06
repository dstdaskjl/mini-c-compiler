//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 15 "Parser.y"
import java.io.*;
import java.util.ArrayList;
//#line 20 "Parser.java"




public class Parser
             extends ParserImpl
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ASSIGN=257;
public final static short OP_OR=258;
public final static short OP_AND=259;
public final static short RELOP_EQ=260;
public final static short RELOP_NE=261;
public final static short RELOP_LE=262;
public final static short RELOP_LT=263;
public final static short RELOP_GE=264;
public final static short RELOP_GT=265;
public final static short OP_ADD=266;
public final static short OP_SUB=267;
public final static short OP_MUL=268;
public final static short OP_DIV=269;
public final static short OP_MOD=270;
public final static short OP_NOT=271;
public final static short IDENT=272;
public final static short INT_LIT=273;
public final static short FLOAT_LIT=274;
public final static short BOOL_LIT=275;
public final static short BOOL=276;
public final static short INT=277;
public final static short FLOAT=278;
public final static short IF=279;
public final static short ELSE=280;
public final static short NEW=281;
public final static short PRINT=282;
public final static short WHILE=283;
public final static short RETURN=284;
public final static short CONTINUE=285;
public final static short BREAK=286;
public final static short LPAREN=287;
public final static short RPAREN=288;
public final static short LBRACE=289;
public final static short RBRACE=290;
public final static short LBRACKET=291;
public final static short RBRACKET=292;
public final static short STRUCT=293;
public final static short SIZE=294;
public final static short SEMI=295;
public final static short COMMA=296;
public final static short DOT=297;
public final static short ADDR=298;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    2,    3,    4,    4,    4,
    4,    4,    5,    6,    7,    7,    8,    8,    9,   10,
   10,   11,   11,   11,   11,   11,   11,   11,   12,   12,
   12,   12,   13,   14,   15,   15,   16,   17,   18,   19,
   20,   20,   21,   21,   22,   22,   22,   22,   22,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   22,   22,   22,   22,   22,   22,   22,
};
final static short yylen[] = {                            2,
    1,    2,    0,    1,    1,    1,    3,    1,    1,    1,
    2,    3,    6,    9,    1,    0,    3,    1,    2,    2,
    0,    2,    1,    1,    1,    2,    2,    1,    3,    1,
    6,    5,    5,    4,    2,    0,    3,    7,    2,    2,
    3,    1,    1,    0,    3,    3,    3,    3,    3,    3,
    3,    2,    3,    3,    3,    3,    3,    3,    3,    1,
    1,    1,    1,    4,    4,    3,    3,
};
final static short yydefred[] = {                         3,
    0,    0,    8,    9,   10,    0,    2,    4,    0,    5,
    6,    0,    0,    0,   36,    0,    7,   12,    0,    0,
    0,    0,    0,   18,    0,    0,   35,   11,   19,    0,
    0,   13,    0,   36,   17,   37,    0,    0,    0,    0,
   62,   63,   61,    0,    0,    0,    0,    0,   36,   14,
   28,   20,    0,   25,   23,   24,    0,    0,    0,    0,
   52,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   22,   26,   27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   29,
    0,    0,    0,    0,    0,   66,    0,    0,   59,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   47,   48,   49,    0,   67,    0,   64,    0,    0,    0,
    0,   34,   65,    0,    0,   32,    0,   33,   31,    0,
   38,
};
final static short yydgoto[] = {                          1,
    2,    7,    8,   26,   10,   11,   22,   23,   24,   38,
   52,   53,   54,   55,   19,   27,   56,   57,   58,   91,
   92,   59,
};
final static short yysindex[] = {                         0,
    0,   -5,    0,    0,    0, -270,    0,    0, -256,    0,
    0, -284, -280, -259,    0,   -2,    0,    0,   -8, -213,
 -216, -226, -204,    0, -201, -215,    0,    0,    0, -184,
   -2,    0, -188,    0,    0,    0,   -2,  147,   49, -237,
    0,    0,    0, -178,   49, -176,   49,   49,    0,    0,
    0,    0, -182,    0,    0,    0, -160, -145,  252, -236,
    0,  222,   49,   49, -242,   49,  252,   49,  252,   96,
   -2,    0,    0,    0,   49,   49,   49,   49,   49,   49,
   49,   49,   49,   49,   49,   49,   49,   49, -241,    0,
 -144, -174,  252,   48, -104,    0,  127,  140,    0,  172,
  264,  238,  273,  273,   59,   59,   59,   59, -124, -124,
    0,    0,    0,   83,    0,   49,    0, -101,  222, -261,
 -261,    0,    0,  252,  222,    0, -121,    0,    0, -261,
    0,
};
final static short yyrindex[] = {                         0,
    0,  160,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -181,    0,    0,    0, -126,    0,    0,    0,    0,
    0,    0, -125,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  197,    0,    0,  -16,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -128, -180,
    0,    0, -120,    0,    0,    0, -123,    0, -112,    0,
  197,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -119,    0, -192,    0,   -3,    0,    0,    0,    0,    0,
 -195,  -55,  -65,  -60, -131, -122,  -83,  -74, -193, -141,
    0,    0,    0,    0,    0,    0,    0,   35,    0,    0,
    0,    0,    0, -190,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,    1,    0,    0,    0,    0,  161,  100,
   28,  -61,    0,    0,  -30,    0,    0,    0,    0,    0,
    0,  -39,
};
final static int YYTABLESIZE=543;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         61,
   90,   12,    9,   37,   15,   67,   16,   69,   70,   39,
   40,   41,   42,   43,   17,   13,   21,   44,   71,   62,
   45,   46,   47,   93,   94,   48,   97,   49,   98,   95,
  115,   21,   18,   51,   14,  101,  102,  103,  104,  105,
  106,  107,  108,  109,  110,  111,  112,  113,  114,   63,
   63,   96,   96,   64,   88,   29,   33,  126,   28,   65,
   89,   30,   50,  129,   45,   45,   45,   45,   45,   45,
   45,   45,   45,   45,   14,   14,  124,   60,   60,   60,
   60,   60,   60,   60,   60,   60,   60,   60,   60,   60,
   11,   31,   50,   32,   45,   42,   50,   41,   45,   50,
   50,   45,   45,   42,   34,   41,   36,   60,   66,   11,
   68,   60,   72,  117,   60,   60,   46,   46,   46,   46,
   46,   46,   46,   46,   46,   46,   55,   55,   55,   55,
   55,   55,   55,   55,   73,   56,   56,   56,   56,   56,
   56,   56,   56,   85,   86,   87,   46,  127,  128,   74,
   46,  116,  119,   46,   46,  125,   55,  131,  130,    1,
   55,   16,   15,   55,   55,   56,   30,   44,   43,   56,
  100,   40,   56,   56,   57,   57,   57,   57,   57,   57,
   57,   57,   39,   58,   58,   58,   58,   58,   58,   58,
   58,   35,   53,   53,   53,   53,    0,   54,   54,   54,
   54,    0,   51,   51,   57,    0,    0,    0,   57,    0,
    0,   57,   57,   58,    0,    0,    0,   58,    0,    0,
   58,   58,   53,    0,    0,    0,   53,   54,    0,   53,
   53,   54,   51,    0,   54,   54,   51,    0,    0,   51,
   51,   60,   60,   60,   60,   60,   60,   60,   60,   60,
   60,   60,   60,   60,   67,   67,   67,   67,   67,   67,
   67,   67,   67,   67,   67,   67,   67,    3,    4,    5,
    3,    4,    5,    3,    4,    5,    0,    0,   60,    0,
    0,   25,    0,    0,   20,    0,    0,    6,    0,    0,
   20,   67,   65,   65,   65,   65,   65,   65,   65,   65,
   65,   65,   65,   65,   65,   75,   76,   77,   78,   79,
   80,   81,   82,   83,   84,   85,   86,   87,    0,   39,
   60,   41,   42,   43,   83,   84,   85,   86,   87,   65,
    0,    0,    0,    0,    0,   48,    0,    0,    0,  118,
   75,   76,   77,   78,   79,   80,   81,   82,   83,   84,
   85,   86,   87,   75,   76,   77,   78,   79,   80,   81,
   82,   83,   84,   85,   86,   87,    0,    0,    0,    0,
    0,    0,    0,    0,  123,    0,    0,    0,    0,    0,
    0,    0,    0,   99,   75,   76,   77,   78,   79,   80,
   81,   82,   83,   84,   85,   86,   87,   75,   76,   77,
   78,   79,   80,   81,   82,   83,   84,   85,   86,   87,
    0,    0,    0,    0,  120,    0,    0,   39,   40,   41,
   42,   43,    0,    0,    0,   44,    0,  121,   45,   46,
   47,    0,    0,   48,    0,   49,   50,    0,    0,    0,
    0,   51,   39,   40,   41,   42,   43,    0,    0,    0,
   44,    0,    0,   45,   46,   47,    0,    0,   48,    0,
   49,  122,    0,    0,    0,    0,   51,   21,   21,   21,
   21,   21,    0,    0,    0,   21,    0,    0,   21,   21,
   21,    0,    0,   21,    0,   21,   21,    0,    0,    0,
    0,   21,   39,   40,   41,   42,   43,   77,   78,   79,
   80,   81,   82,   83,   84,   85,   86,   87,   48,   75,
   76,   77,   78,   79,   80,   81,   82,   83,   84,   85,
   86,   87,   76,   77,   78,   79,   80,   81,   82,   83,
   84,   85,   86,   87,   79,   80,   81,   82,   83,   84,
   85,   86,   87,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         39,
   62,  272,    2,   34,  289,   45,  287,   47,   48,  271,
  272,  273,  274,  275,  295,  272,   16,  279,   49,  257,
  282,  283,  284,   63,   64,  287,   66,  289,   68,  272,
  272,   31,  292,  295,  291,   75,   76,   77,   78,   79,
   80,   81,   82,   83,   84,   85,   86,   87,   88,  287,
  287,  294,  294,  291,  291,  272,  272,  119,  272,  297,
  297,  288,  258,  125,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  291,  291,  116,  258,  259,  260,
  261,  262,  263,  264,  265,  266,  267,  268,  269,  270,
  272,  296,  288,  295,  288,  288,  292,  288,  292,  295,
  296,  295,  296,  296,  289,  296,  295,  288,  287,  291,
  287,  292,  295,  288,  295,  296,  258,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  258,  259,  260,  261,
  262,  263,  264,  265,  295,  258,  259,  260,  261,  262,
  263,  264,  265,  268,  269,  270,  288,  120,  121,  295,
  292,  296,  257,  295,  296,  257,  288,  130,  280,    0,
  292,  288,  288,  295,  296,  288,  295,  288,  288,  292,
   71,  295,  295,  296,  258,  259,  260,  261,  262,  263,
  264,  265,  295,  258,  259,  260,  261,  262,  263,  264,
  265,   31,  258,  259,  260,  261,   -1,  258,  259,  260,
  261,   -1,  258,  259,  288,   -1,   -1,   -1,  292,   -1,
   -1,  295,  296,  288,   -1,   -1,   -1,  292,   -1,   -1,
  295,  296,  288,   -1,   -1,   -1,  292,  288,   -1,  295,
  296,  292,  288,   -1,  295,  296,  292,   -1,   -1,  295,
  296,  258,  259,  260,  261,  262,  263,  264,  265,  266,
  267,  268,  269,  270,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  270,  276,  277,  278,
  276,  277,  278,  276,  277,  278,   -1,   -1,  295,   -1,
   -1,  290,   -1,   -1,  293,   -1,   -1,  293,   -1,   -1,
  293,  295,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  258,  259,  260,  261,  262,
  263,  264,  265,  266,  267,  268,  269,  270,   -1,  271,
  272,  273,  274,  275,  266,  267,  268,  269,  270,  295,
   -1,   -1,   -1,   -1,   -1,  287,   -1,   -1,   -1,  292,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  258,  259,  260,  261,  262,  263,  264,
  265,  266,  267,  268,  269,  270,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  292,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  288,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  270,  258,  259,  260,
  261,  262,  263,  264,  265,  266,  267,  268,  269,  270,
   -1,   -1,   -1,   -1,  288,   -1,   -1,  271,  272,  273,
  274,  275,   -1,   -1,   -1,  279,   -1,  288,  282,  283,
  284,   -1,   -1,  287,   -1,  289,  290,   -1,   -1,   -1,
   -1,  295,  271,  272,  273,  274,  275,   -1,   -1,   -1,
  279,   -1,   -1,  282,  283,  284,   -1,   -1,  287,   -1,
  289,  290,   -1,   -1,   -1,   -1,  295,  271,  272,  273,
  274,  275,   -1,   -1,   -1,  279,   -1,   -1,  282,  283,
  284,   -1,   -1,  287,   -1,  289,  290,   -1,   -1,   -1,
   -1,  295,  271,  272,  273,  274,  275,  260,  261,  262,
  263,  264,  265,  266,  267,  268,  269,  270,  287,  258,
  259,  260,  261,  262,  263,  264,  265,  266,  267,  268,
  269,  270,  259,  260,  261,  262,  263,  264,  265,  266,
  267,  268,  269,  270,  262,  263,  264,  265,  266,  267,
  268,  269,  270,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=298;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"ASSIGN","OP_OR","OP_AND","RELOP_EQ","RELOP_NE","RELOP_LE",
"RELOP_LT","RELOP_GE","RELOP_GT","OP_ADD","OP_SUB","OP_MUL","OP_DIV","OP_MOD",
"OP_NOT","IDENT","INT_LIT","FLOAT_LIT","BOOL_LIT","BOOL","INT","FLOAT","IF",
"ELSE","NEW","PRINT","WHILE","RETURN","CONTINUE","BREAK","LPAREN","RPAREN",
"LBRACE","RBRACE","LBRACKET","RBRACKET","STRUCT","SIZE","SEMI","COMMA","DOT",
"ADDR",
};
final static String yyrule[] = {
"$accept : program",
"program : decl_list",
"decl_list : decl_list decl",
"decl_list :",
"decl : var_decl",
"decl : struc_decl",
"decl : fun_decl",
"var_decl : type_spec IDENT SEMI",
"type_spec : BOOL",
"type_spec : INT",
"type_spec : FLOAT",
"type_spec : STRUCT IDENT",
"type_spec : type_spec LBRACKET RBRACKET",
"struc_decl : STRUCT IDENT LBRACE local_decls RBRACE SEMI",
"fun_decl : type_spec IDENT LPAREN params RPAREN LBRACE local_decls stmt_list RBRACE",
"params : param_list",
"params :",
"param_list : param_list COMMA param",
"param_list : param",
"param : type_spec IDENT",
"stmt_list : stmt_list stmt",
"stmt_list :",
"stmt : expr_stmt SEMI",
"stmt : compound_stmt",
"stmt : if_stmt",
"stmt : while_stmt",
"stmt : return_stmt SEMI",
"stmt : print_stmt SEMI",
"stmt : SEMI",
"expr_stmt : IDENT ASSIGN expr_stmt",
"expr_stmt : expr",
"expr_stmt : IDENT LBRACKET expr RBRACKET ASSIGN expr_stmt",
"expr_stmt : IDENT DOT IDENT ASSIGN expr_stmt",
"while_stmt : WHILE LPAREN expr RPAREN stmt",
"compound_stmt : LBRACE local_decls stmt_list RBRACE",
"local_decls : local_decls local_decl",
"local_decls :",
"local_decl : type_spec IDENT SEMI",
"if_stmt : IF LPAREN expr RPAREN stmt ELSE stmt",
"return_stmt : RETURN expr",
"print_stmt : PRINT expr",
"arg_list : arg_list COMMA expr",
"arg_list : expr",
"args : arg_list",
"args :",
"expr : expr OP_ADD expr",
"expr : expr OP_SUB expr",
"expr : expr OP_MUL expr",
"expr : expr OP_DIV expr",
"expr : expr OP_MOD expr",
"expr : expr OP_OR expr",
"expr : expr OP_AND expr",
"expr : OP_NOT expr",
"expr : expr RELOP_EQ expr",
"expr : expr RELOP_NE expr",
"expr : expr RELOP_LE expr",
"expr : expr RELOP_LT expr",
"expr : expr RELOP_GE expr",
"expr : expr RELOP_GT expr",
"expr : LPAREN expr RPAREN",
"expr : IDENT",
"expr : BOOL_LIT",
"expr : INT_LIT",
"expr : FLOAT_LIT",
"expr : IDENT LPAREN args RPAREN",
"expr : IDENT LBRACKET expr RBRACKET",
"expr : IDENT DOT SIZE",
"expr : IDENT DOT IDENT",
};

//#line 162 "Parser.y"
    public Lexer lexer;

    private int yylex () {
        int yyl_return = -1;
        try {
            yylval = new ParserVal(0);
            yyl_return = lexer.yylex();
        }
        catch (IOException e) {
            System.out.println("IO error :"+e);
        }
        return yyl_return;
    }

    public void yyerror (String error) {
        System.out.println ("Error: " + error);
    }

    public Parser(Reader r) {
        lexer = new Lexer(r, this);
    }
//#line 450 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
throws Exception
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 48 "Parser.y"
{ yyval.obj = program____decllist(val_peek(0).obj); }
break;
case 2:
//#line 51 "Parser.y"
{ yyval.obj = decllist____decllist_decl(val_peek(1).obj, val_peek(0).obj); }
break;
case 3:
//#line 52 "Parser.y"
{ yyval.obj = decllist____eps(); }
break;
case 4:
//#line 55 "Parser.y"
{ yyval.obj = decl____vardecl(val_peek(0).obj); }
break;
case 5:
//#line 56 "Parser.y"
{ yyval.obj = decl____strucdecl(val_peek(0).obj); }
break;
case 6:
//#line 57 "Parser.y"
{ yyval.obj = decl____fundecl(val_peek(0).obj); }
break;
case 7:
//#line 60 "Parser.y"
{ yyval.obj = vardecl____typespec_IDENT_SEMI(val_peek(2).obj, val_peek(1).obj); }
break;
case 8:
//#line 63 "Parser.y"
{ yyval.obj = typespec____BOOL(); }
break;
case 9:
//#line 64 "Parser.y"
{ yyval.obj = typespec____INT(); }
break;
case 10:
//#line 65 "Parser.y"
{ yyval.obj = typespec____FLOAT(); }
break;
case 11:
//#line 66 "Parser.y"
{ yyval.obj = typespec____STRUCT_IDENT(val_peek(0).obj); }
break;
case 12:
//#line 67 "Parser.y"
{ yyval.obj = typespec____typespec_LBRACKET_RBRACKET(val_peek(2).obj); }
break;
case 13:
//#line 70 "Parser.y"
{ yyval.obj = strucdecl____STRUCT_IDENT_LBRACE_localdecls_RBRACE_SEMI(val_peek(4).obj, val_peek(2).obj); }
break;
case 14:
//#line 73 "Parser.y"
{ yyval.obj = fundecl____typespec_IDENT_LPAREN_params_RPAREN_LBRACE_localdecls_stmtlist_RBRACE(val_peek(8).obj, val_peek(7).obj, val_peek(5).obj, val_peek(2).obj, val_peek(1).obj); }
break;
case 15:
//#line 76 "Parser.y"
{ yyval.obj = params____paramlist(val_peek(0).obj); }
break;
case 16:
//#line 77 "Parser.y"
{ yyval.obj = params____eps(); }
break;
case 17:
//#line 80 "Parser.y"
{ yyval.obj = paramlist____paramlist_COMMA_param(val_peek(2).obj, val_peek(0).obj); }
break;
case 18:
//#line 81 "Parser.y"
{ yyval.obj = paramlist____param(val_peek(0).obj); }
break;
case 19:
//#line 84 "Parser.y"
{ yyval.obj = param____typespec_IDENT(val_peek(1).obj, val_peek(0).obj); }
break;
case 20:
//#line 87 "Parser.y"
{ yyval.obj = stmtlist____stmtlist_stmt(val_peek(1).obj, val_peek(0).obj); }
break;
case 21:
//#line 88 "Parser.y"
{ yyval.obj = stmtlist____eps(); }
break;
case 22:
//#line 91 "Parser.y"
{ yyval.obj = stmt____exprstmt_SEMI(val_peek(1).obj); }
break;
case 23:
//#line 92 "Parser.y"
{ yyval.obj = stmt____compoundstmt(val_peek(0).obj); }
break;
case 24:
//#line 93 "Parser.y"
{ yyval.obj = stmt____ifstmt(val_peek(0).obj); }
break;
case 25:
//#line 94 "Parser.y"
{ yyval.obj = stmt____whilestmt(val_peek(0).obj); }
break;
case 26:
//#line 95 "Parser.y"
{ yyval.obj = stmt____returnstmt_SEMI(val_peek(1).obj); }
break;
case 27:
//#line 96 "Parser.y"
{ yyval.obj = stmt____printstmt_SEMI(val_peek(1).obj); }
break;
case 28:
//#line 97 "Parser.y"
{ yyval.obj = stmt____SEMI(); }
break;
case 29:
//#line 100 "Parser.y"
{ yyval.obj = exprstmt____IDENT_ASSIGN_exprstmt(val_peek(2).obj, val_peek(0).obj); }
break;
case 30:
//#line 101 "Parser.y"
{ yyval.obj = exprstmt____expr(val_peek(0).obj); }
break;
case 31:
//#line 102 "Parser.y"
{ yyval.obj = exprstmt____IDENT_LBRACKET_expr_RBRACKET_ASSIGN_exprstmt(val_peek(5).obj, val_peek(3).obj, val_peek(0).obj); }
break;
case 32:
//#line 103 "Parser.y"
{ yyval.obj = exprstmt____IDENT_DOT_IDENT_ASSIGN_exprstmt(val_peek(4).obj, val_peek(2).obj, val_peek(0).obj); }
break;
case 33:
//#line 106 "Parser.y"
{ yyval.obj = whilestmt____WHILE_LPAREN_expr_RPAREN_stmt(val_peek(2).obj, val_peek(0).obj); }
break;
case 34:
//#line 109 "Parser.y"
{ yyval.obj = compoundstmt____LBRACE_localdecls_stmtlist_RBRACE(val_peek(2).obj, val_peek(1).obj); }
break;
case 35:
//#line 112 "Parser.y"
{ yyval.obj = localdecls____localdecls_localdecl(val_peek(1).obj, val_peek(0).obj); }
break;
case 36:
//#line 113 "Parser.y"
{ yyval.obj = localdecls____eps(); }
break;
case 37:
//#line 116 "Parser.y"
{ yyval.obj = localdecl____typespec_IDENT_SEMI(val_peek(2).obj, val_peek(1).obj); }
break;
case 38:
//#line 119 "Parser.y"
{ yyval.obj = ifstmt____IF_LPAREN_expr_RPAREN_stmt_ELSE_stmt(val_peek(4).obj, val_peek(2).obj, val_peek(0).obj); }
break;
case 39:
//#line 122 "Parser.y"
{ yyval.obj = returnstmt____RETURN_expr(val_peek(0).obj); }
break;
case 40:
//#line 125 "Parser.y"
{ yyval.obj = printstmt____PRINT_expr(val_peek(0).obj); }
break;
case 41:
//#line 128 "Parser.y"
{ yyval.obj = arglist____arglist_COMMA_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 42:
//#line 129 "Parser.y"
{ yyval.obj = arglist____expr(val_peek(0).obj); }
break;
case 43:
//#line 132 "Parser.y"
{ yyval.obj = args____arglist(val_peek(0).obj); }
break;
case 44:
//#line 133 "Parser.y"
{ yyval.obj = args____eps(); }
break;
case 45:
//#line 136 "Parser.y"
{ yyval.obj = expr____expr_OPADD_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 46:
//#line 137 "Parser.y"
{ yyval.obj = expr____expr_OPSUB_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 47:
//#line 138 "Parser.y"
{ yyval.obj = expr____expr_OPMUL_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 48:
//#line 139 "Parser.y"
{ yyval.obj = expr____expr_OPDIV_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 49:
//#line 140 "Parser.y"
{ yyval.obj = expr____expr_OPMOD_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 50:
//#line 141 "Parser.y"
{ yyval.obj = expr____expr_OPOR_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 51:
//#line 142 "Parser.y"
{ yyval.obj = expr____expr_OPAND_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 52:
//#line 143 "Parser.y"
{ yyval.obj = expr____OPNOT_expr(val_peek(0).obj); }
break;
case 53:
//#line 144 "Parser.y"
{ yyval.obj = expr____expr_RELOPEQ_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 54:
//#line 145 "Parser.y"
{ yyval.obj = expr____expr_RELOPNE_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 55:
//#line 146 "Parser.y"
{ yyval.obj = expr____expr_RELOPLE_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 56:
//#line 147 "Parser.y"
{ yyval.obj = expr____expr_RELOPLT_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 57:
//#line 148 "Parser.y"
{ yyval.obj = expr____expr_RELOPGE_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 58:
//#line 149 "Parser.y"
{ yyval.obj = expr____expr_RELOPGT_expr(val_peek(2).obj, val_peek(0).obj); }
break;
case 59:
//#line 150 "Parser.y"
{ yyval.obj = expr____LPAREN_expr_RPAREN(val_peek(1).obj); }
break;
case 60:
//#line 151 "Parser.y"
{ yyval.obj = expr____IDENT(val_peek(0).obj); }
break;
case 61:
//#line 152 "Parser.y"
{ yyval.obj = expr____BOOLLIT(val_peek(0).obj); }
break;
case 62:
//#line 153 "Parser.y"
{ yyval.obj = expr____INTLIT(val_peek(0).obj); }
break;
case 63:
//#line 154 "Parser.y"
{ yyval.obj = expr____FLOATLIT(val_peek(0).obj); }
break;
case 64:
//#line 155 "Parser.y"
{ yyval.obj = expr____IDENT_LPAREN_args_RPAREN(val_peek(3).obj, val_peek(1).obj); }
break;
case 65:
//#line 156 "Parser.y"
{ yyval.obj = expr____IDENT_LBRACKET_expr_RBRACKET(val_peek(3).obj, val_peek(1).obj); }
break;
case 66:
//#line 157 "Parser.y"
{ yyval.obj = expr____IDENT_DOT_SIZE(val_peek(2).obj); }
break;
case 67:
//#line 158 "Parser.y"
{ yyval.obj = expr____IDENT_DOT_IDENT(val_peek(2).obj, val_peek(0).obj); }
break;
//#line 867 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
