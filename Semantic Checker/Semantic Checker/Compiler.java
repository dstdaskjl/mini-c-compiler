import java.io.IOException;

public class Compiler {
    Parser parser;
    public Compiler(java.io.Reader r) throws Exception {
        parser = new Parser(r);
    }
    public void Parse()
    {
        try {
            if (parser.yyparse() == 0)
            {
                System.out.println("Success: no semantic error is found.");
            }
            else
            {
                System.out.println("Error: there is syntax error(s).");
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: there is semantic error(s).");
            System.out.println(e.getMessage());
        }
    }
}
