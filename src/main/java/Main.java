import org.antlr.runtime.*;
import org.antlr.runtime.tree.BaseTree;

import java.io.IOException;
import java.util.List;

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
            parser.setTreeAdaptor(new TigerTreeAdaptor());
            TigerTree tree = (TigerTree) parser.tiger_program().getTree();

            if (!parser.successful()) throw new RuntimeException("parse error");

            SemanticChecker semanticChecker = new SemanticChecker();
            semanticChecker.check(tree);

            List<IntermediateCode> ir = semanticChecker.getGenerator().getIR();
            List<IntermediateCode> naiveIR = RegisterAllocation.naive(ir);
            System.out.println(MIPSGenerator.generate(naiveIR));
            
            RegisterAllocation reg = new RegisterAllocation();
            List<IntermediateCode> cfgIR = reg.cfgConstruction(semanticChecker.getGenerator());
            System.out.println(MIPSGenerator.generate(cfgIR));
        } catch (IOException e) {
            System.out.println("failed to read input file");
            System.exit(1);
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}