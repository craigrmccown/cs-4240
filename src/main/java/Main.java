import org.antlr.runtime.*;
import org.antlr.runtime.tree.BaseTree;
import org.antlr.runtime.tree.CommonTree;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("No file selected or too many arguments");
            System.exit(0);
        }
        try {
            CharStream cs = new ANTLRFileStream("target/classes/" + args[0]);
            TigerLexer lexer = new TigerLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CustomTigerParser parser = new CustomTigerParser(tokens);
            BaseTree tree = (BaseTree) parser.tiger_program().getTree();

            if (!parser.successful()) throw new RuntimeException();

            SemanticChecker semanticChecker = new SemanticChecker();
            semanticChecker.check(tree);

            System.out.println(semanticChecker.getSymbolTable());
        } catch (IOException e) {
            System.out.println("failed to read input file");
            System.exit(1);
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}