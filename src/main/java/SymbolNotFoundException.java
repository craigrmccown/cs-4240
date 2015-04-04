public class SymbolNotFoundException extends Exception {
    private String symbolName;

    public SymbolNotFoundException(String symbolName) {
        super("undefined symbol");
        this.symbolName = symbolName;
    }

    public String getSymbolName() {
        return symbolName;
    }
}
