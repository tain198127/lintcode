lexer grammar TestLexer; //词法定义
SS:' SELECT ';
FF:' FROM ';
WW:' WHERE ';
WORD:[A-Za-z_$0-9]*?[A-Za-z_$]+?[A-Za-z_$0-9]*;
