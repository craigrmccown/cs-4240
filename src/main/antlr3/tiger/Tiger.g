grammar Tiger;

options {
    k = 1;
}

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

@parser::members {
    @Override
    public void reportError(RecognitionException e) {
        throw new RuntimeException("parsing error on line " + e.line + " at " + e.token.getText());
    }
}

@lexer::members {
    @Override
    public void reportError(RecognitionException e) {
        throw new RuntimeException("lexing error on line " + e.line + " at " + e.token.getText());
    }
}

ID
    : ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INTLIT
    : '0' | ('1'..'9')('0'..'9')*
    ;

FIXEDPTLIT
    : ('0'|('1'..'9')('0'..'9')*)'.'('0'..'9')+
    ;

COMMENT
    : '/*'.*'*/'
    { skip(); }
    ;

WS
    : (' '|'\t'|'\f'|'\n'|'\r')+
    { skip(); }
    ;

tiger_program
    : type_declaration_list funct_declaration_list
    ;

funct_declaration_list
    : type_id non_void_funct_declaration funct_declaration_list
    | VOID void_funct_declaration
    ;
    
non_void_funct_declaration
    : type_id FUNCTION ID OPENPAREN param_list CLOSEPAREN BEGIN block_list END SEMICOLON
    ;

void_funct_declaration
    : FUNCTION ID OPENPAREN param_list CLOSEPAREN BEGIN block_list END SEMICOLON funct_declaration_list
    | MAIN OPENPAREN CLOSEPAREN BEGIN block_list END SEMICOLON
    ;

funct_declaration
    : FUNCTION ID OPENPAREN param_list CLOSEPAREN BEGIN block_list END SEMICOLON
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
    : type_declaration_list var_declaration_list
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
    | ARRAY OPENBRACKET INTLIT CLOSEBRACKET type_end OF base_type
    ;

type_end
    : OPENBRACKET INTLIT CLOSEBRACKET
    |
    ;

type_id
    : base_type
    | ID
    ;

base_type
    : INT
    | FIXEDPTLIT
    ;

var_declaration
    : VAR id_list COLON type_id optional_init SEMICOLON
    ;

id_list
    : ID id_list_tail
    ;

id_list_tail
    : COMMA id_list
    |
    ;

optional_init
    : ASSIGNMENT_OP constant
    |
    ;

stat_seq
    : stat stat_seq_tail
    ;

stat_seq_tail
    : stat stat_seq_tail
    |
    ;

stat
    : ID funct_call_or_assignment SEMICOLON
    | IF expr THEN stat_seq stat_else ENDIF SEMICOLON
    | WHILE expr DO stat_seq ENDDO SEMICOLON
    | FOR ID ASSIGNMENT_OP index_expr TO index_expr DO stat_seq ENDDO SEMICOLON
    | BREAK SEMICOLON
    | RETURN expr SEMICOLON
    | block
    ;

stat_else
    : ELSE stat_seq
    |
    ;

funct_call_or_assignment
    : OPENPAREN expr_list CLOSEPAREN
    | value_index ASSIGNMENT_OP stat_expr
    ;

stat_expr
    : ID funct_call_or_v_expr
    | nv_expr
    ;

funct_call_or_v_expr
    : OPENPAREN expr_list CLOSEPAREN
    | v_expr
    ;

/* stand-alone expression */

expr
    : expr_2 expr_tail
    ;

expr_tail
    : BIT_AND expr
    | BIT_OR expr
    |
    ;

expr_2
    : expr_3 expr_tail_2
    ;

expr_tail_2
    : EQUALS expr_2
    | NOT_EQUAL expr_2
    | LESS_THAN expr_2
    | GREATER_THAN expr_2
    | LESS_THAN_EQUAL expr_2
    | GREATER_THAN_EQUAL expr_2
    |
    ;

expr_3
    : expr_4 expr_tail_3
    ;

expr_tail_3
    : PLUS expr_3
    | MINUS expr_3
    |
    ;

expr_4
    : expr_5 expr_tail_4
    ;

expr_tail_4
    : MULTIPLY expr_4
    | DIVIDE expr_4
    |
    ;

expr_5
    : OPENPAREN expr CLOSEPAREN
    | value
    | constant
    ;

/* value expression */

v_expr
    : v_expr_2 expr_tail
    ;

v_expr_2
    : v_expr_3 expr_tail_2
    ;

v_expr_3
    : v_expr_4 expr_tail_3
    ;

v_expr_4
    : value_index expr_tail_4
    ;

/* non-value expression */

nv_expr
    : nv_expr_2 expr_tail
    ;

nv_expr_2
    : nv_expr_3 expr_tail_2
    ;

nv_expr_3
    : nv_expr_4 expr_tail_3
    ;

nv_expr_4
    : nv_expr_5 expr_tail_4
    ;

nv_expr_5
    : OPENPAREN expr CLOSEPAREN
    | constant
    ;

constant
    : INTLIT
    | FIXEDPTLIT
    ;

expr_list
    : expr expr_list_tail
    |
    ;

expr_list_tail
    : expr expr_list_tail
    |
    ;

value
    : ID value_index
    ;

value_index
    : OPENBRACKET index_expr CLOSEBRACKET value_index_2
    |
    ;

value_index_2
    : OPENBRACKET index_expr CLOSEBRACKET
    |
    ;

index_expr
    : index_expr_2 index_expr_tail
    ;

index_expr_tail
    : PLUS index_expr
    | MINUS index_expr
    |
    ;

index_expr_2
    : value index_expr_2_tail
    | constant index_expr_2_tail
    ;

index_expr_2_tail
    : MULTIPLY index_expr_2
    |
    ;