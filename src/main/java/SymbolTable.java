import org.antlr.runtime.tree.BaseTree;

import java.util.ArrayList;

public class SymbolTable {
    private Scope rootScope;

    public SymbolTable() {
        rootScope = new Scope(null);

        ArrayList<Symbol> printiParams = new ArrayList<Symbol>();
        ArrayList<Symbol> printfParams = new ArrayList<Symbol>();
        ArrayList<Symbol> notParams = new ArrayList<Symbol>();
        ArrayList<Symbol> exitParams = new ArrayList<Symbol>();

        printiParams.add(new Symbol("i", "int"));
        printfParams.add(new Symbol("f", "fixedpt"));
        notParams.add(new Symbol("i", "int"));
        exitParams.add(new Symbol("i", "int"));

        rootScope.addSymbolUnsafe("printi", new Symbol("printi", "VOID", printiParams));
        rootScope.addSymbolUnsafe("printf", new Symbol("printf", "VOID", printfParams));
        rootScope.addSymbolUnsafe("readi", new Symbol("readi", "int", new ArrayList<Symbol>()));
        rootScope.addSymbolUnsafe("readf", new Symbol("readf", "fixedpt", new ArrayList<Symbol>()));
        rootScope.addSymbolUnsafe("flush", new Symbol("flush", "VOID", new ArrayList<Symbol>()));
        rootScope.addSymbolUnsafe("not", new Symbol("not", "int", notParams));
        rootScope.addSymbolUnsafe("exit", new Symbol("exit", "VOID", exitParams));
    }

    public Scope getRootScope() {
        return rootScope;
    }

    public Scope addChildScope(Scope parent, String functionKey) {
        Scope child = new Scope(parent, functionKey);
        parent.addChildScope(child);
        return child;
    }

    public void handleSymbolDeclaration(Scope scope, TigerTree tree) throws DuplicateSymbolException, SymbolNotFoundException {
        if (tree.getType() == TigerLexer.TYPE) {
            addType(scope, tree);
        } else if (tree.getType() == TigerLexer.VAR) {
            addVar(scope, tree);
        } else if (tree.getType() == TigerLexer.FUNCTION) {
            addFunction(scope, tree);
        }
    }

    private void addType(Scope scope, TigerTree typeTree) throws DuplicateSymbolException {
        if (typeTree.getChild(0).getType() == TigerLexer.ARRAY) {
            BaseTree arrayTree = (BaseTree) typeTree.getChild(0);

            if (arrayTree.getChildren().size() == 2) {
                scope.addSymbol(
                        typeTree.getChild(1).toString(),
                        new Symbol(
                                typeTree.getChild(1).toString(),
                                arrayTree.getChild(0).toString(),
                                Integer.parseInt(arrayTree.getChild(1).toString())
                        )
                );
            } else if (arrayTree.getChildren().size() == 3) {
                scope.addSymbol(
                        typeTree.getChild(1).toString(),
                        new Symbol(
                                typeTree.getChild(1).toString(),
                                arrayTree.getChild(0).toString(),
                                Integer.parseInt(arrayTree.getChild(1).toString()),
                                Integer.parseInt(arrayTree.getChild(2).toString())
                        )
                );
            }
        } else {
            scope.addSymbol(
                    typeTree.getChild(1).toString(),
                    new Symbol(typeTree.getChild(1).toString(), typeTree.getChild(0).toString())
            );
        }
    }

    private void addVar(Scope scope, TigerTree varTree) throws DuplicateSymbolException, SymbolNotFoundException {
        int lastVar;

        scope.lookupDefinedTypeScope(varTree.getChild(0).toString());

        lastVar = varTree.getChildren().size();

        if ((varTree.getChild(lastVar - 1)).getType() == TigerLexer.OPTIONAL_INIT) {
            lastVar --;
        }

        for (int i = 1; i < lastVar; i ++) {
            scope.addSymbol(
                varTree.getChild(i).toString(),
                new Symbol(varTree.getChild(i).toString(), varTree.getChild(0).toString())
            );
        }
    }

    private void addFunction(Scope scope, TigerTree functionTree) throws DuplicateSymbolException {
        ArrayList<Symbol> params = new ArrayList<Symbol>();
        BaseTree paramsTree = (BaseTree) functionTree.getChild(2);
        BaseTree paramTree;

        if (paramsTree.getType() == TigerLexer.PARAMS) {
            for (int i = 0; i < paramsTree.getChildren().size(); i ++) {
                paramTree = (BaseTree) paramsTree.getChild(i);
                params.add(new Symbol(paramTree.getChild(1).toString(), paramTree.getChild(0).toString()));
            }
        }

        scope.addSymbol(
            functionTree.getChild(1).toString(),
            new Symbol(functionTree.getChild(1).toString(), functionTree.getChild(0).toString(), params)
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
