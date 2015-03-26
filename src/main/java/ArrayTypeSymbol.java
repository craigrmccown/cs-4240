public class ArrayTypeSymbol extends TypeSymbol {
    protected String arrayType;
    protected int size;

    public ArrayTypeSymbol(String name, String dataType, String arrayType, int size) {
        super(name, dataType);
        this.arrayType = arrayType;
        this.size = size;
    }

    public String toString() {
        return "array type: " + name + ", " + dataType + ", " + arrayType + ", " + size;
    }
}
