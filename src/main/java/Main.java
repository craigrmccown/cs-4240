import org.antlr.runtime.*;
import org.antlr.runtime.tree.BaseTree;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            CharStream cs = new ANTLRFileStream("target/classes/" + args[0]);
            TigerLexer lexer = new TigerLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CustomTigerParser parser = new CustomTigerParser(tokens);
            parser.setTreeAdaptor(new TigerTreeAdaptor());
            TigerTree tree = (TigerTree) parser.tiger_program().getTree();

            if (!parser.successful()) throw new RuntimeException("parse error");

            SemanticChecker semanticChecker = new SemanticChecker();
            semanticChecker.check(tree);

            List<IntermediateCode> ir = semanticChecker.getGenerator().getIR();

            if (args[1].equals("naive")) {
                List<IntermediateCode> naiveIR = RegisterAllocation.naive(ir);
                System.out.println(MIPSGenerator.generate(naiveIR));
            } else if (args[1].equals("cfg")) {

            } else if (args[1].equals("ebb")) {

            } else {
                System.out.println("Please pass one of these three options as the second argument: 'naive', 'cfg', 'ebb'");
            }

        } catch (IOException e) {
            System.out.println("failed to read input file");
            System.exit(1);
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}