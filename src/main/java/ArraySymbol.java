public class ArraySymbol extends TypeSymbol {
    private String arrayType;
    private int size;

    public ArraySymbol(String name, String dataType, String arrayType, int size) {
        super(name, dataType);
        this.arrayType = arrayType;
        this.size = size;
    }
}
