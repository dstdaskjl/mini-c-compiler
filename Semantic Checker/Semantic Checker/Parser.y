/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * This is a modified version of the example from                          *
 *   http://www.lincom-asg.com/~rjamison/byacc/                            *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%{
import java.io.*;
import java.util.ArrayList;
%}

%right  ASSIGN
%left   OP_OR 
%left   OP_AND 
%left   RELOP_EQ      RELOP_NE  
%left   RELOP_LE      RELOP_LT      RELOP_GE      RELOP_GT 
%left   OP_ADD        OP_SUB
%left   OP_MUL        OP_DIV        OP_MOD 
%right  OP_NOT


%token <obj>    ASSIGN
%token <obj>    RELOP_EQ  RELOP_NE  RELOP_LE  RELOP_LT  RELOP_GE  RELOP_GT
%token <obj>    OP_ADD    OP_SUB    OP_MUL    OP_DIV    OP_MOD
%token <obj>    OP_OR     OP_AND    OP_NOT

%token <obj>    IDENT       INT_LIT     FLOAT_LIT       BOOL_LIT

%token <obj> BOOL  INT  FLOAT
%token <obj> IF  ELSE  NEW  PRINT  WHILE  RETURN  CONTINUE  BREAK
%token <obj> LPAREN  RPAREN  LBRACE  RBRACE  LBRACKET  RBRACKET  STRUCT  SIZE  SEMI  COMMA  DOT  ADDR

%type <obj> program decl_list   decl
%type <obj> var_decl type_spec struc_decl fun_decl params param_list param
%type <obj> stmt_list stmt expr_stmt while_stmt compound_stmt local_decls local_decl
%type <obj> if_stmt return_stmt print_stmt arg_list
%type <obj> args expr

%%

program         : decl_list                                     { $$ = program____decllist($1); }
                ;

decl_list       : decl_list decl                                { $$ = decllist____decllist_decl($1, $2); }
                |                                               { $$ = decllist____eps(); }
                ;

decl            : var_decl                                      { $$ = decl____vardecl($1); }
                | struc_decl                                    { $$ = decl____strucdecl($1); }
                | fun_decl                                      { $$ = decl____fundecl($1); }
                ;

var_decl        : type_spec IDENT SEMI                          { $$ = vardecl____typespec_IDENT_SEMI($1, $2); }
                ;

type_spec       : BOOL                                          { $$ = typespec____BOOL(); }
                | INT                                           { $$ = typespec____INT(); }
                | FLOAT                                         { $$ = typespec____FLOAT(); }
                | STRUCT IDENT                                  { $$ = typespec____STRUCT_IDENT($2); }
                | type_spec LBRACKET RBRACKET                   { $$ = typespec____typespec_LBRACKET_RBRACKET($1); }
                ;

struc_decl      : STRUCT IDENT LBRACE local_decls RBRACE SEMI   { $$ = strucdecl____STRUCT_IDENT_LBRACE_localdecls_RBRACE_SEMI($2, $4); }
                ;

fun_decl        : type_spec IDENT LPAREN params RPAREN LBRACE local_decls stmt_list RBRACE { $$ = fundecl____typespec_IDENT_LPAREN_params_RPAREN_LBRACE_localdecls_stmtlist_RBRACE($1, $2, $4, $7, $8); }
                ;

params          : param_list                                    { $$ = params____paramlist($1); }
                |                                               { $$ = params____eps(); }
                ;

param_list      : param_list COMMA param                        { $$ = paramlist____paramlist_COMMA_param($1, $3); }
                | param                                         { $$ = paramlist____param($1); }
                ;

param           : type_spec IDENT                               { $$ = param____typespec_IDENT($1, $2); }
                ;

stmt_list       : stmt_list stmt                                { $$ = stmtlist____stmtlist_stmt($1, $2); }
                |                                               { $$ = stmtlist____eps(); }
                ;

stmt            : expr_stmt SEMI                                { $$ = stmt____exprstmt_SEMI($1); }
                | compound_stmt                                 { $$ = stmt____compoundstmt($1); }
                | if_stmt                                       { $$ = stmt____ifstmt($1); }
                | while_stmt                                    { $$ = stmt____whilestmt($1); }
                | return_stmt SEMI                              { $$ = stmt____returnstmt_SEMI($1); }
                | print_stmt SEMI                               { $$ = stmt____printstmt_SEMI($1); }
                | SEMI                                          { $$ = stmt____SEMI(); }
                ;

