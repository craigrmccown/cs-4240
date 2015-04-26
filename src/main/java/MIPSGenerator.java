import java.util.HashSet;
import java.util.List;

public class MIPSGenerator {
    static HashSet<String> memLocations = new HashSet<String>();

    // TODO handle floating point
    // TODO array functionality
    static String generate(List<IntermediateCode> ir) {
        for (int i = 0; i < ir.size(); i ++) {
            IntermediateCode instruction = ir.get(i);
            String[] params = instruction.getParams();

            switch (instruction.getOpcode()) {
                case IntermediateCode.ASSIGN:
                    System.out.println("add " + params[0] + " " + params[1] + " 0");
                    break;
                case IntermediateCode.ADD:
                    System.out.println("add " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.SUB:
                    System.out.println("sub " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.MULT:
                    System.out.println("mul " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.DIV:
                    System.out.println("div " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.AND:
                    System.out.println("and " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.OR:
                    System.out.println("or " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.GOTO:
                    System.out.println("b " + params[0]);
                    break;
                case IntermediateCode.BREQ:
                    System.out.println("beq " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.BRNEQ:
                    System.out.println("bne " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.BRLT:
                    System.out.println("blt " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.BRGT:
                    System.out.println("bgt " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.BRLEQ:
                    System.out.println("ble " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.BRGEQ:
                    System.out.println("bge " + params[0] + " " + params[1] + " " + params[2]);
                    break;
                case IntermediateCode.RETURN:
                    System.out.println("skipping return, not implementing function calls");
                    break;
                case IntermediateCode.CALL:
                    System.out.println("skipping call, not implementing function calls");
                    break;
                case IntermediateCode.CALLR:
                    System.out.println("skipping callr, not implementing function calls");
                    break;
                case IntermediateCode.ARRAY_STORE:
                    System.out.println("TODO: array store");
                    break;
                case IntermediateCode.ARRAY_LOAD:
                    System.out.println("TODO: array load");
                    break;
                case IntermediateCode.ARRAY_ASSIGN:
                    System.out.println("TODO: array assign");
                    break;
                case IntermediateCode.LDR:
                    System.out.println("lw " + params[0] + " " + params[1]);
                    break;
                case IntermediateCode.STR:
                    if (!memLocations.contains(params[1])) {
                        memLocations.add(params[1]);
                    }

                    System.out.println("sw " + params[0] + " " + params[1]);
                    break;
                default:
                    LabelCode lc = (LabelCode)instruction;
                    System.out.println(lc.toString() + ":");
                    break;
            }
        }

        return "";
    }
}
