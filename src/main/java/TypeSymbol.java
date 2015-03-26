public class TypeSymbol extends Symbol {
    protected String dataType;

    public TypeSymbol(String name, String dataType) {
        super(name);
        this.dataType = dataType;
    }

    public String toString() {
        return "type: " + name + ", " + dataType;
    }
}
