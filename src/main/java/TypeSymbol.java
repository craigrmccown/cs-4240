public class TypeSymbol extends Symbol {
    private String dataType;

    public TypeSymbol(String name, String dataType) {
        super(name);
        this.dataType = dataType;
    }
}
