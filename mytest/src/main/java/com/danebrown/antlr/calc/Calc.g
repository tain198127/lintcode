grammar Calc;

prog
    : stat+
    ;

stat
    : expr
    | ID '=' expr
    ;

expr
    : expr ('*'|'/') expr
    | expr ('+'|'-') expr
    | INT
    | ID
    | '(' expr ')'
    ;

ID  : [a-zA-Z]+ ;

INT : [0-9]+ ;

WS  : [ \t\r\n]+ -> skip ;    // toss out whitespace