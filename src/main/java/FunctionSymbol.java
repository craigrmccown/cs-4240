import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionSymbol extends Symbol {
    private String returnType;
    private Map<String, VariableSymbol> params;

    public FunctionSymbol(String name, String returnType, List<VariableSymbol> params) {
        super(name);
        this.returnType = returnType;
        this.params = new HashMap<String, VariableSymbol>();

        for (int i = 0; i < params.size(); i ++) {
            this.params.put(params.get(i).name, params.get(i));
        }
    }

    public VariableSymbol getParam(String key) {
        return params.get(key);
    }

    public String toString() {
        return "function: { identifier: " + name +
                ", return type: " + returnType +
                ", parameters " + params + " }";
    }
}
