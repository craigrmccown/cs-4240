public class SemanticChecker {
    private SymbolTable symbolTable;
    private IRGenerator generator;

    public SemanticChecker() {
        this.symbolTable = new SymbolTable();
        this.generator = new IRGenerator(this.symbolTable);
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
            // set the data type of the variable reference to the
            // correct type. if the variable reference includes an
            // array dereference, first check if the dereference is
            // legal.

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
        } else if (subTree.isFunctionCall()) {
            // check that the number and type of parameters match those
            // of the function declaration and set the correct data type
            // to the function call.

            TigerTree functionCallTree = (TigerTree) subTree.getChild(0);
            Symbol functionSymbol = currentScope.lookup(functionCallTree.toString());
            int numArgs = 0;

            if (functionCallTree.getChildren() != null) {
                numArgs = functionCallTree.getChildren().size();
            }

            if (numArgs != functionSymbol.getNumParameters()) {
                raiseError("wrong number of arguments passed to function '" + functionSymbol.getName() + "'", subTree.getLine());
            }

            for (int i = 0; i < functionSymbol.getNumParameters(); i++) {
                String dataType = resolveDataTypes(functionSymbol.getParameter(i).getDataType(), ((TigerTree) functionCallTree.getChild(i)).getDataType());

                if (dataType == null || !dataType.equals(functionSymbol.getParameter(i).getDataType())) {
                    raiseError("argument types do not match parameter types in function definition", subTree.getLine());
                }
            }

            subTree.setDataType(functionSymbol.getDataType());
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
        if (subTree.isAssignmentOperator()) {
            // check that the right side of the assignment
            // can resolve to the same type of the left side
            // of the assignment.

            TigerTree left, right;
            String dataType;

            left = (TigerTree) subTree.getChild(0);
            right = (TigerTree) subTree.getChild(1);
            dataType = resolveDataTypes(left.getDataType(), right.getDataType());

            if (dataType == null || !dataType.equals(left.getDataType())) {
                raiseError("cannot assign value of type '" + right.getDataType() + "' to variable of type '" + left.getDataType() + "'", subTree.getLine());
            }
        } else if (subTree.isBinaryOperator()) {
            // set the data type of the current tree by
            // resolving the types of its left and right child.
            // if resolution fails, an error is raised.

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
            // set the return type of the parent to the type of
            // the return expression.

            TigerTree returnTree, parentTree;

            returnTree = (TigerTree) subTree.getChild(0);
            parentTree = (TigerTree) subTree.parent;

            parentTree.setReturnType(returnTree.getDataType());

        } else if (subTree.isFunctionDeclaration()) {
            // check that the return type of the function body matches
            // the return type of the function.

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
            // propagate the return type attribute up the tree. if
            // the parent already has a return type, check if the existing
            // return type conflicts with the current node's return type.

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

    private void secondPass(TigerTree subTree) {
        generate((TigerTree) subTree);
    }

    private String generate(TigerTree subTree) {
        int opcode = -1;
        if (subTree.isArithmeticOperator()) {
            switch (subTree.getType()) {
                case TigerLexer.PLUS:
                    opcode = IntermediateCode.ADD;
                    break;
                case TigerLexer.MINUS:
                    opcode = IntermediateCode.SUB;
                    break;
                case TigerLexer.MULTIPLY:
                    opcode = IntermediateCode.MULT;
                    break;
                case TigerLexer.DIVIDE:
                    opcode = IntermediateCode.DIV;
                    break;
                default:

            }
            String t1 = generate((TigerTree) subTree.getChild(0));
            String t2 = generate((TigerTree) subTree.getChild(0));
            String t3 = generator.createTemp(null);
            generator.emit(opcode, t1, t2, t3);
            return t3;
        } else if (subTree.isBooleanOperator()) {
            switch (subTree.getType()) {
                case TigerLexer.BIT_AND:
                    opcode = IntermediateCode.AND;
                    break;
                case TigerLexer.BIT_OR:
                    opcode = IntermediateCode.OR;
                    break;
                default:
                    System.out.println("something is seriously broken");
            }

        }
        return "";
    }

    private String resolveDataTypes(String dataType1, String dataType2) {
        // resolves two data types. if mixed 'int' and 'fixedpt',
        // return fixedpt. if the two types are equal, return that
        // type. otherwise, return null.

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