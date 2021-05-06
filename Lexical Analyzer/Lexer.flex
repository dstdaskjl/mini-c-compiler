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
float      = (([0-9]+"."?[0-9]*)|("."[0-9]+))(E[-+]?[0-9]+)?
identifier = [a-zA-Z_][a-zA-Z0-9_]*
comment    = "//".*
newline    = \n
whitespace = [ \t\r]+


%%

"main"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.MAIN   ; }
"print"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.PRINT  ; }
"bool"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL    ; }
"int"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.INT    ; }
"float"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.FLOAT    ; }
"struct"                            { parser.yylval = new ParserVal((Object)yytext()); return Parser.STRUCT    ; }
"size"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.SIZE    ; }
"new"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.NEW    ; }
"while"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.WHILE; }
"if"                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.IF; }
"else"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.ELSE; }
"return"                            { parser.yylval = new ParserVal((Object)yytext()); return Parser.RETURN; }
"break"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.BREAK; }
"continue"                          { parser.yylval = new ParserVal((Object)yytext()); return Parser.CONTINUE; }
"true"                              { parser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL_VALUE; }
"false"                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL_VALUE; }
"{"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.BEGIN  ; }
"}"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.END    ; }
"("                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.LPAREN ; }
")"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RPAREN ; }
"["                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.LBRACKET ; }
"]"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RBRACKET ; }
";"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.SEMI   ; }
","                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.COMMA   ; }
"."                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.DOT   ; }
"&"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.ADDR ; }
"="                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.ASSIGN ; }
"+"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP     ; }
"-"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP     ; }
"*"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP     ; }
"/"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP     ; }
"%"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP     ; }
"and"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP     ; }
"or"                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP     ; }
"not"                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.OP     ; }
"=="                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP  ; }
"!="                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP  ; }
"<"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP  ; }
">"                                 { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP  ; }
"<="                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP  ; }
">="                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP  ; }
{int}                               { parser.yylval = new ParserVal((Object)yytext()); return Parser.INT_VALUE; }
{float}                             { parser.yylval = new ParserVal((Object)yytext()); return Parser.FLOAT_VALUE; }
{identifier}                        { parser.yylval = new ParserVal((Object)yytext()); return Parser.IDENT  ; }
{comment}                           { parser.yylval = new ParserVal((Object)yytext()); return Parser.COMMENT; }
"/*"                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.BLOCK; }
"*/"                                { parser.yylval = new ParserVal((Object)yytext()); return Parser.BLOCK; }
{newline}                           { parser.yylval = new ParserVal((Object)yytext()); return Parser.NEWLINE; }
{whitespace}                        { parser.yylval = new ParserVal((Object)yytext()); return Parser.WHITESPACE; }




\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
