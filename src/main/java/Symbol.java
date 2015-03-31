import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Symbol {
    public static final int TYPETYPE = 0;
    public static final int VARTYPE = 1;
    public static final int ARRAYTYPE = 2;
    public static final int ARRAY2DTYPE = 3;
    public static final int FUNCTIONTYPE = 4;

    private int symbolType;
    private String name;
    private int typeCode;
    private String dataType;
    private int size;
    private int size2d;
    private Map<String, Symbol> parameters;

    public Symbol(String name, int typeCode) {
        this.name = name;
        this.typeCode = typeCode;
        this.symbolType = TYPETYPE;
    }

    public Symbol(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
        this.symbolType = VARTYPE;
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
            this.parameters = new HashMap<String, Symbol>();

            for (Symbol symbol : parameters) {
                this.parameters.put(symbol.name, symbol);
            }
        }

        this.symbolType = FUNCTIONTYPE;
    }

    public String getName() { return name; }

    public String getDataType() { return dataType; }

    public int getSize() { return size; }

    public int getSize2d() { return size2d; }

    public Symbol getParameter(String key) { return parameters.get(key); }

    public int getSymbolType() {
        return this.symbolType;
    };

    public String toString() {
        return name;
    }
}
