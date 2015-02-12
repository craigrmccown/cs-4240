import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            CharStream cs = new ANTLRFileStream("target/classes/sample-program.tiger");
            TigerLexer lexer = new TigerLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            TigerParser parser = new TigerParser(tokens);
            parser.tiger_program();
            System.out.println("successful parse");
        } catch (IOException e) {
            System.out.println("failed to read input file");
            System.exit(1);
        } catch (RuntimeException e) {
            System.out.println("unsuccessful parse: " + e.getMessage());
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}