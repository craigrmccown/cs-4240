grammar Tiger

tokens {
    FUNCTION = 'function';
    BEGIN = 'begin';
    END = 'end';
    VOID = 'void';
    MAIN = 'main';
    TYPE = 'type';
    ARRAY = 'array';
    OF = 'of';
    INT = 'int';
    FIXEDPT = 'fixedpt';
    VAR = 'var';
    IF = 'if';
    THEN = 'then';
    ENDIF = 'endif';
    ELSE = 'else';
    WHILE = 'while';
    DO = 'do';
    ENDDO = 'enddo';
    FOR = 'for';
    TO = 'to';
    BREAK = 'break';
    RETURN = 'return';
    COMMA = ',';
    COLON = ':';
    SEMICOLON = ';';
    OPENPAREN = '(';
    CLOSEPAREN = ')';
    OPENBRACKET = '[';
    CLOSEBRACKET = ']';
    PLUS = '+';
    MINUS = '-';
    MULTIPLY = '*';
    DIVIDE = '/';
    EQUALS = '=';
    NOT_EQUAL = '<>';
    LESS_THAN = '<';
    LESS_THAN_EQUAL = '<=';
    GREATER_THAN = '>';
    GREATER_THAN_EQUAL = '>=';
    BIT_AND = '&';
    BIT_OR = '|';
    ASSIGNMENT_OP = ':=';
}

ID
    : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INTLIT
    : ('1'..'9')('0'..'9')*
    ;

COMMENT
    : '/*'.*'*/'
    { skip() }
    ;

WS
    : ' '
    | '\t'
    | '\r'
    | '\n'
    { skip() }
    ;

tiger_program
    : type_declaration_list funct_declaration_list main_function
    ;

funct_declaration_list
    : funct_declaration funct_declaration_list
    |
    ;
    
funct_declaration
    : ret_type FUNCTION ID OPENPAREN param_list CLOSEPAREN BEGIN block_list END SEMICOLON
    ;

main_function
    : VOID MAIN OPENPAREN CLOSEPAREN BEGIN block_list END SEMICOLON
    ;

ret_type
    : VOID
    | type_id
    ;

param_list
    : param param_list_tail
    |
    ;

param_list_tail
    : COMMA param param_list_tail
    |
    ;

param
    : ID COLON type_id
    ;

block_list
    : block block_tail
    ;

block_tail
    : block block_tail
    |
    ;

block
    : BEGIN declaration_segment stat_seq END SEMICOLON
    ;

declaration_segment
    : type_declaration_list
    | var_declaration_list
    ;

type_declaration_list
    : type_declaration type_declaration_list
    |
    ;

var_declaration_list
    : var_declaration var_declaration_list
    |
    ;

type_declaration
    : TYPE ID EQUALS type SEMICOLON
    ;

type
    : base_type
    | ARRAY OPENBRACKET INTLIT CLOSEBRACKET OF base_type
    | ARRAY OPENBRACKET INTLIT CLOSEBRACKET OPENBRACKET INTLIT CLOSEBRACKET OF base_type
    ;

type_id
    : base_type
    | ID
    ;

base_type
    : INT
    | FIXEDPT
    ;

var_declaration
    : VAR id_list COLON type_id optional_init SEMICOLON
    ;

id_list
    : ID
    | ID COMMA id_list
    ;

optional_init
    : const
    |
    ;

stat_seq
    : stat
    | stat stat_seq
    ;

stat
    : value ASSIGNMENT_OP expr SEMICOLON
    | IF expr THEN stat_seq ENDIF SEMICOLON
    | IF expr THEN stat_seq ELSE stat_seq ENDIF SEMICOLON
    | WHILE expr DO stat_seq ENDDO SEMICOLON
    | FOR ID ASSIGNMENT_OP index_expr TO index_expr DO stat_seq ENDDO SEMICOLON
    | opt_prefix ID OPENPAREN expr_list CLOSEPAREN SEMICOLON
    | BREAK SEMICOLON
    | RETURN expr SEMICOLON
    | block
    ;

opt_prefix
    : value ASSIGNMENT_OP
    |
    ;