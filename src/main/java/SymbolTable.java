import org.antlr.runtime.tree.BaseTree;

import java.util.ArrayList;

public class SymbolTable {
    private Scope rootScope;

    public SymbolTable() {
        rootScope = new Scope(null);

        ArrayList<VariableSymbol> printiParams = new ArrayList<VariableSymbol>();
        ArrayList<VariableSymbol> printfParams = new ArrayList<VariableSymbol>();
        ArrayList<VariableSymbol> notParams = new ArrayList<VariableSymbol>();
        ArrayList<VariableSymbol> exitParams = new ArrayList<VariableSymbol>();

        printiParams.add(new VariableSymbol("i", "int"));
        printfParams.add(new VariableSymbol("f", "fixedpt"));
        notParams.add(new VariableSymbol("i", "int"));
        exitParams.add(new VariableSymbol("i", "int"));

        rootScope.addSymbol("printi", new FunctionSymbol("printi", "void", printiParams));
        rootScope.addSymbol("printf", new FunctionSymbol("printf", "void", printfParams));
        rootScope.addSymbol("readi", new FunctionSymbol("readi", "int", new ArrayList<VariableSymbol>()));
        rootScope.addSymbol("readf", new FunctionSymbol("readf", "fixedpt", new ArrayList<VariableSymbol>()));
        rootScope.addSymbol("flush", new FunctionSymbol("flush", "void", new ArrayList<VariableSymbol>()));
        rootScope.addSymbol("not", new FunctionSymbol("not", "int", notParams));
        rootScope.addSymbol("exit", new FunctionSymbol("exit", "void", exitParams));

    }

    public Scope getRootScope() {
        return rootScope;
    }

    public Scope addChildScope(Scope parent, String functionKey) {
        Scope child = new Scope(parent, functionKey);
        parent.addChildScope(child);
        return child;
    }

    public boolean addSymbol(Scope scope, BaseTree tree) {
        if (tree.getType() == TigerLexer.TYPE) {
            addType(scope, tree);
            return true;
        } else if (tree.getType() == TigerLexer.VAR) {
            addVar(scope, tree);
            return true;
        } else if (tree.getType() == TigerLexer.FUNCTION) {
            addFunction(scope, tree);
            return true;
        } else {
            return false;
        }
    }

    private void addType(Scope scope, BaseTree typeTree) {
        if (typeTree.getChild(0).getType() == TigerLexer.ARRAY) {
            BaseTree arrayTree = (BaseTree) typeTree.getChild(0);

            if (arrayTree.getChildren().size() == 2) {
                scope.addSymbol(
                        typeTree.getChild(1).toString(),
                        new ArrayTypeSymbol(
                                typeTree.getChild(1).toString(),
                                typeTree.getChild(0).toString(),
                                arrayTree.getChild(0).toString(),
                                Integer.parseInt(arrayTree.getChild(1).toString())
                        )
                );
            } else if (arrayTree.getChildren().size() == 3) {
                scope.addSymbol(
                        typeTree.getChild(1).toString(),
                        new Array2DTypeSymbol(
                                typeTree.getChild(1).toString(),
                                typeTree.getChild(0).toString(),
                                arrayTree.getChild(0).toString(),
                                Integer.parseInt(arrayTree.getChild(1).toString()),
                                Integer.parseInt(arrayTree.getChild(2).toString())
                        )
                );
            }
        } else {
            scope.addSymbol(
                    typeTree.getChild(1).toString(),
                    new TypeSymbol(typeTree.getChild(1).toString(), typeTree.getChild(0).toString())
            );
        }
    }

    private void addVar(Scope scope, BaseTree varTree) {
        int lastVar = varTree.getChildren().size();

        if (varTree.getChild(lastVar - 1).getType() == TigerLexer.ASSIGNMENT_OP) {
            lastVar --;
        }

        for (int i = 1; i < lastVar; i ++) {
            scope.addSymbol(
                    varTree.getChild(i).toString(),
                    new VariableSymbol(varTree.getChild(i).toString(), varTree.getChild(0).toString())
            );
        }
    }

    private void addFunction(Scope scope, BaseTree functionTree) {
        ArrayList<VariableSymbol> params = new ArrayList<VariableSymbol>();
        BaseTree paramsTree = (BaseTree) functionTree.getChild(2);
        BaseTree paramTree;

        if (paramsTree.getType() == TigerLexer.PARAMS) {
            for (int i = 0; i < paramsTree.getChildren().size(); i ++) {
                paramTree = (BaseTree) paramsTree.getChild(i);
                params.add(new VariableSymbol(paramTree.getChild(1).toString(), paramTree.getChild(0).toString()));
            }
        }

        scope.addSymbol(
                functionTree.getChild(1).toString(),
                new FunctionSymbol(functionTree.getChild(1).toString(), functionTree.getChild(0).toString(), params)
        );
    }

    public String toString() {
        return toString(rootScope, 0);
    }

    private String toString(Scope scope, int indentation) {
        String str = "";
        String childString = "  CHILDREN:\n";

        if (scope.getChildren().size() == 0) {
            childString += "\t- (no children)\n";
        }

        for (int i = 0; i < scope.getChildren().size(); i ++) {
            str += toString(scope.getChildren().get(i), indentation + 1);
        }

        return Util.indentBlock("SCOPE {\n" + scope.toString(), indentation) + "\n" +
                Util.indentBlock(childString, indentation) + str +
                Util.indentBlock("}\n", indentation);
    }
}
