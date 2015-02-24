grammar Tiger;

options {
    k = 1;
    output = AST;
    ASTLabelType=CommonTree;
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
    PROGRAM;
    PARAMS;
    BLOCK;
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
    : type_declaration_list funct_declaration_list -> ^(PROGRAM type_declaration_list? funct_declaration_list)
    ;

funct_declaration_list
    : ret_type=INT! funct_declaration[$ret_type] funct_declaration_list
    | ret_type=FIXEDPT! funct_declaration[$ret_type] funct_declaration_list
    | ret_type=ID! funct_declaration[$ret_type] funct_declaration_list
    | VOID! void_funct_or_main
    ;

funct_declaration[Token ret_type]
    : FUNCTION ID OPENPAREN param_list CLOSEPAREN BEGIN block_list END SEMICOLON -> ^(FUNCTION {new CommonTree($ret_type)} ID param_list? block_list)
    ;

void_funct_or_main
    : void_funct_declaration funct_declaration_list
    | MAIN OPENPAREN CLOSEPAREN BEGIN block_list END SEMICOLON -> ^(MAIN VOID block_list)
    ;

void_funct_declaration
    : FUNCTION ID OPENPAREN param_list CLOSEPAREN BEGIN block_list END SEMICOLON -> ^(FUNCTION VOID ID param_list? block_list)
    ;

param_list
    : param param_list_tail -> ^(PARAMS param param_list_tail?)
    |
    ;

param_list_tail
    : COMMA! param param_list_tail
    |
    ;

param
    : ID COLON type_id -> ^(type_id ID)
    ;

block_list
    : block block_tail
    ;

block_tail
    : block block_tail
    |
    ;

block
    : BEGIN declaration_segment stat_seq END SEMICOLON -> ^(BLOCK declaration_segment? stat_seq)
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
    : named_type EQUALS type SEMICOLON -> ^(EQUALS named_type type)
    ;

named_type
    : TYPE ID -> ^(TYPE ID)
    ;

type
    : base_type
    | ARRAY OPENBRACKET INTLIT CLOSEBRACKET type_end OF base_type -> ^(OF ^(ARRAY INTLIT type_end?) base_type)
    ;

type_end
    : OPENBRACKET! INTLIT CLOSEBRACKET!
    |
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
    : VAR id_list COLON type_id optional_init SEMICOLON -> ^(type_id id_list optional_init?)
    ;

id_list
    : ID id_list_tail
    ;

id_list_tail
    : COMMA! id_list
    |
    ;

optional_init
    : ASSIGNMENT_OP constant -> ^(ASSIGNMENT_OP constant)
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
    : id=ID! funct_call_or_assignment[$id] SEMICOLON!
    | IF expr THEN stat_seq stat_else ENDIF SEMICOLON -> ^(IF expr ^(THEN stat_seq) stat_else?)
    | WHILE expr DO stat_seq ENDDO SEMICOLON -> ^(WHILE expr ^(DO stat_seq))
    | FOR ID ASSIGNMENT_OP index_expr TO index_expr DO stat_seq ENDDO SEMICOLON -> ^(FOR ^(TO ^(ASSIGNMENT_OP ID index_expr) index_expr) ^(DO stat_seq))
    | BREAK SEMICOLON
    | RETURN expr SEMICOLON -> ^(RETURN expr)
    | block
    ;

stat_else
    : ELSE stat_seq -> ^(ELSE stat_seq)
    |
    ;

funct_call_or_assignment[Token id]
    : OPENPAREN expr_list CLOSEPAREN -> ^({new CommonTree($id)} expr_list)
    | value_index ASSIGNMENT_OP stat_expr -> ^(ASSIGNMENT_OP ^({new CommonTree($id)} value_index?) stat_expr?)
    ;

stat_expr
    : id=ID! funct_call_or_v_expr[$id]
    | nv_expr
    ;

funct_call_or_v_expr[Token id]
    : OPENPAREN expr_list CLOSEPAREN -> ^({new CommonTree($id)} expr_list?)
    | v_expr[$id]
    ;

/* stand-alone expression */

expr
    : expr_2 (bit_operator^ expr_2)*
    ;

bit_operator
    : BIT_AND
    | BIT_OR
    ;

expr_2
    : expr_3 (compare_operator^ expr_3)*
    ;

compare_operator
    : EQUALS
    | NOT_EQUAL
    | LESS_THAN
    | GREATER_THAN
    | LESS_THAN_EQUAL
    | GREATER_THAN_EQUAL expr_2
    ;

expr_3
    : expr_4 (plus_or_minus^ expr_4)*
    ;

plus_or_minus
    : PLUS
    | MINUS
    ;

expr_4
    : expr_5 (multiply_or_divide^ expr_5)*
    ;

multiply_or_divide
    : MULTIPLY
    | DIVIDE
    ;

expr_5
    : OPENPAREN! expr^ CLOSEPAREN!
    | value
    | constant
    ;

v_expr[Token id]
    : v_expr_2[$id] (bit_operator^ expr_2)*
    ;

v_expr_2[Token id]
    : v_expr_3[$id] (compare_operator^ expr_3)*
    ;

v_expr_3[Token id]
    : v_expr_4[$id] (plus_or_minus^ expr_4)*
    ;

v_expr_4[Token id]
    : (value_index -> {new CommonTree($id)} value_index?) (multiply_or_divide expr_5 -> ^(multiply_or_divide expr_5))*
    ;

nv_expr
    : nv_expr_2 (bit_operator^ expr_2)*
    ;

nv_expr_2
    : nv_expr_3 (compare_operator^ expr_3)*
    ;

nv_expr_3
    : nv_expr_4 (plus_or_minus^ expr_4)*
    ;

nv_expr_4
    : nv_expr_5 (multiply_or_divide^ expr_5)*
    ;

nv_expr_5
    : OPENPAREN! expr^ CLOSEPAREN!
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
    : COMMA! expr expr_list_tail
    |
    ;

value
    : ID^ value_index
    ;

value_index
    : OPENBRACKET! index_expr CLOSEBRACKET! value_index_2
    |
    ;

value_index_2
    : OPENBRACKET! index_expr CLOSEBRACKET!
    |
    ;

index_expr
    : index_expr_2 (plus_or_minus^ index_expr_2)*
    ;

index_expr_2
    : index_expr_3 (MULTIPLY^ index_expr_3)*
    ;

index_expr_3
    : value
    | constant
    ;