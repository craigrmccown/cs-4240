import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

public class TigerTree extends CommonTree {
    public TigerTree(Token token) {
        super(token);
    }

    public boolean isBlock() {
        return getType() == TigerLexer.BLOCK;
    }

    public boolean isVariableReference() {
        return getType() == TigerLexer.ID && parent.getType() != TigerLexer.PARAM;
    }

    public boolean isSymbolDeclaration() {
        return getType() == TigerLexer.TYPE || getType() == TigerLexer.VAR || getType() == TigerLexer.FUNCTION;
    }

    public boolean isFunctionBody() {
        return isBlock() && parent.getType() == TigerLexer.FUNCTION;
    }

    public String getFunctionKey() {
        return parent.getChild(1).toString();
    }
}