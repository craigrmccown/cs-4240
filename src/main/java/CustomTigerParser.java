import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

public class CustomTigerParser extends TigerParser {

    private boolean errorsFound = false;


    public CustomTigerParser(TokenStream input) {
        super(input);
    }

    @Override
    public void reportError(RecognitionException e) {
        errorsFound = true;
        System.out.println("unsuccessful parse: parsing error on line " + e.line + " at " + e.token.getText());
    }

    public boolean successful() {
        return !errorsFound;
    }
}
