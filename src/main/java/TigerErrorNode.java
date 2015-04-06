import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;

public class TigerErrorNode extends TigerTree {
    public TigerErrorNode(Token token) {
        super(token);
    }

    public TigerErrorNode(TokenStream input, Token start, Token stop, RecognitionException e) {
        super(start);

    }
}
