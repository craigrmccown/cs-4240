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
	: var_declaraion var_declaration_list
	|
	;

type_declaration
	: TYPE ID EQUALS type SEMICOLON
	;