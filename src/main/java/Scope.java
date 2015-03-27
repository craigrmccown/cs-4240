import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scope {
    private String functionKey;
    private Scope parent;
    private HashMap<String, Symbol> symbols;
    private ArrayList<Scope> children;

    public Scope(String functionKey) {
        this.functionKey = functionKey;
        this.symbols = new HashMap<String, Symbol>();
        this.children = new ArrayList<Scope>();
    }

    public Scope(Scope parent, String functionKey) {
        this(functionKey);
        this.parent = parent;
    }

    public void addChildScope(Scope child) {
        this.children.add(child);
    }

    public void addSymbol(String key, Symbol value) {
        symbols.put(key, value);
    }

    public boolean isFunctionScope() {
        return functionKey != null;
    }

    public Symbol lookup(String key) {
        Symbol found = symbols.get(key);

        if (found == null) {
            if (isFunctionScope()) {
                VariableSymbol paramSymbol = ((FunctionSymbol) parent.lookup(functionKey)).getParam(key);

                if (paramSymbol == null) {
                    return parent.lookup(key);
                } else {
                    return paramSymbol;
                }
            } else {
                if (parent == null) {
                    return null;
                } else {
                    return parent.lookup(key);
                }
            }
        } else {
            return found;
        }
    }

    public List<Scope> getChildren() {
        return children;
    }

    public String toString() {
        String ret = "  SYMBOLS:\n";

        if (symbols.size() == 0) {
            ret += "\t- (no symbols)\n";
        }

        for (Symbol s : symbols.values()) {
            ret += "\t- " + s.toString() + "\n";
        }

        return ret;
    }
}