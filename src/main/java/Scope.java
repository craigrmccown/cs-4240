import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scope {
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

    public boolean addSymbol(String key, Symbol value) {
        Symbol old = symbols.get(key);

        if (old == null) {
            symbols.put(key, value);
            return true;
        } else {
            return false;
        }
    }

    public Symbol getSymbol(String key) {
        Symbol symbol =  symbols.get(key);

        if (symbol == null && functionKey != null) {
            Symbol paramSymbol = parent.getSymbol(functionKey).getParameter(key);

            if (paramSymbol != null) {
                symbol = paramSymbol;
            }
        }

        return symbol;
    }

    public Symbol lookup(String key) {
        Scope x = lookupScope(key);

        if (x == null) {
            return null;
        } else {
            return x.getSymbol(key);
        }
    }

    public Scope lookupScope(String key) {
        Symbol found = getSymbol(key);

        if (found != null) {
            return this;
        } else {
            if (parent == null) {
                return null;
            } else {
                return parent.lookupScope(key);
            }
        }
    }

    public Scope getDataTypeScope(Symbol symbol) {
        if (symbol.getDataType().equals("int") || symbol.getDataType().equals("fixedpt") || symbol.getDataType().equals("VOID")) {
            return null;
        } else {
            return lookupScope(symbol.getDataType());
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