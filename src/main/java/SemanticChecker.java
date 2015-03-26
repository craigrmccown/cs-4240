import org.antlr.runtime.tree.BaseTree;

public class SemanticChecker {
    private SymbolTable symbolTable;

    public SemanticChecker() {
        this.symbolTable = new SymbolTable();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void check(BaseTree parseTree) {
        check(parseTree, symbolTable.getRootScope());
    }

    private void check(BaseTree subTree, Scope currentScope) {
        if (subTree.getChildren() != null) {
            for (int i = 0; i < subTree.getChildren().size(); i ++) {
                if (subTree.getChild(i) instanceof BaseTree) {
                    Scope nextScope;

                    if (subTree.getChild(i).toString().equals("BLOCK")) {
                        nextScope = symbolTable.addChildScope(currentScope);
                    } else {
                        nextScope = currentScope;
                        symbolTable.addSymbol(currentScope, (BaseTree) subTree.getChild(i));
                    }

                    check((BaseTree) subTree.getChild(i), nextScope);
                } else {
                    // terminal
                }
            }
        }
    }
}
