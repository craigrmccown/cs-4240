import java.util.ArrayList;
import java.util.HashMap;

public class Scope {
    private Scope parent;
    private HashMap<String, Symbol> symbols;
    private ArrayList<Scope> children;

    public Scope() {
        this.symbols = new HashMap<String, Symbol>();
        this.children = new ArrayList<Scope>();
    }

    public Scope(Scope parent) {
        this();
        this.parent = parent;
    }

    public void addChildScope(Scope child) {
        this.children.add(child);
    }

    public void addSymbol(String key, Symbol value) {
        symbols.put(key, value);
    }
}