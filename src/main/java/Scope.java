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

    public void addSymbol(String key, Symbol value) throws DuplicateSymbolException {
        if (!addSymbolUnsafe(key, value)) {
            throw new DuplicateSymbolException(value);
        }
    }

    public boolean addSymbolUnsafe(String key, Symbol value) {
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

    public Symbol lookup(String key) throws SymbolNotFoundException {

        Scope x = lookupScope(key);

        if (x == null) {
            return null;
        } else {
            return x.getSymbol(key);
        }
    }

    public Scope lookupScope(String key) throws SymbolNotFoundException {
        Scope scope = lookupScopeUnsafe(key);

        if (scope != null) {
            return scope;
        } else {
            throw new SymbolNotFoundException(key);
        }
    }

    public Scope lookupDefinedTypeScope(String dataType) throws SymbolNotFoundException {
        if (dataType.equals("int") || dataType.equals("fixedpt") || dataType.equals("VOID")) {
            return null;
        } else {
            return lookupScope(dataType);
        }
    }

    public Scope lookupScopeUnsafe(String key) {
        Symbol found = getSymbol(key);

        if (found != null) {
            return this;
        } else {
            if (parent == null) {
                return null;
            } else {
                return parent.lookupScopeUnsafe(key);
            }
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