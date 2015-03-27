import org.antlr.runtime.tree.BaseTree;
import java.util.Arrays;

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
                TigerTree childTree = (TigerTree) subTree.getChild(i);

                if (childTree.isBlock()) {
                    String functionKey = null;

                    if (childTree.isFunctionBody()) {
                        functionKey = childTree.getFunctionKey();
                    }

                    nextScope = symbolTable.addChildScope(currentScope, functionKey);
                } else if (childTree.isSymbolDeclaration()) {
                    symbolTable.addSymbol(currentScope, childTree);
                } else if (childTree.isVariableReference()) {
                    Symbol symbol = currentScope.lookup(childTree.toString());

                    if (symbol == null) {
                        throw new RuntimeException("undefined symbol: '" + childTree.toString() + "'. line " + childTree.getLine());
                    }
                }

                check(childTree, nextScope);
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
