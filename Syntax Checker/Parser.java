//Seongmin An
import java.lang.reflect.Array;
import java.util.*;

public class Parser
{
    public static final int ENDMARKER   =  0;
    public static final int LEXERROR    =  1;
    public static final int INT         = 11;
    public static final int BOOL        = 12;
    public static final int WHILE       = 13;
    public static final int IF          = 14;
    public static final int ELSE        = 15;
    public static final int BOOL_LIT    = 16;
    public static final int BEGIN       = 17;
    public static final int END         = 18;
    public static final int LPAREN      = 19;
    public static final int RPAREN      = 20;
    public static final int LBRACKET    = 21;
    public static final int RBRACKET    = 22;
    public static final int SEMI        = 23;
    public static final int COMMA       = 24;
    public static final int ASSIGN      = 25;
    public static final int EXPROP      = 26;
    public static final int TERMOP      = 27;
    public static final int RELOP       = 28;
    public static final int INT_LIT     = 29;
    public static final int IDENT       = 30;

    public class Token {
        public int       type;
        public ParserVal attr;
        public Token(int type, ParserVal attr) {
            this.type   = type;
            this.attr   = attr;
        }
    }
    
    public class Result {
        public List<String> update;
        public String err;
        public int indentCount;
        public int beginCount;
        public Result(List<String> update, String err, int indentCount, int beginCount){
            this.update = update;
            this.err = err;
            this.indentCount = indentCount;
            this.beginCount = beginCount;
        }
    }

    public ParserVal yylval;
    Token _token;
    Lexer _lexer;
    Result result;
    public Parser(java.io.Reader r) throws Exception
    {
        _lexer = new Lexer(r, this);
        _token = null;
        result = new Result(new ArrayList<>(), "", 0, 0);
        Advance();
    }

    public void Advance() throws Exception
    {
        int token_type = _lexer.yylex();
        if(token_type ==  0)      _token = new Token(ENDMARKER , null  );
        else if(token_type == -1) _token = new Token(LEXERROR  , yylval);
        else                      _token = new Token(token_type, yylval);
    }

    public String Match(int token_type) throws Exception
    {
        boolean match = (token_type == _token.type);
        result.update.add(_token.attr.obj.toString());

        if(match == false){
            result.err = "\"" + GetAttr(token_type) + "\" is expected instead of \"" + _token.attr.obj + "\" at " + _lexer.lineno + ":" + _lexer.column + ".";
            throw new Exception();
        }
        _lexer.column += _token.attr.obj.toString().length();

        if(_token.type != ENDMARKER)
            Advance();

        return "";
    }

