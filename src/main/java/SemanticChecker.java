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
                Symbol typeSymbol = currentScope.lookupDataType(symbol);
                String dataType;

                if (typeSymbol.getSymbolType() == Symbol.ARRAYTYPE && variableTree.getChildren() != null) {
                    if (variableTree.getChildren().size() == 1) {
                        dataType = typeSymbol.getDataType();
                    } else {
                        throw new RuntimeException("error on line " + subTree.getLine() + ": one dimensional array types must be dereferenced exactly once, or not at all.");
                    }
                } else if (typeSymbol.getSymbolType() == Symbol.ARRAY2DTYPE && variableTree.getChildren() != null) {
                    if (variableTree.getChildren().size() == 2) {
                        dataType = typeSymbol.getDataType();
                    } else {
                        throw new RuntimeException("error on line " + subTree.getLine() + ": two dimensional array types must be dereferenced exactly twice, or not at all.");
                    }
                } else {
                    dataType = symbol.getDataType();
                }

                subTree.setDataType(dataType);
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
                    throw new RuntimeException("error on line " + subTree.getLine() + ": mismatched operands for binary operator '" + subTree.getText() + "'");
                }
            } else if (subTree.isReturnStatement()) {
                TigerTree returnTree = (TigerTree) subTree.getChild(0);
                Symbol functionSymbol = currentScope.getFunctionDeclaration();

                if (functionSymbol.getDataType().equals("VOID")) {
                    throw new RuntimeException("error on line " + subTree.getLine() + ": cannot return value from void function '" + functionSymbol.getName() + "'.");
                } else if (!returnTree.getDataType().equals(functionSymbol.getDataType())) {
                    throw new RuntimeException("error on line " + subTree.getLine() + ": mismatched return type. '" + functionSymbol.getDataType() + "' expected.");
                }
            } else if (subTree.isFunctionCall()) {
                TigerTree functionCallTree = (TigerTree) subTree.getChild(0);
                Symbol functionSymbol = currentScope.lookup(functionCallTree.toString());
                int numArgs = 0;

                if (functionCallTree.getChildren() != null) {
                    numArgs = functionCallTree.getChildren().size();
                }

                if (numArgs != functionSymbol.getNumParameters()) {
                    throw new RuntimeException("you suck");
                }

                for (int i = 0; i < functionSymbol.getNumParameters(); i ++) {
                    if (!functionSymbol.getParameter(i).getDataType().equals(((TigerTree) functionCallTree.getChild(i)).getDataType())) {
                        throw new RuntimeException("error on line " + subTree.getLine() + ": argument types do not match parameter types in funciton definition.");
                    }
                }
            }
        }
    }
}
