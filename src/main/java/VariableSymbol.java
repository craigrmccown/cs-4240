public class VariableSymbol extends Symbol {
    private String dataType;

    public VariableSymbol(String name, String dataType) {
        super(name);
        this.dataType = dataType;
    }
}