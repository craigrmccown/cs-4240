import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;

public class TigerToken extends CommonToken {
    public double value;

    public TigerToken(Token token) {
        super(token);
        setValue();
    }

    public TigerToken(int tokenType, String text) {
        super(tokenType, text);
        setValue();
    }

    private void setValue() {
        switch (getType()) {
            case TigerLexer.INTLIT:
                this.value = Integer.parseInt(getText());
                break;
            case TigerLexer.FIXEDPTLIT:
                this.value = Double.parseDouble(getText());
                break;
        }
    }
}
