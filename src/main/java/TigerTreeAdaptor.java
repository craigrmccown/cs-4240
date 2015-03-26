import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;

public class TigerTreeAdaptor extends CommonTreeAdaptor {
    @Override
    public Object create(Token token) {
        if (token == null) {
            return new TigerTree(token);
        } else {
            return new TigerTree(new TigerToken(token, "asdf"));
        }
    }
}