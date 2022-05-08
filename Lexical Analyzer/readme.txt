Open jflex-1.6.1.jar program. Set Lexical specification file as Lexer.flex. Click the "Generate" button. Then Lexer.java file will be created.

New identifiers will be stored in the "symboltable" Hashmap and all possible token names are stored in the "tokenName" Hashmap.

SymbolList1 contains token name numbers which do not need to print their attribute.

SymbolList2 contains token name numbers which need to print their attribute.

"blockBegin" is a flag for block comment. If /* is seen, the flag turns to true and all the letters following become comments until the lexer finds */

state = 0 prints a result without an attribute

state = 1 prints a result with attribute

state = 2 prints an identifier. If it is a new identifier, it is stored in "symboltable"

state = 3 prints a comment

state = 4 prints a block comment. It must start with /* and end with */

state = 5 prints a new line

Anything else is an error
