import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

public class TigerTree extends CommonTree {
    private String dataType;
    private String returnType;
    private Scope dataTypeScope;
    private Scope returnTypeScope;
    private Scope currentScope;

    public TigerTree(Token token) {
        super(token);

        if (token == null) {
            this.dataType = null;
        } else if (token.getType() == TigerLexer.INTLIT) {
            this.dataType = "int";
        } else if (token.getType() == TigerLexer.FIXEDPTLIT) {
            this.dataType = "fixedpt";
        } else {
            this.dataType = null;
        }
    }

    public boolean isArray() {
        //shhh, this is easier
        if (getChildCount() > 0) {
            return getChild(0).getChildCount() > 0;
        }
        return false;
    }

    public boolean isBlock() {
        return getType() == TigerLexer.BLOCK;
    }

    public boolean isSymbolDeclaration() {
        return getType() == TigerLexer.TYPE || getType() == TigerLexer.VAR || getType() == TigerLexer.FUNCTION;
    }

    public boolean isVariableDeclaration() {
        return getType() == TigerLexer.VAR;
    }

    public boolean isTypeDeclaration() {
        return getType() == TigerLexer.TYPE;
    }

    public boolean isLiteral() {
        return getType() == TigerLexer.FIXEDPTLIT || getType() == TigerLexer.INTLIT;
    }

    public boolean isArithmeticOperator() {
        return getType() == TigerLexer.PLUS ||
                getType() == TigerLexer.MINUS ||
                getType() == TigerLexer.MULTIPLY ||
                getType() == TigerLexer.DIVIDE;
    }

    public boolean isConditionalOperator() {
        return getType() == TigerLexer.BIT_AND ||
                getType() == TigerLexer.BIT_OR ||
                getType() == TigerLexer.NOT_EQUAL ||
                getType() == TigerLexer.LESS_THAN ||
                getType() == TigerLexer.LESS_THAN_EQUAL ||
                getType() == TigerLexer.GREATER_THAN ||
                getType() == TigerLexer.GREATER_THAN_EQUAL ||
                getType() == TigerLexer.EQUALS;
    }

    public boolean isBooleanOperator() {
        return getType() == TigerLexer.BIT_AND ||
                getType() == TigerLexer.BIT_OR;
    }

    public boolean isAssignmentOperator() {
        return getType() == TigerLexer.ASSIGNMENT_OP;
    }

    public boolean isAndOperator() { return getType() == TigerLexer.BIT_AND; }

    public boolean isOrOperator() { return getType() == TigerLexer.BIT_OR; }

    public boolean isOptionalInit() { return getType() == TigerLexer.OPTIONAL_INIT; }

    public boolean isVariableReference() {
        return getType() == TigerLexer.VALUE;
    }

    public boolean isReturnStatement() {
        return getType() == TigerLexer.RETURN;
    }

    public boolean isFunctionCall() {
        return getType() == TigerLexer.FUNCTION_CALL;
    }

    public boolean isFunctionBody() {
        return isBlock() && parent.getType() == TigerLexer.FUNCTION;
    }

    public boolean isFunctionDeclaration() { return getType() == TigerLexer.FUNCTION; }

    public boolean isWhileLoop() {
        return getType() == TigerLexer.WHILE;
    }

    public boolean isForLoop() {
        return getType() == TigerLexer.FOR;
    }

    public boolean isIfStatement() {
        return getType() == TigerLexer.IF;
    }

    public String getFunctionKey() {
        return isFunctionBody() ? parent.getChild(1).toString() : null;
    }

    public void setDataType(String dataType, Scope dataTypeScope) {
        this.dataType = dataType;
        this.dataTypeScope = dataTypeScope;
    }

    public String getDataType() {
        return dataType;
    }

    public void setReturnType(String returnType, Scope returnTypeScope) {
        this.returnType = returnType;
        this.returnTypeScope = returnTypeScope;
    }

    public String getReturnType() {
        return returnType;
    }

    public Scope getDataTypeScope() {
        return dataTypeScope;
    }

    public Scope getReturnTypeScope() {
        return returnTypeScope;
    }

    public void setCurrentScope(Scope currentScope) {
        this.currentScope = currentScope;
    }

    public Scope getCurrentScope() {
        return currentScope;
    }
}