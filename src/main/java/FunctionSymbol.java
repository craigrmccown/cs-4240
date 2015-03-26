import java.util.ArrayList;
import java.util.List;

public class FunctionSymbol extends Symbol {
    private String returnType;
    private List<VariableSymbol> params;

    public FunctionSymbol(String name, String returnType, List<VariableSymbol> params) {
        super(name);
        this.returnType = returnType;
        this.params = new ArrayList<VariableSymbol>();

        for (int i = 0; i < params.size(); i ++) {
            this.params.add(params.get(i));
        }
    }
}
