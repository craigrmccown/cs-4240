// $ANTLR 3.5.1 /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g 2015-02-17 20:35:17

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class TigerLexer extends Lexer {
	public static final int EOF=-1;
	public static final int ARRAY=4;
	public static final int ASSIGNMENT_OP=5;
	public static final int BEGIN=6;
	public static final int BIT_AND=7;
	public static final int BIT_OR=8;
	public static final int BREAK=9;
	public static final int CLOSEBRACKET=10;
	public static final int CLOSEPAREN=11;
	public static final int COLON=12;
	public static final int COMMA=13;
	public static final int COMMENT=14;
	public static final int DIVIDE=15;
	public static final int DO=16;
	public static final int ELSE=17;
	public static final int END=18;
	public static final int ENDDO=19;
	public static final int ENDIF=20;
	public static final int EQUALS=21;
	public static final int FIXEDPT=22;
	public static final int FIXEDPTLIT=23;
	public static final int FOR=24;
	public static final int FUNCTION=25;
	public static final int GREATER_THAN=26;
	public static final int GREATER_THAN_EQUAL=27;
	public static final int ID=28;
	public static final int IF=29;
	public static final int INT=30;
	public static final int INTLIT=31;
	public static final int LESS_THAN=32;
	public static final int LESS_THAN_EQUAL=33;
	public static final int MAIN=34;
	public static final int MINUS=35;
	public static final int MULTIPLY=36;
	public static final int NOT_EQUAL=37;
	public static final int OF=38;
	public static final int OPENBRACKET=39;
	public static final int OPENPAREN=40;
	public static final int PLUS=41;
	public static final int PROGRAM=42;
	public static final int RETURN=43;
	public static final int SEMICOLON=44;
	public static final int THEN=45;
	public static final int TO=46;
	public static final int TYPE=47;
	public static final int VAR=48;
	public static final int VOID=49;
	public static final int WHILE=50;
	public static final int WS=51;

	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public TigerLexer() {} 
	public TigerLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public TigerLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "/Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g"; }

	// $ANTLR start "ARRAY"
	public final void mARRAY() throws RecognitionException {
		try {
			int _type = ARRAY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:2:7: ( 'array' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:2:9: 'array'
			{
			match("array"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ARRAY"

	// $ANTLR start "ASSIGNMENT_OP"
	public final void mASSIGNMENT_OP() throws RecognitionException {
		try {
			int _type = ASSIGNMENT_OP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:3:15: ( ':=' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:3:17: ':='
			{
			match(":="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ASSIGNMENT_OP"

	// $ANTLR start "BEGIN"
	public final void mBEGIN() throws RecognitionException {
		try {
			int _type = BEGIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:4:7: ( 'begin' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:4:9: 'begin'
			{
			match("begin"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BEGIN"

	// $ANTLR start "BIT_AND"
	public final void mBIT_AND() throws RecognitionException {
		try {
			int _type = BIT_AND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:5:9: ( '&' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:5:11: '&'
			{
			match('&'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BIT_AND"

	// $ANTLR start "BIT_OR"
	public final void mBIT_OR() throws RecognitionException {
		try {
			int _type = BIT_OR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:6:8: ( '|' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:6:10: '|'
			{
			match('|'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BIT_OR"

	// $ANTLR start "BREAK"
	public final void mBREAK() throws RecognitionException {
		try {
			int _type = BREAK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:7:7: ( 'break' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:7:9: 'break'
			{
			match("break"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BREAK"

	// $ANTLR start "CLOSEBRACKET"
	public final void mCLOSEBRACKET() throws RecognitionException {
		try {
			int _type = CLOSEBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:8:14: ( ']' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:8:16: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CLOSEBRACKET"

	// $ANTLR start "CLOSEPAREN"
	public final void mCLOSEPAREN() throws RecognitionException {
		try {
			int _type = CLOSEPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:9:12: ( ')' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:9:14: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CLOSEPAREN"

	// $ANTLR start "COLON"
	public final void mCOLON() throws RecognitionException {
		try {
			int _type = COLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:10:7: ( ':' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:10:9: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COLON"

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:11:7: ( ',' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:11:9: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "DIVIDE"
	public final void mDIVIDE() throws RecognitionException {
		try {
			int _type = DIVIDE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:12:8: ( '/' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:12:10: '/'
			{
			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DIVIDE"

	// $ANTLR start "DO"
	public final void mDO() throws RecognitionException {
		try {
			int _type = DO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:13:4: ( 'do' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:13:6: 'do'
			{
			match("do"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DO"

	// $ANTLR start "ELSE"
	public final void mELSE() throws RecognitionException {
		try {
			int _type = ELSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:14:6: ( 'else' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:14:8: 'else'
			{
			match("else"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ELSE"

	// $ANTLR start "END"
	public final void mEND() throws RecognitionException {
		try {
			int _type = END;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:15:5: ( 'end' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:15:7: 'end'
			{
			match("end"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "END"

	// $ANTLR start "ENDDO"
	public final void mENDDO() throws RecognitionException {
		try {
			int _type = ENDDO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:16:7: ( 'enddo' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:16:9: 'enddo'
			{
			match("enddo"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ENDDO"

	// $ANTLR start "ENDIF"
	public final void mENDIF() throws RecognitionException {
		try {
			int _type = ENDIF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:17:7: ( 'endif' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:17:9: 'endif'
			{
			match("endif"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ENDIF"

	// $ANTLR start "EQUALS"
	public final void mEQUALS() throws RecognitionException {
		try {
			int _type = EQUALS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:18:8: ( '=' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:18:10: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EQUALS"

	// $ANTLR start "FIXEDPT"
	public final void mFIXEDPT() throws RecognitionException {
		try {
			int _type = FIXEDPT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:19:9: ( 'fixedpt' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:19:11: 'fixedpt'
			{
			match("fixedpt"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FIXEDPT"

	// $ANTLR start "FOR"
	public final void mFOR() throws RecognitionException {
		try {
			int _type = FOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:20:5: ( 'for' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:20:7: 'for'
			{
			match("for"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FOR"

	// $ANTLR start "FUNCTION"
	public final void mFUNCTION() throws RecognitionException {
		try {
			int _type = FUNCTION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:21:10: ( 'function' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:21:12: 'function'
			{
			match("function"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FUNCTION"

	// $ANTLR start "GREATER_THAN"
	public final void mGREATER_THAN() throws RecognitionException {
		try {
			int _type = GREATER_THAN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:22:14: ( '>' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:22:16: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GREATER_THAN"

	// $ANTLR start "GREATER_THAN_EQUAL"
	public final void mGREATER_THAN_EQUAL() throws RecognitionException {
		try {
			int _type = GREATER_THAN_EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:23:20: ( '>=' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:23:22: '>='
			{
			match(">="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GREATER_THAN_EQUAL"

	// $ANTLR start "IF"
	public final void mIF() throws RecognitionException {
		try {
			int _type = IF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:24:4: ( 'if' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:24:6: 'if'
			{
			match("if"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IF"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:25:5: ( 'int' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:25:7: 'int'
			{
			match("int"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT"

	// $ANTLR start "LESS_THAN"
	public final void mLESS_THAN() throws RecognitionException {
		try {
			int _type = LESS_THAN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:26:11: ( '<' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:26:13: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LESS_THAN"

	// $ANTLR start "LESS_THAN_EQUAL"
	public final void mLESS_THAN_EQUAL() throws RecognitionException {
		try {
			int _type = LESS_THAN_EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:27:17: ( '<=' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:27:19: '<='
			{
			match("<="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LESS_THAN_EQUAL"

	// $ANTLR start "MAIN"
	public final void mMAIN() throws RecognitionException {
		try {
			int _type = MAIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:28:6: ( 'main' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:28:8: 'main'
			{
			match("main"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MAIN"

	// $ANTLR start "MINUS"
	public final void mMINUS() throws RecognitionException {
		try {
			int _type = MINUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:29:7: ( '-' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:29:9: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MINUS"

	// $ANTLR start "MULTIPLY"
	public final void mMULTIPLY() throws RecognitionException {
		try {
			int _type = MULTIPLY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:30:10: ( '*' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:30:12: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MULTIPLY"

	// $ANTLR start "NOT_EQUAL"
	public final void mNOT_EQUAL() throws RecognitionException {
		try {
			int _type = NOT_EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:31:11: ( '<>' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:31:13: '<>'
			{
			match("<>"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOT_EQUAL"

	// $ANTLR start "OF"
	public final void mOF() throws RecognitionException {
		try {
			int _type = OF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:32:4: ( 'of' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:32:6: 'of'
			{
			match("of"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OF"

	// $ANTLR start "OPENBRACKET"
	public final void mOPENBRACKET() throws RecognitionException {
		try {
			int _type = OPENBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:33:13: ( '[' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:33:15: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OPENBRACKET"

	// $ANTLR start "OPENPAREN"
	public final void mOPENPAREN() throws RecognitionException {
		try {
			int _type = OPENPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:34:11: ( '(' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:34:13: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OPENPAREN"

	// $ANTLR start "PLUS"
	public final void mPLUS() throws RecognitionException {
		try {
			int _type = PLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:35:6: ( '+' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:35:8: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PLUS"

	// $ANTLR start "RETURN"
	public final void mRETURN() throws RecognitionException {
		try {
			int _type = RETURN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:36:8: ( 'return' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:36:10: 'return'
			{
			match("return"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RETURN"

	// $ANTLR start "SEMICOLON"
	public final void mSEMICOLON() throws RecognitionException {
		try {
			int _type = SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:37:11: ( ';' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:37:13: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SEMICOLON"

	// $ANTLR start "THEN"
	public final void mTHEN() throws RecognitionException {
		try {
			int _type = THEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:38:6: ( 'then' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:38:8: 'then'
			{
			match("then"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "THEN"

	// $ANTLR start "TO"
	public final void mTO() throws RecognitionException {
		try {
			int _type = TO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:39:4: ( 'to' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:39:6: 'to'
			{
			match("to"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TO"

	// $ANTLR start "TYPE"
	public final void mTYPE() throws RecognitionException {
		try {
			int _type = TYPE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:40:6: ( 'type' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:40:8: 'type'
			{
			match("type"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TYPE"

	// $ANTLR start "VAR"
	public final void mVAR() throws RecognitionException {
		try {
			int _type = VAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:41:5: ( 'var' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:41:7: 'var'
			{
			match("var"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "VAR"

	// $ANTLR start "VOID"
	public final void mVOID() throws RecognitionException {
		try {
			int _type = VOID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:42:6: ( 'void' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:42:8: 'void'
			{
			match("void"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "VOID"

	// $ANTLR start "WHILE"
	public final void mWHILE() throws RecognitionException {
		try {
			int _type = WHILE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:43:7: ( 'while' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:43:9: 'while'
			{
			match("while"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WHILE"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:55:5: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:55:7: ( 'a' .. 'z' | 'A' .. 'Z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:55:27: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop1;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ID"

	// $ANTLR start "INTLIT"
	public final void mINTLIT() throws RecognitionException {
		try {
			int _type = INTLIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:59:5: ( '0' | ( '1' .. '9' ) ( '0' .. '9' )* )
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0=='0') ) {
				alt3=1;
			}
			else if ( ((LA3_0 >= '1' && LA3_0 <= '9')) ) {
				alt3=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}

			switch (alt3) {
				case 1 :
					// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:59:7: '0'
					{
					match('0'); 
					}
					break;
				case 2 :
					// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:59:13: ( '1' .. '9' ) ( '0' .. '9' )*
					{
					if ( (input.LA(1) >= '1' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:59:23: ( '0' .. '9' )*
					loop2:
					while (true) {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop2;
						}
					}

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INTLIT"

	// $ANTLR start "FIXEDPTLIT"
	public final void mFIXEDPTLIT() throws RecognitionException {
		try {
			int _type = FIXEDPTLIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:63:5: ( ( '0' | ( '1' .. '9' ) ( '0' .. '9' )* ) '.' ( '0' .. '9' )+ )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:63:7: ( '0' | ( '1' .. '9' ) ( '0' .. '9' )* ) '.' ( '0' .. '9' )+
			{
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:63:7: ( '0' | ( '1' .. '9' ) ( '0' .. '9' )* )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0=='0') ) {
				alt5=1;
			}
			else if ( ((LA5_0 >= '1' && LA5_0 <= '9')) ) {
				alt5=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}

			switch (alt5) {
				case 1 :
					// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:63:8: '0'
					{
					match('0'); 
					}
					break;
				case 2 :
					// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:63:12: ( '1' .. '9' ) ( '0' .. '9' )*
					{
					if ( (input.LA(1) >= '1' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:63:22: ( '0' .. '9' )*
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( ((LA4_0 >= '0' && LA4_0 <= '9')) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop4;
						}
					}

					}
					break;

			}

			match('.'); 
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:63:37: ( '0' .. '9' )+
			int cnt6=0;
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( ((LA6_0 >= '0' && LA6_0 <= '9')) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt6 >= 1 ) break loop6;
					EarlyExitException eee = new EarlyExitException(6, input);
					throw eee;
				}
				cnt6++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FIXEDPTLIT"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:67:5: ( '/*' ( . )* '*/' )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:67:7: '/*' ( . )* '*/'
			{
			match("/*"); 

			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:67:11: ( . )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0=='*') ) {
					int LA7_1 = input.LA(2);
					if ( (LA7_1=='/') ) {
						alt7=2;
					}
					else if ( ((LA7_1 >= '\u0000' && LA7_1 <= '.')||(LA7_1 >= '0' && LA7_1 <= '\uFFFF')) ) {
						alt7=1;
					}

				}
				else if ( ((LA7_0 >= '\u0000' && LA7_0 <= ')')||(LA7_0 >= '+' && LA7_0 <= '\uFFFF')) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:67:11: .
					{
					matchAny(); 
					}
					break;

				default :
					break loop7;
				}
			}

			match("*/"); 

			 skip(); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:72:5: ( ( ' ' | '\\t' | '\\f' | '\\n' | '\\r' )+ )
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:72:7: ( ' ' | '\\t' | '\\f' | '\\n' | '\\r' )+
			{
			// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:72:7: ( ' ' | '\\t' | '\\f' | '\\n' | '\\r' )+
			int cnt8=0;
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( ((LA8_0 >= '\t' && LA8_0 <= '\n')||(LA8_0 >= '\f' && LA8_0 <= '\r')||LA8_0==' ') ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt8 >= 1 ) break loop8;
					EarlyExitException eee = new EarlyExitException(8, input);
					throw eee;
				}
				cnt8++;
			}

			 skip(); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	@Override
	public void mTokens() throws RecognitionException {
		// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:8: ( ARRAY | ASSIGNMENT_OP | BEGIN | BIT_AND | BIT_OR | BREAK | CLOSEBRACKET | CLOSEPAREN | COLON | COMMA | DIVIDE | DO | ELSE | END | ENDDO | ENDIF | EQUALS | FIXEDPT | FOR | FUNCTION | GREATER_THAN | GREATER_THAN_EQUAL | IF | INT | LESS_THAN | LESS_THAN_EQUAL | MAIN | MINUS | MULTIPLY | NOT_EQUAL | OF | OPENBRACKET | OPENPAREN | PLUS | RETURN | SEMICOLON | THEN | TO | TYPE | VAR | VOID | WHILE | ID | INTLIT | FIXEDPTLIT | COMMENT | WS )
		int alt9=47;
		alt9 = dfa9.predict(input);
		switch (alt9) {
			case 1 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:10: ARRAY
				{
				mARRAY(); 

				}
				break;
			case 2 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:16: ASSIGNMENT_OP
				{
				mASSIGNMENT_OP(); 

				}
				break;
			case 3 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:30: BEGIN
				{
				mBEGIN(); 

				}
				break;
			case 4 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:36: BIT_AND
				{
				mBIT_AND(); 

				}
				break;
			case 5 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:44: BIT_OR
				{
				mBIT_OR(); 

				}
				break;
			case 6 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:51: BREAK
				{
				mBREAK(); 

				}
				break;
			case 7 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:57: CLOSEBRACKET
				{
				mCLOSEBRACKET(); 

				}
				break;
			case 8 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:70: CLOSEPAREN
				{
				mCLOSEPAREN(); 

				}
				break;
			case 9 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:81: COLON
				{
				mCOLON(); 

				}
				break;
			case 10 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:87: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 11 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:93: DIVIDE
				{
				mDIVIDE(); 

				}
				break;
			case 12 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:100: DO
				{
				mDO(); 

				}
				break;
			case 13 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:103: ELSE
				{
				mELSE(); 

				}
				break;
			case 14 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:108: END
				{
				mEND(); 

				}
				break;
			case 15 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:112: ENDDO
				{
				mENDDO(); 

				}
				break;
			case 16 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:118: ENDIF
				{
				mENDIF(); 

				}
				break;
			case 17 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:124: EQUALS
				{
				mEQUALS(); 

				}
				break;
			case 18 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:131: FIXEDPT
				{
				mFIXEDPT(); 

				}
				break;
			case 19 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:139: FOR
				{
				mFOR(); 

				}
				break;
			case 20 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:143: FUNCTION
				{
				mFUNCTION(); 

				}
				break;
			case 21 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:152: GREATER_THAN
				{
				mGREATER_THAN(); 

				}
				break;
			case 22 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:165: GREATER_THAN_EQUAL
				{
				mGREATER_THAN_EQUAL(); 

				}
				break;
			case 23 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:184: IF
				{
				mIF(); 

				}
				break;
			case 24 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:187: INT
				{
				mINT(); 

				}
				break;
			case 25 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:191: LESS_THAN
				{
				mLESS_THAN(); 

				}
				break;
			case 26 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:201: LESS_THAN_EQUAL
				{
				mLESS_THAN_EQUAL(); 

				}
				break;
			case 27 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:217: MAIN
				{
				mMAIN(); 

				}
				break;
			case 28 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:222: MINUS
				{
				mMINUS(); 

				}
				break;
			case 29 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:228: MULTIPLY
				{
				mMULTIPLY(); 

				}
				break;
			case 30 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:237: NOT_EQUAL
				{
				mNOT_EQUAL(); 

				}
				break;
			case 31 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:247: OF
				{
				mOF(); 

				}
				break;
			case 32 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:250: OPENBRACKET
				{
				mOPENBRACKET(); 

				}
				break;
			case 33 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:262: OPENPAREN
				{
				mOPENPAREN(); 

				}
				break;
			case 34 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:272: PLUS
				{
				mPLUS(); 

				}
				break;
			case 35 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:277: RETURN
				{
				mRETURN(); 

				}
				break;
			case 36 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:284: SEMICOLON
				{
				mSEMICOLON(); 

				}
				break;
			case 37 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:294: THEN
				{
				mTHEN(); 

				}
				break;
			case 38 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:299: TO
				{
				mTO(); 

				}
				break;
			case 39 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:302: TYPE
				{
				mTYPE(); 

				}
				break;
			case 40 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:307: VAR
				{
				mVAR(); 

				}
				break;
			case 41 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:311: VOID
				{
				mVOID(); 

				}
				break;
			case 42 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:316: WHILE
				{
				mWHILE(); 

				}
				break;
			case 43 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:322: ID
				{
				mID(); 

				}
				break;
			case 44 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:325: INTLIT
				{
				mINTLIT(); 

				}
				break;
			case 45 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:332: FIXEDPTLIT
				{
				mFIXEDPTLIT(); 

				}
				break;
			case 46 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:343: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 47 :
				// /Users/craigrmccown/Documents/craigrmccown/cs-4240/src/main/antlr3/tiger/Tiger.g:1:351: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA9 dfa9 = new DFA9(this);
	static final String DFA9_eotS =
		"\1\uffff\1\35\1\43\1\35\5\uffff\1\47\2\35\1\uffff\1\35\1\57\1\35\1\64"+
		"\1\35\2\uffff\1\35\3\uffff\1\35\1\uffff\3\35\1\uffff\2\76\1\uffff\1\35"+
		"\2\uffff\2\35\2\uffff\1\104\5\35\2\uffff\1\112\1\35\3\uffff\1\35\1\115"+
		"\2\35\1\120\4\35\2\uffff\1\76\3\35\1\uffff\1\35\1\133\1\35\1\135\1\35"+
		"\1\uffff\1\137\1\35\1\uffff\2\35\1\uffff\1\35\1\144\5\35\1\152\2\35\1"+
		"\uffff\1\35\1\uffff\1\35\1\uffff\1\157\1\35\1\161\1\162\1\uffff\1\163"+
		"\1\35\1\165\1\166\1\167\1\uffff\1\170\1\171\2\35\1\uffff\1\35\3\uffff"+
		"\1\175\5\uffff\2\35\1\u0080\1\uffff\1\u0081\1\35\2\uffff\1\u0083\1\uffff";
	static final String DFA9_eofS =
		"\u0084\uffff";
	static final String DFA9_minS =
		"\1\11\1\162\1\75\1\145\5\uffff\1\52\1\157\1\154\1\uffff\1\151\1\75\1\146"+
		"\1\75\1\141\2\uffff\1\146\3\uffff\1\145\1\uffff\1\150\1\141\1\150\1\uffff"+
		"\2\56\1\uffff\1\162\2\uffff\1\147\1\145\2\uffff\1\60\1\163\1\144\1\170"+
		"\1\162\1\156\2\uffff\1\60\1\164\3\uffff\1\151\1\60\1\164\1\145\1\60\1"+
		"\160\1\162\2\151\2\uffff\1\56\1\141\1\151\1\141\1\uffff\1\145\1\60\1\145"+
		"\1\60\1\143\1\uffff\1\60\1\156\1\uffff\1\165\1\156\1\uffff\1\145\1\60"+
		"\1\144\1\154\1\171\1\156\1\153\1\60\1\157\1\146\1\uffff\1\144\1\uffff"+
		"\1\164\1\uffff\1\60\1\162\2\60\1\uffff\1\60\1\145\3\60\1\uffff\2\60\1"+
		"\160\1\151\1\uffff\1\156\3\uffff\1\60\5\uffff\1\164\1\157\1\60\1\uffff"+
		"\1\60\1\156\2\uffff\1\60\1\uffff";
	static final String DFA9_maxS =
		"\1\174\1\162\1\75\1\162\5\uffff\1\52\1\157\1\156\1\uffff\1\165\1\75\1"+
		"\156\1\76\1\141\2\uffff\1\146\3\uffff\1\145\1\uffff\1\171\1\157\1\150"+
		"\1\uffff\1\56\1\71\1\uffff\1\162\2\uffff\1\147\1\145\2\uffff\1\172\1\163"+
		"\1\144\1\170\1\162\1\156\2\uffff\1\172\1\164\3\uffff\1\151\1\172\1\164"+
		"\1\145\1\172\1\160\1\162\2\151\2\uffff\1\71\1\141\1\151\1\141\1\uffff"+
		"\1\145\1\172\1\145\1\172\1\143\1\uffff\1\172\1\156\1\uffff\1\165\1\156"+
		"\1\uffff\1\145\1\172\1\144\1\154\1\171\1\156\1\153\1\172\1\157\1\146\1"+
		"\uffff\1\144\1\uffff\1\164\1\uffff\1\172\1\162\2\172\1\uffff\1\172\1\145"+
		"\3\172\1\uffff\2\172\1\160\1\151\1\uffff\1\156\3\uffff\1\172\5\uffff\1"+
		"\164\1\157\1\172\1\uffff\1\172\1\156\2\uffff\1\172\1\uffff";
	static final String DFA9_acceptS =
		"\4\uffff\1\4\1\5\1\7\1\10\1\12\3\uffff\1\21\5\uffff\1\34\1\35\1\uffff"+
		"\1\40\1\41\1\42\1\uffff\1\44\3\uffff\1\53\2\uffff\1\57\1\uffff\1\2\1\11"+
		"\2\uffff\1\56\1\13\6\uffff\1\26\1\25\2\uffff\1\32\1\36\1\31\11\uffff\1"+
		"\54\1\55\4\uffff\1\14\5\uffff\1\27\2\uffff\1\37\2\uffff\1\46\12\uffff"+
		"\1\16\1\uffff\1\23\1\uffff\1\30\4\uffff\1\50\5\uffff\1\15\4\uffff\1\33"+
		"\1\uffff\1\45\1\47\1\51\1\uffff\1\1\1\3\1\6\1\17\1\20\3\uffff\1\52\2\uffff"+
		"\1\43\1\22\1\uffff\1\24";
	static final String DFA9_specialS =
		"\u0084\uffff}>";
	static final String[] DFA9_transitionS = {
			"\2\40\1\uffff\2\40\22\uffff\1\40\5\uffff\1\4\1\uffff\1\26\1\7\1\23\1"+
			"\27\1\10\1\22\1\uffff\1\11\1\36\11\37\1\2\1\31\1\20\1\14\1\16\2\uffff"+
			"\32\35\1\25\1\uffff\1\6\3\uffff\1\1\1\3\1\35\1\12\1\13\1\15\2\35\1\17"+
			"\3\35\1\21\1\35\1\24\2\35\1\30\1\35\1\32\1\35\1\33\1\34\3\35\1\uffff"+
			"\1\5",
			"\1\41",
			"\1\42",
			"\1\44\14\uffff\1\45",
			"",
			"",
			"",
			"",
			"",
			"\1\46",
			"\1\50",
			"\1\51\1\uffff\1\52",
			"",
			"\1\53\5\uffff\1\54\5\uffff\1\55",
			"\1\56",
			"\1\60\7\uffff\1\61",
			"\1\62\1\63",
			"\1\65",
			"",
			"",
			"\1\66",
			"",
			"",
			"",
			"\1\67",
			"",
			"\1\70\6\uffff\1\71\11\uffff\1\72",
			"\1\73\15\uffff\1\74",
			"\1\75",
			"",
			"\1\77",
			"\1\77\1\uffff\12\100",
			"",
			"\1\101",
			"",
			"",
			"\1\102",
			"\1\103",
			"",
			"",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\105",
			"\1\106",
			"\1\107",
			"\1\110",
			"\1\111",
			"",
			"",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\113",
			"",
			"",
			"",
			"\1\114",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\116",
			"\1\117",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\121",
			"\1\122",
			"\1\123",
			"\1\124",
			"",
			"",
			"\1\77\1\uffff\12\100",
			"\1\125",
			"\1\126",
			"\1\127",
			"",
			"\1\130",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\3\35\1\131\4\35\1\132\21\35",
			"\1\134",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\136",
			"",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\140",
			"",
			"\1\141",
			"\1\142",
			"",
			"\1\143",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\145",
			"\1\146",
			"\1\147",
			"\1\150",
			"\1\151",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\153",
			"\1\154",
			"",
			"\1\155",
			"",
			"\1\156",
			"",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\160",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\164",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\172",
			"\1\173",
			"",
			"\1\174",
			"",
			"",
			"",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"",
			"",
			"",
			"",
			"",
			"\1\176",
			"\1\177",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\u0082",
			"",
			"",
			"\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			""
	};

	static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
	static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
	static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
	static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
	static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
	static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
	static final short[][] DFA9_transition;

	static {
		int numStates = DFA9_transitionS.length;
		DFA9_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
		}
	}

	protected class DFA9 extends DFA {

		public DFA9(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 9;
			this.eot = DFA9_eot;
			this.eof = DFA9_eof;
			this.min = DFA9_min;
			this.max = DFA9_max;
			this.accept = DFA9_accept;
			this.special = DFA9_special;
			this.transition = DFA9_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( ARRAY | ASSIGNMENT_OP | BEGIN | BIT_AND | BIT_OR | BREAK | CLOSEBRACKET | CLOSEPAREN | COLON | COMMA | DIVIDE | DO | ELSE | END | ENDDO | ENDIF | EQUALS | FIXEDPT | FOR | FUNCTION | GREATER_THAN | GREATER_THAN_EQUAL | IF | INT | LESS_THAN | LESS_THAN_EQUAL | MAIN | MINUS | MULTIPLY | NOT_EQUAL | OF | OPENBRACKET | OPENPAREN | PLUS | RETURN | SEMICOLON | THEN | TO | TYPE | VAR | VOID | WHILE | ID | INTLIT | FIXEDPTLIT | COMMENT | WS );";
		}
	}

}
