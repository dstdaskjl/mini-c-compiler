import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser
{
    public static final int MAIN        = 1 ;
    public static final int PRINT       = 2 ;
    public static final int BOOL        = 3 ;
    public static final int INT         = 4 ;
    public static final int FLOAT       = 5 ;
    public static final int STRUCT      = 6 ;
    public static final int SIZE        = 7 ;
    public static final int NEW         = 8 ;
    public static final int WHILE       = 9 ;
    public static final int IF          = 10;
    public static final int ELSE        = 11;
    public static final int RETURN      = 12;
    public static final int BREAK       = 13;
    public static final int CONTINUE    = 14;
    public static final int BEGIN       = 15;
    public static final int END         = 16;
    public static final int LPAREN      = 17;
    public static final int RPAREN      = 18;
    public static final int LBRACKET    = 19;
    public static final int RBRACKET    = 20;
    public static final int SEMI        = 21;
    public static final int COMMA       = 22;
    public static final int DOT         = 23;
    public static final int ADDR        = 24;
    public static final int ASSIGN      = 25;
    public static final int OP          = 26;
    public static final int RELOP       = 27;
    public static final int INT_VALUE   = 28;
    public static final int FLOAT_VALUE = 29;
    public static final int BOOL_VALUE  = 30;
    public static final int IDENT       = 31;
    public static final int COMMENT     = 32;
    public static final int BLOCK       = 33;
    public static final int NEWLINE     = 34;
    public static final int WHITESPACE  = 35;


    public Parser(java.io.Reader r) throws java.io.IOException
    {
        this.lexer    = new Lexer(r, this);
    }

    Lexer   lexer;
    public ParserVal yylval;

    // Identifier goes to symboltable. Identifier is the key and the number of identifiers in current symboltable is the value.
    java.util.HashMap<String, Integer> symboltable = new HashMap<>();

    // All possible token names are sotred in tokenName Hashmap.
    java.util.HashMap<Integer, String> tokenName = new HashMap<>(){
        {
            put(1 , "MAIN");
            put(2 , "PRINT");
            put(3 , "BOOL");
            put(4 , "INT");
            put(5 , "FLOAT");
            put(6 , "STRUCT");
            put(7 , "SIZE");
            put(8 , "NEW");
            put(9 , "WHILE");
            put(10, "IF");
            put(11, "ELSE");
            put(12, "RETURN");
            put(13, "BREAK");
            put(14, "CONTINUE");
            put(15, "BEGIN");
            put(16, "END");
            put(17, "LPAREN");
            put(18, "RPAREN");
            put(19, "LBRACKET");
            put(20, "RBRACKET");
            put(21, "SEMI");
            put(22, "COMMA");
            put(23, "DOT");
            put(24, "ADDRESS");
            put(25, "ASSIGN");
            put(26, "OP");
            put(27, "RELOP");
            put(28, "INT_VALUE");
            put(29, "FLOAT_VALUE");
            put(30, "BOOL_VALUE");
            put(31, "ID");
            put(32, "COMMENT");
            put(33, "BLOCK");
            put(34, "NEWLINE");
            put(35, "WHITESPACE");
        }
    };

    // SymbolList1 contains token name numbers which do not need to print their attribute.
    List<Integer> symbolList1 = new ArrayList<>(){
        {
            for (int i = 1; i < 26; i++){
                add(i);
            }
        }
    };

    // SymbolList2 contains token name numbers which need to print their attribute
    List<Integer> symbolList2 = new ArrayList<>(){
        {
            for (int i = 26; i < 31; i++){
                add(i);
            }
        }
    };

    // blockBegin is a flag for block comment. If /* is seen, the flag turns to true and all the letters following become comments until the lexer finds */
    boolean blockBegin = false;
    int state = 0;
    
    public int yyparse() throws java.io.IOException
    {
        while ( true ) {
            int token = lexer.yylex();

            if (token == 0) {
                // EOF is reached
                return 0;
            }
            if (token == -1) {
                // error
                return -1;
            }

            // state = 0 prints a result without attribute
            // state = 1 prints a result with attribute
            // state = 2 prints an identifier. If it is a new identifier, it is stored in symboltable
            // state = 3 prints a comment
            // state = 4 prints a block comment. It must start with /* and end with */
            // state = 5 prints a new line
            // Anything else is an error
            if (symbolList1.contains(token)) state = 0;
            if (symbolList2.contains(token)) state = 1;
            if (token == 31) state = 2;
            if (token == 32) state = 3;
            if (token == 33) state = 4;
            if (token == 34) state = 5;
            if (token == 35) state = 3;

            if (blockBegin == true) state = 4;

            Object attr = yylval.obj;

            switch (state) {
                case 0:
                    System.out.print("<"+ tokenName.get(token) + " :" + lexer.lineno + ":" + lexer.column + ">");
                    lexer.column += ((String)attr).length();
                    break;
                case 1:
                    System.out.print("<" + tokenName.get(token) + ", " + attr + " :" + lexer.lineno + ":" + lexer.column + ">");
                    lexer.column = lexer.column + ((String)attr).length();
                    break;
                case 2:
                    if (!symboltable.containsKey((String) attr)){
                        System.out.print("<< add \"" + attr + "\" into symbol table at " + symboltable.size() + " >>");
                        symboltable.put((String) attr, symboltable.size());
                    }
                    System.out.print("<ID, " + symboltable.get(attr) + " :" + lexer.lineno + ":" + lexer.column + ">");
                    lexer.column = lexer.column + ((String)attr).length();
                    break;
                case 3:
                    System.out.print(attr);
                    lexer.column += ((String)attr).length();
                    break;
                case 4:
                    if (blockBegin == true){
                        if (attr.equals("*/")){
                            blockBegin = false;
                            System.out.print(attr);
                            lexer.column += ((String)attr).length();
                        }
                        else{
                            if (attr.equals("\n")){
                                System.out.println();
                                lexer.lineno += 1;
                                lexer.column = 1;
                            }
                            else{
                                System.out.print(attr);
                                lexer.column += ((String)attr).length();
                            }
                        }
                    }
                    else{
                        if (attr.equals("/*")){
                            blockBegin = true;
                            System.out.print(attr);
                            lexer.column += ((String)attr).length();
                        }
                        else{
                            throw new AssertionError("Out of rule");
                        }
                    }
                    break;
                case 5:
                    System.out.println();
                    lexer.lineno += 1;
                    lexer.column = 1;
                    break;
                default:
                    throw new AssertionError("Error");
            }
        }
    }
}
