public class Array2DSymbol extends ArraySymbol {
    private int size2d;

    public Array2DSymbol(String name, String dataType, String arrayType, int size, int size2d) {
        super(name, dataType, arrayType, size);
        this.size2d = size2d;
    }
}
