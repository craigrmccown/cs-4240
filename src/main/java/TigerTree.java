import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

public class TigerTree extends CommonTree {
    private String dataType;

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

    public boolean isBlock() {
        return getType() == TigerLexer.BLOCK;
    }

    public boolean isSymbolDeclaration() {
        return getType() == TigerLexer.TYPE || getType() == TigerLexer.VAR || getType() == TigerLexer.FUNCTION;
    }

    public boolean isLiteral() {
        return getType() == TigerLexer.FIXEDPTLIT || getType() == TigerLexer.INTLIT;
    }

    public boolean isBinaryOperator() {
        return getType() == TigerLexer.PLUS ||
                getType() == TigerLexer.MINUS ||
                getType() == TigerLexer.MULTIPLY ||
                getType() == TigerLexer.DIVIDE ||
                getType() == TigerLexer.BIT_AND ||
                getType() == TigerLexer.BIT_OR ||
                getType() == TigerLexer.EQUALS;
    }

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

    public String getFunctionKey() {
        return isFunctionBody() ? parent.getChild(1).toString() : null;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }
}