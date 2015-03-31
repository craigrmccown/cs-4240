import org.antlr.runtime.tree.Tree;

public class SemanticChecker {
    private SymbolTable symbolTable;

    public SemanticChecker() {
        this.symbolTable = new SymbolTable();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void check(TigerTree parseTree) {
        firstPass(parseTree, symbolTable.getRootScope());
    }

    private void firstPass(TigerTree subTree, Scope currentScope) {
        if (subTree.getChildren() != null) {
            // base cases
            if (subTree.isVariableReference()) {
                TigerTree variableTree = (TigerTree) subTree.getChild(0);
                Symbol symbol = currentScope.lookup(variableTree.toString());
                subTree.setDataType(symbol.getDataType());
                return;
            } else if (subTree.isLiteral()) {
                return;
            }

            // recurse to children and build symbol table
            for (int i = 0; i < subTree.getChildren().size(); i ++) {
                TigerTree childTree = (TigerTree) subTree.getChild(i);
                Scope nextScope = currentScope;

                if (childTree.isBlock()) {
                    nextScope = symbolTable.addChildScope(currentScope, childTree.getFunctionKey());
                } else if (childTree.isSymbolDeclaration()) {
                    symbolTable.addSymbol(currentScope, childTree);
                }

                firstPass(childTree, nextScope);
            }

            // post order traversal
            if (subTree.isBinaryOperator()) {
                TigerTree left, right;
                left = (TigerTree) subTree.getChild(0);
                right = (TigerTree) subTree.getChild(1);

                if (
                    (left.getDataType().equals("fixedpt") && right.getDataType().equals("fixedpt")) ||
                    (left.getDataType().equals("int") && right.getDataType().equals("fixedpt")) ||
                    (left.getDataType().equals("fixedpt") && right.getDataType().equals("int"))
                ) {
                    subTree.setDataType("fixedpt");
                } else if (left.getDataType().equals(right.getDataType())) {
                    subTree.setDataType(left.getDataType());
                } else {
                    throw new RuntimeException("error on line " + subTree.getLine() + ": unsupported type for binary operator '" + subTree.getText() + "'");
                }
            }
        }
    }
}
