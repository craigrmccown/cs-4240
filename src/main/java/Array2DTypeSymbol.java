public class Array2DTypeSymbol extends ArrayTypeSymbol {
    private int size2d;

    public Array2DTypeSymbol(String name, String dataType, String arrayType, int size, int size2d) {
        super(name, dataType, arrayType, size);
        this.size2d = size2d;
    }

    public String toString() {
        return "2d array type: " + name + ", " + dataType + ", " + arrayType + ", " + size + ", " + size2d;
    }
}