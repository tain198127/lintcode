grammar GrammerStatment; //语法定义
import TestLexer;
param:
    WORD
    ;
sql:
    SS param FF param WW
    ;