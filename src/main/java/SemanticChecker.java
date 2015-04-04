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
        subTree.setCurrentScope(currentScope);

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

            TigerTree variableTree;
            Symbol symbol, typeSymbol;
            Scope typeScope;

            variableTree = (TigerTree) subTree.getChild(0);

            try {
                symbol = currentScope.lookup(variableTree.toString());
                typeScope = currentScope.lookupDefinedTypeScope(symbol.getDataType());
            } catch (SymbolNotFoundException e) {
                raiseError("undefined symbol named '" + e.getSymbolName() + "'", subTree.getLine());
                return;
            }

            if (typeScope == null) {
                typeSymbol = symbol;
            } else {
                typeSymbol = typeScope.getSymbol(symbol.getDataType());
            }

            if (variableTree.getChildren() == null) {
                subTree.setDataType(symbol.getDataType(), typeScope);
            } else if (typeSymbol.getSymbolType() == Symbol.ARRAYTYPE) {
                if (variableTree.getChildren().size() != 1) {
                    raiseError("one dimensional array types must be dereferenced exactly once, or not at all.", subTree.getLine());
                }

                subTree.setDataType(typeSymbol.getDataType(), null);
            } else if (typeSymbol.getSymbolType() == Symbol.ARRAY2DTYPE) {
                if (variableTree.getChildren().size() != 2) {
                    raiseError("two dimensional array types must be dereferenced exactly twice, or not at all.", subTree.getLine());
                }

                subTree.setDataType(typeSymbol.getDataType(), null);
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
                try {
                    symbolTable.handleSymbolDeclaration(currentScope, childTree);
                } catch (DuplicateSymbolException e) {
                    raiseError("duplicate declaration detected for symbol '" + e.getSymbol() + "'", childTree.getLine());
                } catch (SymbolNotFoundException e) {
                    raiseError("undefined symbol named '" + e.getSymbolName() + "'", childTree.getLine());
                }
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
            dataType = resolveLeftTypeEquivalence(left, right);

            if (dataType == null) {
                raiseError("cannot assign value of type '" + right.getDataType() + "' to variable of type '" + left.getDataType() + "'", subTree.getLine());
            }
        } else if (subTree.isArithmeticOperator() || subTree.isConditionalOperator()) {
            // set the data type of the current tree by
            // resolving the types of its left and right child.
            // if resolution fails, an error is raised. the
            // data type is set to @boolean if the operator produces
            // a boolean expression.

            TigerTree left, right;
            String dataType;

            left = (TigerTree) subTree.getChild(0);
            right = (TigerTree) subTree.getChild(1);
            dataType = resolveTypeEquivalence(left, right);

            if (dataType != null) {
                if (subTree.isConditionalOperator()) {
                    subTree.setDataType("@boolean", null);
                } else {
                    subTree.setDataType(dataType, left.getDataTypeScope());
                }
            } else {
                raiseError("mismatched operands for binary operator '" + subTree.getText() + "'. got types '" + left.getDataType() + "' and '" + right.getDataType() + "'", subTree.getLine());
            }
        } else if (subTree.isReturnStatement()) {
            // set the return type of the parent to the type of
            // the return expression.

            TigerTree returnTree, parentTree;

            returnTree = (TigerTree) subTree.getChild(0);
            parentTree = (TigerTree) subTree.parent;

            if (parentTree.getReturnType() != null) {
                raiseError("unreachable return statement", subTree.getLine());
            } else {
                parentTree.setReturnType(returnTree.getDataType(), returnTree.getDataTypeScope());
            }
        } else if (subTree.isFunctionDeclaration()) {
            // check that the return type of the function body matches
            // the return type of the function.

            Scope declarationScope, declarationTypeScope;
            Symbol declarationSymbol;

            try {
                declarationScope = currentScope.lookupScope(subTree.getChild(1).toString());
                declarationSymbol = declarationScope.getSymbol(subTree.getChild(1).toString());
                declarationTypeScope = declarationScope.lookupDefinedTypeScope(declarationSymbol.getDataType());
            } catch (SymbolNotFoundException e) {
                raiseError("undefined symbol named '" + e.getSymbolName() + "'", subTree.getLine());
                return;
            }

            if (declarationSymbol.getDataType().equals("VOID")) {
                if (subTree.getReturnType() != null) {
                    raiseError("void function cannot return a value", subTree.getLine());
                }
            } else {
                if (subTree.getReturnType() == null) {
                    raiseError("non-void function must return a value", subTree.getLine());
                } else if (!subTree.getReturnType().equals(declarationSymbol.getDataType()) || subTree.getReturnTypeScope() != declarationTypeScope) {
                    raiseError("function contains return statements with mismatched types", subTree.getLine());
                }
            }
        } else if (subTree.isOptionalInit()) {
            // check that the data type of the var declaration matches
            // the type of the initialization constant. only data type
            // names, not scopes need to be checked for type equality
            // because the initializer must be a constant of type fixedpt
            // or int.

            TigerTree constantTree, typeTree;
            String dataType;
            Symbol typeSymbol;

            constantTree = (TigerTree) subTree.getChild(0);
            typeTree = (TigerTree) subTree.parent.getChild(0);

            dataType = resolveLeftDataTypeNames(typeTree.toString(), constantTree.getDataType());

            if (dataType == null) {
                try {
                    typeSymbol = currentScope.lookup(typeTree.toString());
                } catch (SymbolNotFoundException e) {
                    raiseError("undefined symbol named '" + e.getSymbolName() + "'", subTree.getLine());
                    return;
                }

                if (typeSymbol == null || resolveLeftDataTypeNames(typeSymbol.getDataType(), constantTree.getDataType()) == null) {
                    raiseError("cannot assign value of type '" + constantTree.getDataType() + "' to variable of type '" + typeTree.toString() + "'", subTree.getLine());
                }
            }
        } else if (subTree.isFunctionCall()) {
            // check that the number and type of parameters match those
            // of the function declaration and set the correct data type
            // to the function call.

            TigerTree functionCallTree;
            Scope functionScope, typeScope;
            Symbol functionSymbol;
            int numArgs;

            try {
                functionCallTree = (TigerTree) subTree.getChild(0);
                functionScope = currentScope.lookupScope(functionCallTree.toString());
                functionSymbol = functionScope.getSymbol(functionCallTree.toString());
                typeScope = functionScope.lookupDefinedTypeScope(functionSymbol.getDataType());
            } catch (SymbolNotFoundException e) {
                raiseError("undefined symbol named '" + e.getSymbolName() + "'", subTree.getLine());
                return;
            }

            numArgs = 0;

            if (functionCallTree.getChildren() != null) {
                numArgs = functionCallTree.getChildren().size();
            }

            if (numArgs != functionSymbol.getNumParameters()) {
                raiseError("wrong number of arguments passed to function '" + functionSymbol.getName() + "'", subTree.getLine());
            }

            TigerTree parameterTree;
            String dataType;
            Scope parameterTypeScope;

            for (int i = 0; i < functionSymbol.getNumParameters(); i++) {
                parameterTree = (TigerTree) functionCallTree.getChild(i);
                dataType = resolveLeftDataTypeNames(functionSymbol.getParameter(i).getDataType(), parameterTree.getDataType());

                try {
                    parameterTypeScope = functionScope.lookupDefinedTypeScope(functionSymbol.getParameter(i).getDataType());
                } catch (SymbolNotFoundException e) {
                    raiseError("undefined symbol named '" + e.getSymbolName() + "'", subTree.getLine());
                    return;
                }

                if (dataType == null || parameterTypeScope != parameterTree.getDataTypeScope()) {
                    raiseError("argument types do not match parameter types in function definition", subTree.getLine());
                }
            }

            subTree.setDataType(functionSymbol.getDataType(), typeScope);
        } else if (subTree.isWhileLoop()) {
            // check that the while loop header is a boolean expression

            TigerTree whileTree = (TigerTree) subTree.getChild(0);

            if (!whileTree.getDataType().equals("@boolean")) {
                raiseError("boolean expression required in while loop header, got '" + whileTree.getDataType() + "'", subTree.getLine());
            }
        } else if (subTree.isIfStatement()) {
            // check that the if statement header is a boolean expression

            TigerTree ifTree = (TigerTree) subTree.getChild(0);

            if (!ifTree.getDataType().equals("@boolean")) {
                raiseError("boolean expression required in if statement header, got '" + ifTree.getDataType() + "'", subTree.getLine());
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
                returnType = resolveReturnTypeEquivalence(parentTree, subTree);

                if (returnType != null) {
                    parentTree.setReturnType(returnType, subTree.getReturnTypeScope());
                } else {
                    raiseError("function returns conflicting types", subTree.getLine());
                }
            } else {
                parentTree.setReturnType(subTree.getReturnType(), subTree.getReturnTypeScope());
            }
        }
    }

    private String resolveTypeEquivalence(TigerTree left, TigerTree right) {
        String dataType = resolveDataTypeNames(left.getDataType(), right.getDataType());

        if (left.getDataTypeScope() == right.getDataTypeScope()) {
            return dataType;
        } else {
            return null;
        }
    }

    private String resolveLeftTypeEquivalence(TigerTree left, TigerTree right) {
        String dataType = resolveLeftDataTypeNames(left.getDataType(), right.getDataType());

        if (left.getDataTypeScope() == right.getDataTypeScope()) {
            return dataType;
        } else {
            return null;
        }
    }

    private String resolveReturnTypeEquivalence(TigerTree left, TigerTree right) {
        String returnType = resolveDataTypeNames(left.getReturnType(), right.getReturnType());

        if (left.getReturnTypeScope() == right.getReturnTypeScope()) {
            return returnType;
        } else {
            return null;
        }
    }

    private String resolveDataTypeNames(String left, String right) {
        // resolves two data types. if mixed 'int' and 'fixedpt',
        // return fixedpt. if the two types are equal, return that
        // type. otherwise, return null.

        if (
            left.equals("fixedpt") && right.equals("fixedpt") ||
            left.equals("int") && right.equals("fixedpt") ||
            left.equals("fixedpt") && right.equals("int")
        ) {
            return "fixedpt";
        } else if (left.equals(right)) {
            return left;
        } else {
            return null;
        }
    }

    private String resolveLeftDataTypeNames(String left, String right) {
        String dataType = resolveDataTypeNames(left, right);

        if (dataType != null && dataType.equals(left)) {
            return dataType;
        } else {
            return null;
        }
    }

    private void raiseError(String message, int lineNumber) {
        throw new RuntimeException("error on line " + lineNumber + ": " + message + ".");
    }
}