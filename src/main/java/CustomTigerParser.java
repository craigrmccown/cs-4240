import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

/**
 * Created by andrewmehlberg on 2/12/15.
 */
public class CustomTigerParser extends TigerParser {

    private boolean errorsFound = false;


    public CustomTigerParser(TokenStream input) {
        super(input);
    }

    @Override
    public void reportError(RecognitionException e) {
        errorsFound = true;
        System.out.println("unsuccesful parse: parsing error on line " + e.line + " at " + e.token.getText());
    }

    public boolean successful() {
        return !errorsFound;
    }
}
