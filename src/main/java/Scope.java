import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public List<Scope> getChildren() {
        return children;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String prefix) {
        String ret = prefix + "SCOPE\n";

        if (symbols.size() == 0) {
            ret += prefix + "(no symbols)\n";
        }
        for (Symbol s : symbols.values()) {
            ret += prefix + s.toString() + "\n";
        }

        return ret;
    }
}