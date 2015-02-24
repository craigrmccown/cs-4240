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
            CommonTree x = (CommonTree) parser.tiger_program().getTree();
            if (parser.successful()) printTree(x, "");
        } catch (IOException e) {
            System.out.println("failed to read input file");
            System.exit(1);
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }

    public static void printTree(BaseTree tree, String indent) {
        System.out.println(indent + tree.toString());

        if (tree.getChildren() != null) {
            for (int i = 0; i < tree.getChildren().size(); i ++) {
                if (tree.getChild(i) instanceof BaseTree) {
                    printTree((BaseTree) tree.getChild(i), indent + '\t');
                } else {
                    System.out.println(tree.getChild(i).toString());
                }
            }
        }
    }
}