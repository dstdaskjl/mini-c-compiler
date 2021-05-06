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

  public Parser   yyparser;
  public int      lineno;
  public int      column;

  public Lexer(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
    this.lineno   = 1;
    this.column   = 1;
  }
%}

int        = [0-9]+
identifier = [a-zA-Z_][a-zA-Z0-9_]*
newline    = \n
whitespace = [ \t\r]+
comment    = "//".*

%%

"int"                               { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.INT     ; }
"bool"                              { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL    ; }
"while"                             { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.WHILE   ; }
"if"                                { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.IF      ; }
"else"                              { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.ELSE    ; }
"true"                              { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL_LIT; }
"false"                             { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.BOOL_LIT; }
"{"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.BEGIN   ; }
"}"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.END     ; }
"("                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.LPAREN  ; }
")"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.RPAREN  ; }
"["                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.LBRACKET; }
"]"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.RBRACKET; }
";"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.SEMI    ; }
","                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.COMMA   ; }
"="                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.ASSIGN  ; }
"+"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.EXPROP  ; }
"-"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.EXPROP  ; }
"or"                                { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.EXPROP  ; }
"*"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.TERMOP  ; }
"/"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.TERMOP  ; }
"and"                               { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.TERMOP  ; }
"=="                                { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
"!="                                { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
"<"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
">"                                 { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
"<="                                { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
">="                                { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.RELOP   ; }
{int}                               { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.INT_LIT ; }
{identifier}                        { yyparser.yylval = new ParserVal((Object)yytext()); return Parser.IDENT   ; }
{comment}                           { column = 1; }
{newline}                           { lineno += 1; column = 1; }
{whitespace}                        { column += yytext().length(); }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
