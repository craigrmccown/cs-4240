import org.antlr.runtime.tree.BaseTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                Scope nextScope = currentScope;

                if (subTree.getChild(i).getType() == TigerLexer.BLOCK) {
                    TigerTree blockTree = (TigerTree) subTree.getChild(i);
                    String functionKey = null;

                    if (blockTree.isFunctionBody()) {
                        functionKey = blockTree.getFunctionKey();
                    }

                    nextScope = symbolTable.addChildScope(currentScope, functionKey);
                } else if (symbolTable.addSymbol(currentScope, (BaseTree) subTree.getChild(i))) {
                    // added symbol to table
                } else if (subTree.getChild(i).getType() == TigerLexer.ID && subTree.getType() != TigerLexer.PARAM) {
                    Symbol symbol = currentScope.lookup(subTree.getChild(i).toString());

                    if (symbol == null) {
                        throw new RuntimeException("symbol " + subTree.getChild(i).toString() + " not recognized on line " + subTree.getChild(i).getLine());
                    }
                }

                check((BaseTree) subTree.getChild(i), nextScope);
            }
        }
    }

    private boolean checkTree(BaseTree tree, Scope scope) {
        int[] binaryOperators = {
                TigerLexer.PLUS,
                TigerLexer.MINUS,
                TigerLexer.MULTIPLY,
                TigerLexer.DIVIDE,
                TigerLexer.EQUALS,
                TigerLexer.NOT_EQUAL,
                TigerLexer.LESS_THAN,
                TigerLexer.LESS_THAN_EQUAL,
                TigerLexer.GREATER_THAN,
                TigerLexer.GREATER_THAN_EQUAL,
                TigerLexer.BIT_AND,
                TigerLexer.BIT_OR,
                TigerLexer.ASSIGNMENT_OP
        };

        if (Arrays.asList(binaryOperators).contains(tree.getType())) {
            return checkBinaryOperator(tree);
        } else if (tree.getType() == TigerLexer.RETURN) {
            return checkReturnType(tree, scope);
        } else if (tree.toString().equals("FUNCTION_CALL")) {
            return checkFunctionCall(tree);
        } else {
            return true;
        }
    }

    private boolean checkBinaryOperator(BaseTree tree) {
        return true;
    }

    private boolean checkReturnType(BaseTree tree, Scope scope) {
        return true;
    }

    private boolean checkFunctionCall(BaseTree tree) {
        return true;
    }
}
