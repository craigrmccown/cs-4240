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

    public IRGenerator getGenerator() {
        return generator;
    }

    public void check(TigerTree parseTree) {
        firstPass(parseTree, symbolTable.getRootScope());
        secondPass(parseTree);
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

            subTree.setDataType(dataType, left.getDataTypeScope());
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
        } else if (subTree.isForLoop()) {
            // check that the for loop 'to' expression is an int

            TigerTree toTree, left, right;
            String dataType;

            toTree = (TigerTree) subTree.getChild(0);
            left = (TigerTree) toTree.getChild(0);
            right = (TigerTree) toTree.getChild(1);

            dataType = resolveDataTypeNames(left.getDataType(), right.getDataType());

            if (dataType == null || !dataType.equals("int")) {
                raiseError("for loop 'to' clause must produce an value of type 'int'", subTree.getLine());
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

    private void secondPass(TigerTree subTree) {
        generator.emit(IntermediateCode.GOTO, "main", "", "");
        generate(subTree, null);
    }

    private String generate(TigerTree subTree, String innerLoopLabel) {
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
                    System.out.println("Something is seriously broken");
            }
            String t1 = generate((TigerTree) subTree.getChild(0), innerLoopLabel);
            String t2 = generate((TigerTree) subTree.getChild(1), innerLoopLabel);
            String ret = createTemp(generator, subTree);
            generator.emit(opcode, t1, t2, ret);
            return ret;

        } else if (subTree.isAndOperator()) {

            String ret = createTemp(generator, subTree);
            String t1 = generate((TigerTree) subTree.getChild(0), innerLoopLabel);

            String label1 = generator.createLabel();
            generator.emit(IntermediateCode.ASSIGN, ret, t1, "");
            generator.emit(IntermediateCode.BREQ, t1, "0", label1);
            String t2 = generate((TigerTree) subTree.getChild(1), innerLoopLabel);
            generator.emit(IntermediateCode.AND, ret, t2, ret);
            generator.emitLabel(label1);

            return ret;

        } else if (subTree.isOrOperator()) {

            String label1 = generator.createLabel();
            String ret = createTemp(generator, subTree);
            //get truth value of left side
            String t1 = generate((TigerTree) subTree.getChild(0), innerLoopLabel);


            generator.emit(IntermediateCode.ASSIGN, ret, t1, "");
            generator.emit(IntermediateCode.BRNEQ, t1, "0", label1);
            String t2 = generate((TigerTree) subTree.getChild(1), innerLoopLabel);
            generator.emit(IntermediateCode.OR, ret, t2, ret);
            generator.emitLabel(label1);

            return ret;

        } else if (subTree.isConditionalOperator()) {
            switch (subTree.getType()) {
                case TigerLexer.LESS_THAN:
                    opcode = IntermediateCode.BRLT;
                    break;
                case TigerLexer.GREATER_THAN:
                    opcode = IntermediateCode.BRGT;
                    break;
                case TigerLexer.LESS_THAN_EQUAL:
                    opcode = IntermediateCode.BRLEQ;
                    break;
                case TigerLexer.GREATER_THAN_EQUAL:
                    opcode = IntermediateCode.BRGEQ;
                    break;
                case TigerLexer.EQUALS:
                    opcode = IntermediateCode.BREQ;
                    break;
                case TigerLexer.NOT_EQUAL:
                    opcode = IntermediateCode.BRNEQ;
                    break;
                default:
                    System.out.println("yeah that's not good");
            }
            //really not sure of the best way to do this,
            //currently assigning a temp var to 1, then if
            //the condition is true it skips over the assignment to 0.
            //the reason for this is that the truth false value of this could
            //matter if this is nested in other conditionals
            String label1 = generator.createLabel();
            String ret = createTemp(generator, subTree);
            generator.emit(IntermediateCode.ASSIGN, ret, "1", "");
            //generate both sides since there's no short circuiting here
            String t1 = generate((TigerTree) subTree.getChild(0), innerLoopLabel);
            String t2 = generate((TigerTree) subTree.getChild(1), innerLoopLabel);
            generator.emit(opcode, t1, t2, label1);
            generator.emit(IntermediateCode.ASSIGN, ret, "0", "");
            generator.emitLabel(label1);
            return ret;
        } else if (subTree.isIfStatement()) {

            String label1 = generator.createLabel();
            //evaluate conditional
            String t1 = generate((TigerTree) subTree.getChild(0), innerLoopLabel);
            //generate the then block
            generate((TigerTree) subTree.getChild(1).getChild(0), innerLoopLabel);
            //spot that the boolean check jumps to if false
            generator.emitLabel(label1);
            if (subTree.getChildCount() == 3) {
                //generate else block
                generate((TigerTree) subTree.getChild(2).getChild(0), innerLoopLabel);
            }
            return "";

        } else if (subTree.isWhileLoop()) {
            String label1 = generator.createLabel();
            String label2 = generator.createLabel();
            generator.emitLabel(label1);
            String t1 = generate((TigerTree) subTree.getChild(0), innerLoopLabel);
            generator.emit(IntermediateCode.BREQ, t1, "0", label2);
            generate((TigerTree) subTree.getChild(1), label2);
            generator.emit(IntermediateCode.GOTO, label1, "", "");
            generator.emitLabel(label2);
            return "";

        } else if (subTree.isForLoop()) {

            //this takes the form:
            //  for t1 := t2 to t3 do <body>

            String label1 = generator.createLabel();
            String label2 = generator.createLabel();
            //Yup, this is ugly. This is the index variable
            String t1 = generate((TigerTree) subTree.getChild(0).getChild(0).getChild(0), innerLoopLabel);
            //this is the initial value of the index variable
            String t2 = generate((TigerTree) subTree.getChild(0).getChild(0).getChild(1), innerLoopLabel);
            generator.emit(IntermediateCode.ASSIGN, t1, t2, "");
            generator.emitLabel(label1);
            //this is the end condition value
            String t3 = generate((TigerTree) subTree.getChild(0).getChild(1), innerLoopLabel);
            generator.emit(IntermediateCode.BRGT, t1, t3, label2);
            //generate body of loop
            generate((TigerTree) subTree.getChild(1), label2);
            generator.emit(IntermediateCode.ADD, t1, "1", t1);
            generator.emit(IntermediateCode.GOTO, label1, "", "");
            generator.emitLabel(label2);
            return "";

        } else if (subTree.isFunctionCall()) {
            // the only time this should be called is when
            // this is a statement by itself, it's not being
            // assigned to anything, the assignment operator
            // tree should handle this differently
            TigerTree paramTree = (TigerTree) subTree.getChild(0);
            String[] params = new String[paramTree.getChildCount()];

            for (int i = 0; i < subTree.getChildCount(); i++) {
                params[i] = generate((TigerTree) paramTree.getChild(i), innerLoopLabel);
            }

            Symbol functionSymbol;

            try {
                Scope functionScope = subTree.getCurrentScope().lookupScope(paramTree.toString());
                functionSymbol = functionScope.getSymbol(paramTree.toString());
            } catch (SymbolNotFoundException e) {
                raiseError("undefined symbol named '" + e.getSymbolName() + "'", subTree.getLine());
                return "";
            }

            if (!functionSymbol.getDataType().equals("VOID")) {
                String ret = createTemp(generator, subTree);
                generator.emitCallWithReturn(IntermediateCode.CALLR, paramTree.toString(), ret, params);
            } else {
                generator.emitCall(IntermediateCode.CALL, paramTree.toString(), params);
            }
            return "";

        } else if (subTree.isAssignmentOperator()) {

            TigerTree rightTree = (TigerTree) subTree.getChild(1);
            TigerTree leftTree = (TigerTree) subTree.getChild(0);

            String t1 = leftGenerate(leftTree);

            if (rightTree.isFunctionCall()) {

                TigerTree paramTree = (TigerTree) rightTree.getChild(0);
                String[] params = new String[paramTree.getChildCount()];

                for (int i = 0; i < paramTree.getChildCount(); i++) {
                    params[i] = generate((TigerTree) paramTree.getChild(i), innerLoopLabel);
                }
                if (leftTree.isArray()) {
                    String t2 = createTemp(generator, subTree);
                    generator.emitCallWithReturn(IntermediateCode.CALLR,
                            paramTree.toString(), t2, params);
                    generator.emit(IntermediateCode.ARRAY_STORE,
                            leftTree.getChild(0).toString(), t1, t2);
                } else {
                    generator.emitCallWithReturn(IntermediateCode.CALLR,
                            paramTree.toString(), t1, params);
                }

            } else {

                String t2 = generate(rightTree, innerLoopLabel);
                if (leftTree.isArray()) {
                    generator.emit(IntermediateCode.ARRAY_STORE,
                            leftTree.getChild(0).toString(), t1, t2);
                } else {
                    generator.emit(IntermediateCode.ASSIGN, t1, t2, "");
                }
            }
            return "";

        } else if (subTree.isLiteral()) {
            return subTree.toString();

        } else if (subTree.isID()) {
            return subTree.toString();
        } else if (subTree.isVariableReference()) {

            TigerTree variableTree = (TigerTree) subTree.getChild(0);

            if (subTree.isArray()) {

                Symbol symbol;
                Scope typeScope;
                try {
                    symbol = subTree.getCurrentScope().lookup(variableTree.toString());
                    typeScope = subTree.getCurrentScope().lookupDefinedTypeScope(symbol.getDataType());
                } catch (SymbolNotFoundException e) {
                    raiseError("undefined symbol named '" + e.getSymbolName() + "'", subTree.getLine());
                    return "";
                }

                Symbol typeSymbol = typeScope.getSymbol(symbol.getDataType());

                String t1 = createTemp(generator, subTree);
                String t2 = generate((TigerTree) variableTree.getChild(0), innerLoopLabel);

                if (typeSymbol.getSymbolType() == Symbol.ARRAYTYPE) {
                    generator.emit(IntermediateCode.ARRAY_LOAD, t1,
                            variableTree.toString(), t2);
                } else {
                    //array flattening
                    String t3 = generate((TigerTree) variableTree.getChild(1), innerLoopLabel);
                    String t4 = createTemp(generator, subTree);
                    generator.emit(IntermediateCode.MULT, t2, "" + typeSymbol.getSize2d(), t4);
                    generator.emit(IntermediateCode.ADD, t4, t3, t4);
                    generator.emit(IntermediateCode.ARRAY_LOAD, t1,
                            variableTree.toString(), t4);

                }
                return t1;
            } else return variableTree.toString();

        } else if (subTree.isVariableDeclaration()) {
            int numVars = subTree.getChildCount() - 1;
            Symbol symbol = null;
            String type = subTree.getChild(0).toString();

            if (!type.equals("int") && !type.equals("fixedpt")) {
                try {
                    symbol = subTree.getCurrentScope().lookup(subTree.getChild(0).toString());
                } catch (SymbolNotFoundException e) {
                    raiseError("undefined symbol named '" + e.getSymbolName() + "'", subTree.getLine());
                    return "";
                }
            }

            if (((TigerTree) subTree.getChild(numVars)).isOptionalInit()) {
                String t1 = (subTree.getChild(numVars).getChild(0)).toString();
                numVars--;
                for (int i = 0; i < numVars; i++) {
                    if (symbol != null) {
                        if (symbol.getSymbolType() == Symbol.ARRAYTYPE) {
                            generator.emit(IntermediateCode.ARRAY_ASSIGN,
                                    subTree.getChild(i + 1).toString(), "" + symbol.getSize(), t1);
                        } else if (symbol.getSymbolType() == Symbol.ARRAY2DTYPE) {
                            String t2 = createTemp(generator, subTree);
                            generator.emit(IntermediateCode.MULT,
                                    "" + symbol.getSize(), "" + symbol.getSize2d(), t2);
                            generator.emit(IntermediateCode.ARRAY_LOAD,
                                    subTree.getChild(i + 1).toString(), t2, t1);
                        }
                    } else generator.emit(IntermediateCode.ASSIGN,
                            subTree.getChild(i + 1).toString(), t1, "");
                }
            } else if (symbol != null) {
                if (symbol.getSymbolType() == Symbol.ARRAYTYPE) {
                    for (int i = 0; i < numVars; i++) {
                        generator.emit(IntermediateCode.ARRAY_ASSIGN,
                                subTree.getChild(i + 1).toString(), "" + symbol.getSize(), "0");
                    }
                } else if (symbol.getSymbolType() == Symbol.ARRAY2DTYPE) {
                    for (int i = 0; i < numVars; i++) {
                        String t2 = createTemp(generator, subTree);
                        generator.emit(IntermediateCode.MULT,
                                "" + symbol.getSize(), "" + symbol.getSize2d(), t2);
                        generator.emit(IntermediateCode.ARRAY_LOAD,
                                subTree.getChild(i + 1).toString(), t2, "0");
                    }
                }
            }
            return "";
        } else if (subTree.isFunctionDeclaration()) {
            generator.emitLabel("func_" + subTree.getChild(1).toString()); // prefix with 'func_' to avoid label collisions
            generate((TigerTree) subTree.getChild(3), innerLoopLabel);
            return "";

        } else if (subTree.isReturnStatement()) {
            if (subTree.getChildCount() > 0) {
                String t1 = generate((TigerTree) subTree.getChild(0), innerLoopLabel);
                generator.emit(IntermediateCode.RETURN, t1, "", "");
            } else generator.emit(IntermediateCode.RETURN, "", "", "");
            return "";

        } else if (subTree.isBreak()) {
            if (innerLoopLabel != null) {
                generator.emit(IntermediateCode.GOTO, innerLoopLabel, "", "");
            }
        } else {
            for (int i = 0; i < subTree.getChildCount(); i++) {
                generate((TigerTree) subTree.getChild(i), innerLoopLabel);
            }
        }
        return "";
    }

    private String leftGenerate(TigerTree subTree) {

        TigerTree variableTree = (TigerTree) subTree.getChild(0);

        if (subTree.isArray()) {

            Symbol symbol;
            Scope typeScope;
            try {
                symbol = subTree.getCurrentScope().lookup(variableTree.toString());
                typeScope = subTree.getCurrentScope().lookupDefinedTypeScope(symbol.getDataType());
            } catch (SymbolNotFoundException e) {
                raiseError("undefined symbol named '" + e.getSymbolName() + "'", subTree.getLine());
                return "";
            }

            Symbol typeSymbol = typeScope.getSymbol(symbol.getDataType());

            String t1 = generate((TigerTree) variableTree.getChild(0), null);

            if (typeSymbol.getSymbolType() == Symbol.ARRAYTYPE) {
                return t1;
            } else {
                //array flattening
                String t2 = generate((TigerTree) variableTree.getChild(1), null);
                String t3 = createTemp(generator, subTree);
                generator.emit(IntermediateCode.MULT, t1, "" + typeSymbol.getSize2d(), t3);
                generator.emit(IntermediateCode.ADD, t3, t2, t3);
                return t3;
            }
        } else return variableTree.toString();
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
        // resolves two data types using resolveDataTypeNames, but
        // returns null if the resolved type does not equal the left
        // type.

        String dataType = resolveDataTypeNames(left, right);

        if (dataType != null && dataType.equals(left)) {
            return dataType;
        } else {
            return null;
        }
    }

    private String createTemp(IRGenerator generator, TigerTree subTree) {
        // creates a temporary variable and adds it to the symbol table.
        // uses the specified tree's scope and data type

        String ret;

        ret = generator.createTemp(subTree.getCurrentScope());
        symbolTable.addTemp(subTree.getCurrentScope(), ret, subTree.getDataType());

        return ret;
    }

    private void raiseError(String message, int lineNumber) {
        throw new RuntimeException("error on line " + lineNumber + ": " + message + ".");
    }
}