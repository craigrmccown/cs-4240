import org.antlr.runtime.tree.BaseTree;

import java.util.ArrayList;

public class SymbolTable {
    private Scope root;

    public SymbolTable(BaseTree parseTree) {
        root = new Scope();
        walk(parseTree, root);
    }

    private void walk(BaseTree parseTree, Scope currentScope) {
        System.out.println(parseTree);

        if (parseTree.getChildren() != null) {
            for (int i = 0; i < parseTree.getChildren().size(); i ++) {
                if (parseTree.getChild(i) instanceof BaseTree) {
                    Scope nextScope;

                    if (parseTree.getChild(i).toString().equals("BLOCK")) {
                        nextScope = new Scope(currentScope);
                        currentScope.addChildScope(nextScope);
                    } else {
                        nextScope = currentScope;
                        addSymbol(currentScope, (BaseTree) parseTree.getChild(i));
                    }

                    walk((BaseTree) parseTree.getChild(i), nextScope);
                } else {
                    System.out.println(parseTree.getChild(i));
                }
            }
        }
    }

    private void addSymbol(Scope scope, BaseTree tree) {
        if (tree.toString().equals("type")) {
            addType(scope, tree);
        } else if (tree.toString().equals("var")) {
            addVar(scope, tree);
        } else if (tree.toString().equals("function")) {
            addFunction(scope, tree);
        }
    }

    private void addType(Scope scope, BaseTree typeTree) {
        if (typeTree.getChild(0).toString().equals("array")) {
            BaseTree arrayTree = (BaseTree) typeTree.getChild(0);

            if (arrayTree.getChildren().size() == 2) {
                scope.addSymbol(
                        typeTree.getChild(1).toString(),
                        new ArraySymbol(
                                typeTree.getChild(1).toString(),
                                typeTree.getChild(0).toString(),
                                arrayTree.getChild(0).toString(),
                                Integer.parseInt(arrayTree.getChild(1).toString())
                        )
                );
            } else if (arrayTree.getChildren().size() == 3) {
                scope.addSymbol(
                        typeTree.getChild(1).toString(),
                        new Array2DSymbol(
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

        if (varTree.getChild(lastVar - 1).toString().equals(":=")) {
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

        if (paramsTree.toString().equals("PARAMS")) {
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
}
