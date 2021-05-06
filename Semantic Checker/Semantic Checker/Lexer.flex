/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2000 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%%

%class Lexer
%byaccj

%{

  public Parser   parser;
  public int      lineno;
  public int      column;

  public Lexer(java.io.Reader r, Parser parser) {
    this(r);
    this.parser = parser;
    this.lineno = 1;
    this.column = 1;
  }

%}

int        = [0-9]+
float      = [0-9]+"."[0-9]+(E[+-]?[0-9]+)?
identifier = [a-zA-Z_][a-zA-Z0-9_]*
newline    = \n
whitespace = [ \t\r]+
comment    = "//".*

%%

"print"                             { ParserImpl.curr_input += "print"  ;return Parser.PRINT      ; }
"bool"                              { ParserImpl.curr_input += "bool"   ;return Parser.BOOL       ; }
"int"                               { ParserImpl.curr_input += "int"    ;return Parser.INT        ; }
"float"                             { ParserImpl.curr_input += "float"  ;return Parser.FLOAT      ; }
"struct"                            { ParserImpl.curr_input += "struct" ;return Parser.STRUCT     ; }
"size"                              { ParserImpl.curr_input += "size"   ;return Parser.SIZE       ; }
"new"                               { ParserImpl.curr_input += "new"    ;return Parser.NEW        ; }
"while"                             { ParserImpl.curr_input += "while"  ;return Parser.WHILE      ; }
"if"                                { ParserImpl.curr_input += "if"     ;return Parser.IF         ; }
"else"                              { ParserImpl.curr_input += "else"   ;return Parser.ELSE       ; }
"return"                            { ParserImpl.curr_input += "return" ;return Parser.RETURN     ; }
"{"                                 { ParserImpl.curr_input += "{"      ;return Parser.LBRACE     ; }
"}"                                 { ParserImpl.curr_input += "}"      ;return Parser.RBRACE     ; }
"("                                 { ParserImpl.curr_input += "("      ;return Parser.LPAREN     ; }
")"                                 { ParserImpl.curr_input += ")"      ;return Parser.RPAREN     ; }
"["                                 { ParserImpl.curr_input += "["      ;return Parser.LBRACKET   ; }
"]"                                 { ParserImpl.curr_input += "]"      ;return Parser.RBRACKET   ; }
"="                                 { ParserImpl.curr_input += "="      ;return Parser.ASSIGN     ; }
"&"                                 { ParserImpl.curr_input += "&"      ;return Parser.ADDR       ; }
";"                                 { ParserImpl.curr_input += ";"      ;return Parser.SEMI       ; }
","                                 { ParserImpl.curr_input += ","      ;return Parser.COMMA      ; }
"."                                 { ParserImpl.curr_input += "."      ;return Parser.DOT        ; }
"+"                                 { ParserImpl.curr_input += "+"      ;return Parser.OP_ADD     ; }
"-"                                 { ParserImpl.curr_input += "-"      ;return Parser.OP_SUB     ; }
"*"                                 { ParserImpl.curr_input += "*"      ;return Parser.OP_MUL     ; }
"/"                                 { ParserImpl.curr_input += "/"      ;return Parser.OP_DIV     ; }
"%"                                 { ParserImpl.curr_input += "%"      ;return Parser.OP_MOD     ; }
"and"                               { ParserImpl.curr_input += "and"    ;return Parser.OP_AND     ; }
"or"                                { ParserImpl.curr_input += "or"     ;return Parser.OP_OR      ; }
"not"                               { ParserImpl.curr_input += "not"    ;return Parser.OP_NOT     ; }
"<"                                 { ParserImpl.curr_input += "<"      ;return Parser.RELOP_LT   ; }
">"                                 { ParserImpl.curr_input += ">"      ;return Parser.RELOP_GT   ; }
"<="                                { ParserImpl.curr_input += "<="     ;return Parser.RELOP_LE   ; }
">="                                { ParserImpl.curr_input += ">="     ;return Parser.RELOP_GE   ; }
"=="                                { ParserImpl.curr_input += "=="     ;return Parser.RELOP_EQ   ; }
"!="                                { ParserImpl.curr_input += "!="     ;return Parser.RELOP_NE   ; }
"true"|"false"                      { ParserImpl.curr_input += (new ParserVal((Object)yytext())).obj; parser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL_LIT   ; }
{int}                               { ParserImpl.curr_input += (new ParserVal((Object)yytext())).obj; parser.yylval = new ParserVal((Object)yytext()); return Parser.INT_LIT    ; }
{float}                             { ParserImpl.curr_input += (new ParserVal((Object)yytext())).obj; parser.yylval = new ParserVal((Object)yytext()); return Parser.FLOAT_LIT  ; }
{identifier}                        { ParserImpl.curr_input += (new ParserVal((Object)yytext())).obj; parser.yylval = new ParserVal((Object)yytext()); return Parser.IDENT      ; }
{comment}                           { /* skip */ }
{newline}                           { ParserImpl.curr_input += (new ParserVal((Object)yytext())).obj;  }
{whitespace}                        { ParserImpl.curr_input += (new ParserVal((Object)yytext())).obj;  }
"/*"                                { /* skip */ }
"*/"                                { /* skip */ }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
