import org.antlr.runtime.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            CharStream cs = new ANTLRFileStream("target/classes/large-program.tiger");
            TigerLexer lexer = new TigerLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CustomTigerParser parser = new CustomTigerParser(tokens);
            parser.tiger_program();
            if (parser.successful()) System.out.println("successful parse");
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