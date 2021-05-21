grammar GrammerStatment;
import TestLexer;
param:
    WORD
    ;
sql:
    SS param FF param WW
    ;