expr_stmt       : IDENT ASSIGN expr_stmt                        { $$ = exprstmt____IDENT_ASSIGN_exprstmt($1, $3); }
                | expr                                          { $$ = exprstmt____expr($1); }
                | IDENT LBRACKET expr RBRACKET ASSIGN expr_stmt { $$ = exprstmt____IDENT_LBRACKET_expr_RBRACKET_ASSIGN_exprstmt($1, $3, $6); }
                | IDENT DOT IDENT ASSIGN expr_stmt              { $$ = exprstmt____IDENT_DOT_IDENT_ASSIGN_exprstmt($1, $3, $5); }
                ;

while_stmt      : WHILE LPAREN expr RPAREN stmt                 { $$ = whilestmt____WHILE_LPAREN_expr_RPAREN_stmt($3, $5); }
                ;

compound_stmt   : LBRACE local_decls stmt_list RBRACE           { $$ = compoundstmt____LBRACE_localdecls_stmtlist_RBRACE($2, $3); }
                ;

local_decls     : local_decls local_decl                        { $$ = localdecls____localdecls_localdecl($1, $2); }
                |                                               { $$ = localdecls____eps(); }
                ;

local_decl      : type_spec IDENT SEMI                          { $$ = localdecl____typespec_IDENT_SEMI($1, $2); }
                ;

if_stmt         : IF LPAREN expr RPAREN stmt ELSE stmt          { $$ = ifstmt____IF_LPAREN_expr_RPAREN_stmt_ELSE_stmt($3, $5, $7); }
                ;

return_stmt     : RETURN expr                                   { $$ = returnstmt____RETURN_expr($2); }
                ;

print_stmt      : PRINT expr                                    { $$ = printstmt____PRINT_expr($2); }
                ;

arg_list        : arg_list COMMA expr                           { $$ = arglist____arglist_COMMA_expr($1, $3); }
                | expr                                          { $$ = arglist____expr($1); }
                ;

args            : arg_list                                      { $$ = args____arglist($1); }
                |                                               { $$ = args____eps(); }
                ;

expr            : expr OP_ADD expr                              { $$ = expr____expr_OPADD_expr($1, $3); }
                | expr OP_SUB expr                              { $$ = expr____expr_OPSUB_expr($1, $3); }
                | expr OP_MUL expr                              { $$ = expr____expr_OPMUL_expr($1, $3); }
                | expr OP_DIV expr                              { $$ = expr____expr_OPDIV_expr($1, $3); }
                | expr OP_MOD expr                              { $$ = expr____expr_OPMOD_expr($1, $3); }
                | expr OP_OR expr                               { $$ = expr____expr_OPOR_expr($1, $3); }
                | expr OP_AND expr                              { $$ = expr____expr_OPAND_expr($1, $3); }
                | OP_NOT expr                                   { $$ = expr____OPNOT_expr($2); }
                | expr RELOP_EQ expr                            { $$ = expr____expr_RELOPEQ_expr($1, $3); }
                | expr RELOP_NE expr                            { $$ = expr____expr_RELOPNE_expr($1, $3); }
                | expr RELOP_LE expr                            { $$ = expr____expr_RELOPLE_expr($1, $3); }
                | expr RELOP_LT expr                            { $$ = expr____expr_RELOPLT_expr($1, $3); }
                | expr RELOP_GE expr                            { $$ = expr____expr_RELOPGE_expr($1, $3); }
                | expr RELOP_GT expr                            { $$ = expr____expr_RELOPGT_expr($1, $3); }
                | LPAREN expr RPAREN                            { $$ = expr____LPAREN_expr_RPAREN($2); }
                | IDENT                                         { $$ = expr____IDENT($1); }
                | BOOL_LIT                                      { $$ = expr____BOOLLIT($1); }
                | INT_LIT                                       { $$ = expr____INTLIT($1); }
                | FLOAT_LIT                                     { $$ = expr____FLOATLIT($1); }
                | IDENT LPAREN args RPAREN                      { $$ = expr____IDENT_LPAREN_args_RPAREN($1, $3); }
                | IDENT LBRACKET expr RBRACKET                  { $$ = expr____IDENT_LBRACKET_expr_RBRACKET($1, $3); }
                | IDENT DOT SIZE                                { $$ = expr____IDENT_DOT_SIZE($1); }
                | IDENT DOT IDENT                               { $$ = expr____IDENT_DOT_IDENT($1, $3); }
                ;

%%
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
