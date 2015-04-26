import java.util.HashSet;
import java.util.List;

public class MIPSGenerator {
    static HashSet<String> memLocations = new HashSet<String>();

    // TODO handle floating point
    // TODO array functionality
    // TODO add boilerplate MIPS code before and after
    static String generate(List<IntermediateCode> ir) {
        String dataBlock, textBlock;
        dataBlock = textBlock = "";

        for (int i = 0; i < ir.size(); i ++) {
            IntermediateCode instruction = ir.get(i);
            String[] params = instruction.getParams();

            switch (instruction.getOpcode()) {
                case IntermediateCode.ASSIGN:
                    textBlock += "add " + params[0] + " " + params[1] + " 0\n";
                    break;
                case IntermediateCode.ADD:
                    textBlock += "add " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.SUB:
                    textBlock += "sub " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.MULT:
                    textBlock += "mul " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.DIV:
                    textBlock += "div " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.AND:
                    textBlock += "and " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.OR:
                    textBlock += "or " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.GOTO:
                    textBlock += "b " + params[0] + "\n";
                    break;
                case IntermediateCode.BREQ:
                    textBlock += "beq " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.BRNEQ:
                    textBlock += "bne " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.BRLT:
                    textBlock += "blt " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.BRGT:
                    textBlock += "bgt " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.BRLEQ:
                    textBlock += "ble " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.BRGEQ:
                    textBlock += "bge " + params[0] + " " + params[1] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.RETURN:
                    // skipping return, not implementing function calls"
                    break;
                case IntermediateCode.CALL:
                    // skipping call, not implementing function calls"
                    break;
                case IntermediateCode.CALLR:
                    // skipping callr, not implementing function calls"
                    break;
                case IntermediateCode.ARRAY_STORE:
                    String[] splitParam = params[0].split("\\|");
                    textBlock += "la " + splitParam[0] + " " + splitParam[1] + "\n";
                    textBlock += "add " + splitParam[0] + " " + splitParam[0] + " " + params[1] + "\n";
                    textBlock += "sw " + splitParam[0] + " " + params[2] + "\n";
                    break;
                case IntermediateCode.ARRAY_LOAD:
                    textBlock += "la " + params[0] + " " + params[1] + "\n";
                    textBlock += "add " + params[0] + " " + params[0] + " " + params[2] + "\n";
                    textBlock += "lw " + params[0] + " " + params[0] + "\n";
                    break;
                case IntermediateCode.ARRAY_ASSIGN:
                    int size = Integer.parseInt(params[1]);
                    dataBlock += params[0] + ": .space " + (size * 4) + "\n";
                    memLocations.add(params[0]);
                    break;
                case IntermediateCode.LDR:
                    textBlock += "lw " + params[0] + " " + params[1] + "\n";
                    break;
                case IntermediateCode.STR:
                    if (!memLocations.contains(params[1])) {
                        memLocations.add(params[1]);
                    }

                    textBlock += "sw " + params[0] + " " + params[1] + "\n";
                    break;
                default:
                    LabelCode lc = (LabelCode)instruction;
                    textBlock += lc.toString() + ":" + "\n";
                    break;
            }
        }

        for (String word : memLocations) {
            dataBlock += word + ": .word \n";
        }

        return ".text\n" + textBlock + "\n.data\n" + dataBlock;
    }
}
