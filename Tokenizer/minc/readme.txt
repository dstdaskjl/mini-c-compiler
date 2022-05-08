The code below is for compiling the java files.
Change the filepath and put the code in the main method.

args = new String[] { F I L E P A T H };
if(args.length <= 0)
    return;
java.io.Reader r = new java.io.FileReader(args[0]);
Compiler compiler = new Compiler(r);
compiler.Compile();

Keyword: lb(lexBegin pointer), f(forward pointer),
	 buffChange(When yylex() method reads the last element of the first buffer, buffChange is changed to true from false. Then yylex() reads the second buffer)

Two buffers with size 10 are created in NextChar() method. NextChar() returns a character from either buffer1 or buffer2.
Using the character, yylex() finds every lexeme. The while loop in yylex() method is the transition diagram.
Whenever yylex() reaches the final state, it calls GetLexeme() method, which returns a lexeme based on the two buffers, lb, and f.
If yylex() finds any error, yyparse() method shows an error message and the program is closed.
