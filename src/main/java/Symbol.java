import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Symbol {
    public static final int VARTYPE = 1;
    public static final int ARRAYTYPE = 2;
    public static final int ARRAY2DTYPE = 3;
    public static final int FUNCTIONTYPE = 4;

    private int symbolType;
    private String name;
    private String dataType;
    private int size;
    private int size2d;
    private Map<String, Symbol> parameters;
    private ArrayList<Symbol> orderedParameters;

    public Symbol(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
        this.symbolType = VARTYPE;
        this.parameters = new HashMap<String, Symbol>();
        this.orderedParameters = new ArrayList<Symbol>();
    }

    public Symbol(String name, String dataType, int size) {
        this(name, dataType);
        this.size = size;
        this.symbolType = ARRAYTYPE;
    }

    public Symbol(String name, String dataType, int size, int size2d) {
        this(name, dataType, size);
        this.size2d = size2d;
        this.symbolType = ARRAY2DTYPE;
    }

    public Symbol(String name, String dataType, List<Symbol> parameters) {
        this(name, dataType);

        if (parameters != null) {
            for (Symbol symbol : parameters) {
                this.parameters.put(symbol.name, symbol);
                this.orderedParameters.add(symbol);
            }
        }

        this.symbolType = FUNCTIONTYPE;
    }

    public void setDataType(String dataType) { this.dataType = dataType; }

    public String getName() { return name; }

    public String getDataType() { return dataType; }

    public int getSize() { return size; }

    public int getSize2d() { return size2d; }

    public Symbol getParameter(String key) { return parameters.get(key); }

    public Symbol getParameter(int i) { return orderedParameters.get(i); }

    public int getNumParameters() { return parameters.size(); }

    public int getSymbolType() {
        return this.symbolType;
    };

    public String toString() {
        String ret = "";

        switch (symbolType) {
            case VARTYPE:
                ret = "{ name: " + name + ", data type: " + dataType + " }";
                break;
            case ARRAYTYPE:
                ret = "{ name: " + name + ", data type: one dimensional array, base type: " + dataType + ", size: " + size + " }";
                break;
            case ARRAY2DTYPE:
                ret = "{ name: " + name + ", data type: two dimensional array, base type: " + dataType + ", rows: " + size + ", columns: " + size2d + " }";
                break;
            case FUNCTIONTYPE:
                ret = "{ name: " + name + ", return type: " + dataType + ", parameters: " + orderedParameters + " }";
                break;
        }

        return ret;
    }
}