    public int yyparse() throws Exception
    {
        try
        {
            parse();
            System.out.println("Success: no syntax error found.\n");
            System.out.println("Following is the indentation-updated source code:\n");
            for (int i = 0; i < result.update.size(); i++){
                System.out.print(result.update.get(i));
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: there exists syntax error(s).");
            System.out.println(result.err);
        }
        return 0;
    }

    public String parse() throws Exception
    {
        return program();
    }

    public String program() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                decl_list();
                return "";
            case BOOL:
                decl_list();
                return "";
            case ENDMARKER:
                decl_list();
                return "";
        }
        throw new Exception();
    }
    public String decl_list() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                decl_list_();
                return "";
            case BOOL:
                decl_list_();
                return "";
            case ENDMARKER:
                decl_list_();
                return "";
        }
        throw new Exception();
    }
    public String decl_list_() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                if (!result.update.isEmpty()){
                    result.update.add("\n");
                    result.update.add("\n");
                }
                fun_decl();
                decl_list_();
                return "";
            case BOOL:
                if (!result.update.isEmpty()){
                    result.update.add("\n");
                    result.update.add("\n");
                }
                fun_decl();
                decl_list_();
                return "";
            case ENDMARKER:
                return "";
        }
        result.err = "An incorrect type format is declared at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception();
    }
    public String type_spec() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                Match(INT);
                type_spec_();
                return "";
            case BOOL:
                Match(BOOL);
                type_spec_();
                return "";
        }
        throw new Exception();
    }
    public String type_spec_() throws Exception
    {
        switch(_token.type)
        {
            case LBRACKET:
                Match(LBRACKET);
                Match(RBRACKET);
                type_spec_();
                return "";
            case IDENT:
                return "";
        }
        result.err = "An incorrect type format is declared at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception();
    }
    public String fun_decl() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                type_spec();
                result.update.add(" ");
                Match(IDENT);
                Match(LPAREN);
                params();
                Match(RPAREN);
                compound_stmt();
                return "";
            case BOOL:
                type_spec();
                result.update.add(" ");
                Match(IDENT);
                Match(LPAREN);
                params();
                Match(RPAREN);
                compound_stmt();
                return "";
        }
        throw new Exception();
    }
    public String params() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                param_list();
                return "";
            case BOOL:
                param_list();
                return "";
            case RPAREN:
                return "";
        }
        throw new Exception("not implemented");
    }
    public String param_list() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                param();
                param_list_();
                return "";
            case BOOL:
                param();
                param_list_();
                return "";
        }
        throw new Exception("not implemented");
    }
    public String param_list_() throws Exception
    {
        switch(_token.type)
        {
            case COMMA:
                Match(COMMA);
                result.update.add(" ");
                param();
                param_list_();
                return "";
            case RPAREN:
                return "";
        }
        result.err = "There is an incorrect parameter format of a function at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception("not implemented");
    }
    public String param() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                type_spec();
                result.update.add(" ");
                Match(IDENT);
                return "";
            case BOOL:
                type_spec();
                result.update.add(" ");
                Match(IDENT);
                return "";
        }
        throw new Exception("not implemented");
    }
    public String stmt_list() throws Exception
    {
        switch(_token.type)
        {
            case SEMI:
                stmt_list_();
                return "";
            case IF:
                stmt_list_();
                return "";
            case WHILE:
                stmt_list_();
                return "";
            case BEGIN:
                stmt_list_();
                return "";
            case IDENT:
                stmt_list_();
                return "";
            case END:
                stmt_list_();
                return "";
        }
        throw new Exception("not implemented");
    }
    public String stmt_list_() throws Exception
    {
        switch(_token.type)
        {
            case SEMI:
                stmt();
                stmt_list_();
                return "";
            case IF:
                stmt();
                stmt_list_();
                return "";
            case WHILE:
                stmt();
                stmt_list_();
                return "";
            case BEGIN:
                stmt();
                stmt_list_();
                return "";
            case IDENT:
                stmt();
                stmt_list_();
                return "";
            case END:
                return "";
        }
        result.err = "There must be a (if, while, assignment, or compound) statement at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception("not implemented");
    }
    public String stmt() throws Exception
    {
        switch(_token.type)
        {
            case IDENT:
                result.update.add("\n");
                AddTab();
                expr_stmt();
                Match(SEMI);
                return "";
            case BEGIN:
                compound_stmt();
                return "";
            case IF:
                if_stmt();
                return "";
            case WHILE:
                while_stmt();
                return "";
            case SEMI:
                if (result.update.get(result.update.size()-1).equals("{")){
                    result.update.add("\n");
                    AddTab();
                }
                Match(SEMI);
                return "";
        }
        throw new Exception("not implemented");
    }
    public String expr_stmt() throws Exception
    {
        switch(_token.type)
        {
            case IDENT:
                Match(IDENT);
                result.update.add(" ");
                Match(ASSIGN);
                result.update.add(" ");
                expr();
                return "";
        }
        throw new Exception("not implemented");
    }
    public String while_stmt() throws Exception
    {
        switch(_token.type)
        {
            case WHILE:
                result.update.add("\n");
                AddTab();
                Match(WHILE);
                Match(LPAREN);
                result.update.add(" ");
                expr();
                result.update.add(" ");
                Match(RPAREN);
                stmt();
                return "";
        }
        throw new Exception("not implemented");
    }
    public String compound_stmt() throws Exception
    {
        switch(_token.type)
        {
            case BEGIN:
                while (result.indentCount != result.beginCount) result.indentCount -= 1;
                result.update.add("\n");
                AddTab();
                result.indentCount += 1;
                result.beginCount += 1;
                Match(BEGIN);
                local_decls();
                stmt_list();
                result.indentCount -= 1;
                result.beginCount -= 1;
                result.update.add("\n");
                AddTab();
                Match(END);
                return "";
        }
        result.err = "\"{\" is expected instead of \"" + _token.attr.obj + "\" at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception("not implemented");
    }
    public String local_decls() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                local_decls_();
                return "";
            case BOOL:
                local_decls_();
                return "";
            case SEMI:
                local_decls_();
                return "";
            case IF:
                local_decls_();
                return "";
            case WHILE:
                local_decls_();
                return "";
            case BEGIN:
                local_decls_();
                return "";
            case END:
                local_decls_();
                return "";
            case IDENT:
                local_decls_();
                return "";
        }
        throw new Exception("not implemented");
    }
    public String local_decls_() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                result.update.add("\n");
                AddTab();
                local_decl();
                local_decls_();
                return "";
            case BOOL:
                result.update.add("\n");
                AddTab();
                local_decl();
                local_decls_();
                return "";
            case SEMI:
                return "";
            case IF:
                return "";
            case WHILE:
                return "";
            case BEGIN:
                return "";
            case END:
                return "";
            case IDENT:
                return "";
        }
        throw new Exception("not implemented");
    }
    public String local_decl() throws Exception
    {
        switch(_token.type)
        {
            case INT:
                type_spec();
                result.update.add(" ");
                Match(IDENT);
                Match(SEMI);
                return "";
            case BOOL:
                type_spec();
                result.update.add(" ");
                Match(IDENT);
                Match(SEMI);
                return "";
        }
        throw new Exception("not implemented");
    }
    public String if_stmt() throws Exception
    {
        switch(_token.type)
        {
            case IF:
                result.update.add("\n");
                AddTab();
                Match(IF);
                Match(LPAREN);
                result.update.add(" ");
                expr();
                result.update.add(" ");
                Match(RPAREN);
                result.indentCount += 1;
                stmt();
                result.update.add("\n");
                if (result.indentCount != result.beginCount) result.indentCount -= 1;
                AddTab();
                Match(ELSE);
                result.indentCount += 1;
                stmt();
                if (result.indentCount != result.beginCount) result.indentCount -= 1;
                return "";
        }
        throw new Exception("not implemented");
    }
    public String arg_list() throws Exception
    {
        switch(_token.type)
        {
            case LPAREN:
                expr();
                arg_list_();
                return "";
            case BOOL_LIT:
                expr();
                arg_list_();
                return "";
            case IDENT:
                expr();
                arg_list_();
                return "";
            case INT_LIT:
                expr();
                arg_list_();
                return "";
        }
        throw new Exception("not implemented");
    }
    public String arg_list_() throws Exception
    {
        switch(_token.type)
        {
            case COMMA:
                Match(COMMA);
                result.update.add(" ");
                expr();
                arg_list_();
                return "";
            case RPAREN:
                return "";
        }
        result.err = " There is a wrong argument format at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception("not implemented");
    }
    public String args() throws Exception
    {
        switch(_token.type)
        {
            case LPAREN:
                expr();
                arg_list_();
                return "";
            case BOOL_LIT:
                expr();
                arg_list_();
                return "";
            case IDENT:
                expr();
                arg_list_();
                return "";
            case INT_LIT:
                expr();
                arg_list_();
                return "";
            case RPAREN:
                return "";
        }
        throw new Exception("not implemented");
    }
    public String expr() throws Exception
    {
        switch(_token.type)
        {
            case LPAREN:
                term();
                expr_();
                return "";
            case BOOL_LIT:
                term();
                expr_();
                return "";
            case IDENT:
                term();
                expr_();
                return "";
            case INT_LIT:
                term();
                expr_();
                return "";
        }
        result.err = "There is an expression error at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception("not implemented");
    }
    public String expr_() throws Exception
    {
        switch(_token.type)
        {
            case EXPROP:
                result.update.add(" ");
                Match(EXPROP);
                result.update.add(" ");
                term();
                expr_();
                return "";
            case RELOP:
                result.update.add(" ");
                Match(RELOP);
                result.update.add(" ");
                term();
                expr_();
                return "";
            case RPAREN:
                return "";
            case COMMA:
                return "";
            case SEMI:
                return "";
            case RBRACKET:
                return "";
        }
        throw new Exception("not implemented");
    }
    public String term() throws Exception
    {
        switch(_token.type)
        {
            case LPAREN:
                factor();
                term_();
                return "";
            case BOOL_LIT:
                factor();
                term_();
                return "";
            case IDENT:
                factor();
                term_();
                return "";
            case INT_LIT:
                factor();
                term_();
                return "";
        }
        result.err = "There is an expression error at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception("not implemented");
    }
    public String term_() throws Exception
    {
        switch(_token.type)
        {
            case TERMOP:
                result.update.add(" ");
                Match(TERMOP);
                result.update.add(" ");
                factor();
                term_();
                return "";
            case RPAREN:
                return "";
            case COMMA:
                return "";
            case SEMI:
                return "";
            case RBRACKET:
                return "";
            case EXPROP:
                return "";
            case RELOP:
                return "";
        }
        result.err = "There is an expression error at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception("not implemented");
    }
    public String factor() throws Exception
    {
        switch(_token.type)
        {
            case IDENT:
                Match(IDENT);
                factor_();
                return "";
            case LPAREN:
                Match(LPAREN);
                result.update.add(" ");
                expr();
                result.update.add(" ");
                Match(RPAREN);
                return "";
            case INT_LIT:
                Match(INT_LIT);
                return "";
            case BOOL_LIT:
                Match(BOOL_LIT);
                return "";
        }
        result.err = "There is an expression error at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception("not implemented");
    }
    public String factor_() throws Exception
    {
        switch(_token.type)
        {
            case LPAREN:
                Match(LPAREN);
                args();
                Match(RPAREN);
                return "";
            case LBRACKET:
                Match(LBRACKET);
                expr();
                Match(RBRACKET);
                return "";
            case RPAREN:
                return "";
            case COMMA:
                return "";
            case SEMI:
                return "";
            case RBRACKET:
                return "";
            case EXPROP:
                return "";
            case RELOP:
                return "";
            case TERMOP:
                return "";
        }
        result.err = "There is an expression error at " + _lexer.lineno + ":" + _lexer.column + ".";
        throw new Exception("not implemented");
    }

    String GetAttr(int type){
        switch (type){
            case 11:
                return "int";
            case 12:
                return "bool";
            case 13:
                return "while";
            case 14:
                return "if";
            case 15:
                return "else";
            case 16:
                return "Boolean value";
            case 17:
                return "{";
            case 18:
                return "}";
            case 19:
                return "(";
            case 20:
                return ")";
            case 21:
                return "[";
            case 22:
                return "]";
            case 23:
                return ";";
            case 24:
                return ",";
            case 25:
                return "=";
            case 26:
                return "EXPROP";
            case 27:
                return "TERMOP";
            case 28:
                return "RELOP";
            case 29:
                return "Int value";
            case 30:
                return "Identifier";
        }
        return "";
    }

    void AddTab(){
        for (int i = 0; i < result.indentCount; i++){
            result.update.add("\t");
        }
    }
}
