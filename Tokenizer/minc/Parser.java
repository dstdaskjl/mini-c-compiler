// Seongmin An

public class Parser
{
    public static final int OP         = 10;
    public static final int RELOP      = 11;
    public static final int ASSIGN     = 12;
    public static final int BEGIN      = 13;
    public static final int END        = 14;
    public static final int LPAREN     = 15;
    public static final int RPAREN     = 16;
    public static final int SEMI       = 17;
    public static final int COMMA      = 18;
    public static final int NUM        = 19;
    public static final int ID         = 20;
    public static final int MAIN       = 21;
    public static final int PRINT      = 22;
    public static final int IF         = 23;
    public static final int ELSE       = 24;

    Compiler         compiler;
    Lexer            lexer;     // lexer.yylex() returns token-name
    public ParserVal yylval;    // yylval contains token-attribute

    public Parser(java.io.Reader r, Compiler compiler) throws java.io.IOException
    {
        this.compiler = compiler;
        this.lexer    = new Lexer(r, this);
    }

    public int yyparse() throws java.io.IOException
    {
        while ( true )
        {
            int token = lexer.yylex();  // get next token-name
            Object attr = yylval.obj;   // get      token-attribute

            if(token == 0)
            {
                // EOF is reached
                System.out.println("successful in tokenizing the input.");
                return 0;
            }
            if(token == -1)
            {
                // error
                System.out.println("tokenizing error at line " + lexer.lineno + ".");
                return -1;
            }

            System.out.println("<token-name:" + lexer.tokenname + ", token-attr:\"" + attr + "\", lineno:" + lexer.lineno + ">");
        }
    }
}
