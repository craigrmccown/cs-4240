import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;

public class TigerToken extends CommonToken {
    public String asdf;

    public TigerToken(Token token, String asdf) {
        super(token);
        this.asdf = asdf;
    }
}
