import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scope {
    public final static int VOID = 0;
    public final static int INTTYPE = 1;
    public final static int FIXEDPTTYPE = 2;

    private String functionKey;
    private Scope parent;
    private Map<String, Symbol> symbols;
    private List<Scope> children;

    public Scope(String functionKey) {
        this.functionKey = functionKey;
        this.symbols = new HashMap<String, Symbol>();
        this.children = new ArrayList<Scope>();
    }

    public Scope(Scope parent, String functionKey) {
        this(functionKey);
        this.parent = parent;
    }

    public void addChildScope(Scope child) { this.children.add(child); }

    public void addSymbol(String key, Symbol value) {
        Symbol old = symbols.get(key);

        if (old == null) {
            symbols.put(key, value);
        } else {
            throw new RuntimeException("symbol defined twice: " + key);
        }
    }

    public Symbol lookup(String key) {
        Symbol found = symbols.get(key);

        if (found != null) {
            return found;
        } else {
            if (functionKey != null) {
                Symbol paramSymbol = parent.lookup(functionKey).getParameter(key);

                if (paramSymbol != null) {
                    return paramSymbol;
                }
            }

            if (parent == null) {
                throw new RuntimeException("undefined symbol");
            } else {
                return parent.lookup(key);
            }
        }
    }

    public Symbol lookupDataType(Symbol symbol) {
        if (symbol.getDataType().equals("int") || symbol.getDataType().equals("fixedpt")) {
            return symbol;
        } else {
            return lookup(symbol.getDataType());
        }
    }

    public Symbol getFunctionDeclaration() {
        if (functionKey != null) {
            return lookup(functionKey);
        } else {
            return parent.getFunctionDeclaration();
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