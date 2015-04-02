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
        // base cases
        if (subTree.getChildren() == null) {
            return;
        } else if (subTree.isLiteral()) {
            return;
        } else if (subTree.isVariableReference()) {
            TigerTree variableTree = (TigerTree) subTree.getChild(0);
            Symbol symbol = currentScope.lookup(variableTree.toString());
            Symbol typeSymbol = currentScope.lookupDataType(symbol);

            if (variableTree.getChildren() == null) {
                subTree.setDataType(symbol.getDataType());
            } else if (typeSymbol.getSymbolType() == Symbol.ARRAYTYPE) {
                if (variableTree.getChildren().size() != 1) {
                    raiseError("one dimensional array types must be dereferenced exactly once, or not at all.", subTree.getLine());
                }

                subTree.setDataType(typeSymbol.getDataType());
            } else if (typeSymbol.getSymbolType() == Symbol.ARRAY2DTYPE) {
                if (variableTree.getChildren().size() != 2) {
                    raiseError("two dimensional array types must be dereferenced exactly twice, or not at all.", subTree.getLine());
                }

                subTree.setDataType(typeSymbol.getDataType());
            }

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
            String dataType;

            left = (TigerTree) subTree.getChild(0);
            right = (TigerTree) subTree.getChild(1);
            dataType = resolveDataTypes(left.getDataType(), right.getDataType());

            if (dataType != null) {
                subTree.setDataType(dataType);
            } else {
                raiseError("mismatched operands for binary operator '" + subTree.getText() + "'", subTree.getLine());
            }
        } else if (subTree.isReturnStatement()) {
            TigerTree returnTree, parentTree;

            returnTree = (TigerTree) subTree.getChild(0);
            parentTree = (TigerTree) subTree.parent;

            parentTree.setReturnType(returnTree.getDataType());
        } else if (subTree.isFunctionCall()) {
            TigerTree functionCallTree = (TigerTree) subTree.getChild(0);
            Symbol functionSymbol = currentScope.lookup(functionCallTree.toString());
            int numArgs = 0;

            if (functionCallTree.getChildren() != null) {
                numArgs = functionCallTree.getChildren().size();
            }

            if (numArgs != functionSymbol.getNumParameters()) {
                raiseError("wrong number of arguments passed to function '" + functionSymbol.getName() + "'", subTree.getLine());
            }

            for (int i = 0; i < functionSymbol.getNumParameters(); i ++) {
                if (!functionSymbol.getParameter(i).getDataType().equals(((TigerTree) functionCallTree.getChild(i)).getDataType())) {
                    raiseError("argument types do not match parameter types in function definition", subTree.getLine());
                }
            }
        } else if (subTree.isFunctionDeclaration()) {
            Symbol declarationSymbol = currentScope.lookup(subTree.getChild(1).toString());

            if (declarationSymbol.getDataType().equals("VOID")) {
                if (subTree.getReturnType() != null) {
                    raiseError("void function cannot return a value", subTree.getLine());
                }
            } else {
                if (subTree.getReturnType() == null) {
                    raiseError("non-void function must return a value", subTree.getLine());
                } else if (!declarationSymbol.getDataType().equals(resolveDataTypes(declarationSymbol.getDataType(), subTree.getReturnType()))) {
                    raiseError("function contains return statements with mismatched types", subTree.getLine());
                }
            }
        }

        if (subTree.getReturnType() != null && !subTree.isFunctionDeclaration()) {
            TigerTree parentTree;
            String returnType;

            parentTree = (TigerTree) subTree.parent;

            if (parentTree.getReturnType() != null) {
                returnType = resolveDataTypes(parentTree.getReturnType(), subTree.getReturnType());

                if (returnType != null) {
                    parentTree.setReturnType(returnType);
                } else {
                    raiseError("function returns conflicting types", subTree.getLine());
                }
            } else {
                parentTree.setReturnType(subTree.getReturnType());
            }
        }
    }

    private String resolveDataTypes(String dataType1, String dataType2) {
        if (
            dataType1.equals("fixedpt") && dataType2.equals("fixedpt") ||
            dataType1.equals("int") && dataType2.equals("fixedpt") ||
            dataType1.equals("fixedpt") && dataType2.equals("int")
        ) {
            return "fixedpt";
        } else if (dataType1.equals(dataType2)) {
            return dataType1;
        } else {
            return null;
        }
    }

    private void raiseError(String message, int lineNumber) {
        throw new RuntimeException("error on line " + lineNumber + ": " + message + ".");
    }
}