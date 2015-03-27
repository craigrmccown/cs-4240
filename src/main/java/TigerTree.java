import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import java.util.ArrayList;
import java.util.List;

public class TigerTree extends CommonTree {
    private String functionKey;

    public TigerTree(Token token) {
        super(token);
    }

    public boolean isFunctionBody() {
        return getType() == TigerLexer.BLOCK && parent.getType() == TigerLexer.FUNCTION;
    }

    public String getFunctionKey() {
        return this.functionKey = parent.getChild(1).toString();
    }
}