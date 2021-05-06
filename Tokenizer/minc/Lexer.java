// Seongmin An

import java.io.IOException;
import java.util.ArrayList;

public class Lexer
{
    private static final char EOF        =  0;

    private Parser         yyparser; // parent parser object
    private java.io.Reader reader;   // input stream
    public int             lineno;   // line number
    public String          tokenname;
    private char[] buffer1, buffer2;
    private int lb = 0;
    private int f = 0;
    private boolean buffChange = false;

    public Lexer(java.io.Reader reader, Parser yyparser) throws java.io.IOException
    {
        this.reader   = reader;
        this.yyparser = yyparser;
        lineno = 1;
        tokenname = new String();
    }

    public char NextChar() throws IOException
    {
        if (buffer1 == null){
            buffer1 = new char[10];
            reader.read(buffer1, 0, 9);
        }
        if (buffer2 == null){
            buffer2 = new char[10];
            reader.read(buffer2, 0, 9);
        }

        if (lb == 9){
            lb = 0;
            f = 0;
            buffer1 = buffer2;
            buffer2 = new char[10];
            reader.read(buffer2, 0, 9);
        }

        if (f == -1){buffChange = false; f = 8;}

        if (f == 9){buffChange = true; f = 0;}

        int data = 0;
        if (buffChange == false){data = buffer1[f];}
        if (buffChange == true){data = buffer2[f];}
        if (f == -1){data = buffer1[8];}

        return (char)data;
    }

    public int Fail()
    {
        return -1;
    }

    private String GetLexeme(){
        String lexeme = new String();
        if (buffChange == false){
            for (int i = lb; i <= f; i++){
                lexeme += buffer1[i];
            }
        }
        if (buffChange == true){
            for (int i = lb; i <= 8; i++){
                lexeme += buffer1[i];
            }
            if (f != -1){
                for (int i = 0; i <= f; i++){
                    lexeme += buffer2[i];
                }
            }

            buffer1 = buffer2;
            buffer2 = null;
            buffChange = false;
        }

        lb = ++f;
        f = lb;

        return lexeme;
    }

    public int yylex() throws java.io.IOException
    {
        int state = 0;

        while(true)
        {
            char c;
            c = NextChar();
            switch(state)
            {
                case 0:
                    if (c == '\n'){lb++; f++; lineno++; break;}
                    if (c == ' ' || c == '\r' || c == '\t'){lb++; f++; break;}
                    if (Character.isDigit(c)) {state = 1; f++; break;}
                    if (c == 'm') {state = 30; f++; break;}
                    if (c == 'p') {state = 34; f++; break;}
                    if (c == 'i') {state = 39; f++; break;}
                    if (c == 'e') {state = 41; f++; break;}
                    if (Character.isLetter(c) || c == '_') {state = 4; f++; break;}
                    if (c == '+' || c == '-' || c == '*' || c == '/'){state = 10; break;}
                    if (c == '<' || c == '>' || c == '!') {state = 5; f++; break;}
                    if (c == '=') {state = 6; f++; break;}
                    if (c == '{') {state = 13; break;}
                    if (c == '}') {state = 14; break;}
                    if (c == '(') {state = 15; break;}
                    if (c == ')') {state = 16; break;}
                    if (c == ';') {state = 17; break;}
                    if (c == ',') {state = 18; break;}
                    if (c == EOF) {state = 99; break; }
                    return Fail();
                case 1:
                    if (Character.isDigit(c)){f++; break;}
                    if (c == '.'){state = 2; f++; break;}
                    if (Character.isLetter(c) || c == '_'){state = 100; break;}
                    state = 19; f--; break;
                case 2:
                    if (Character.isDigit(c)){state = 3; f++; break;}
                    state = 100; break;
                case 3:
                    if (Character.isDigit(c)){f++; break;}
                    if (Character.isLetter(c) || c == '_'){state = 100; break;}
                    state = 19; f--; break;
                case 4:
                    if (Character.isLetter(c) || c == '_' || Character.isDigit(c)){f++; break;}
                    state = 20; f--; break;
                case 5:
                    if (c == '='){state = 11; break;}
                    state = 11; f--; break;
                case 6:
                    if (c == '='){state = 11; break;}
                    state = 12; f--; break;
                case 10:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "OP";
                    return Parser.OP;
                case 11:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "RELOP";
                    return Parser.RELOP;
                case 12:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "ASSIGN";
                    return Parser.ASSIGN;
                case 13:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "BEGIN";
                    return Parser.BEGIN;
                case 14:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "END";
                    return Parser.END;
                case 15:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "LPAREN";
                    return Parser.LPAREN;
                case 16:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "RPAREN";
                    return Parser.RPAREN;
                case 17:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "SEMI";
                    return Parser.SEMI;
                case 18:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "COMMA";
                    return Parser.COMMA;
                case 19:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "NUM";
                    return Parser.NUM;
                case 20:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "ID";
                    return Parser.ID;
                case 21:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "MAIN";
                    return Parser.MAIN;
                case 22:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "PRINT";
                    return Parser.PRINT;
                case 23:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "IF";
                    return Parser.IF;
                case 24:
                    yyparser.yylval = new ParserVal((Object) GetLexeme());
                    tokenname = "ELSE";
                    return Parser.ELSE;
                case 30:
                    if (c == 'a'){state = 31; f++; break;}
                    state = 4; break;
                case 31:
                    if (c == 'i'){state = 32; f++; break;}
                    state = 4; break;
                case 32:
                    if (c == 'n'){state = 33; f++; break;}
                    state = 4; break;
                case 33:
                    if (Character.isLetter(c) || c == '_' || Character.isDigit(c)){state = 4; break;}
                    state = 21; f--; break;
                case 34:
                    if (c == 'r'){state = 35; f++; break;}
                    state = 4; break;
                case 35:
                    if (c == 'i'){state = 36; f++; break;}
                    state = 4; break;
                case 36:
                    if (c == 'n'){state = 37; f++; break;}
                    state = 4; break;
                case 37:
                    if (c == 't'){state = 38; f++; break;}
                    state = 4; break;
                case 38:
                    if (Character.isLetter(c) || c == '_' || Character.isDigit(c)){state = 4; break;}
                    state = 22; f--; break;
                case 39:
                    if (c == 'f'){state = 40; f++; break;}
                    state = 4; break;
                case 40:
                    if (Character.isLetter(c) || c == '_' || Character.isDigit(c)){state = 4; break;}
                    state = 23; f--; break;
                case 41:
                    if (c == 'l'){state = 42; f++; break;}
                    state = 4; break;
                case 42:
                    if (c == 's'){state = 43; f++; break;}
                    state = 4; break;
                case 43:
                    if (c == 'e'){state = 44; f++; break;}
                    state = 4; break;
                case 44:
                    if (Character.isLetter(c) || c == '_' || Character.isDigit(c)){state = 4; break;}
                    state = 24; f--; break;


                case 99:
                    return EOF;                                     // return end-of-file symbol
                case 100:
                    return Fail();
            }
        }
    }
}
