public class DuplicateSymbolException extends Exception {
    private Symbol symbol;

    public DuplicateSymbolException(Symbol symbol) {
        super("duplicate symbol detected");
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }
}