// Seongmin An

public class Compiler
{
    java.util.HashMap<String, Integer> _symtbl;
    Parser _parser;

    public Compiler(java.io.Reader r) throws java.io.IOException
    {
        _symtbl = new java.util.HashMap<String, Integer>();
        _parser = new Parser(r, this);
    }
    public void Compile() throws java.io.IOException
    {
        _parser.yyparse();
    }
}
