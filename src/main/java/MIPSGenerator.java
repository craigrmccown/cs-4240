import java.util.HashMap;
import java.util.List;

public class MIPSGenerator {
    static HashMap<String, String> data = new HashMap<String, String>();

    static String generate(List<IntermediateCode> ir) {
        String dataBlock, textBlock;
        dataBlock = textBlock = "";

        for (int i = 0; i < ir.size(); i ++) {
            IntermediateCode instruction = ir.get(i);
            String[] params = instruction.getParams();

            switch (instruction.getOpcode()) {
                case IntermediateCode.ASSIGN:
                    // params[0] is destination register, params[1] is source register
                    textBlock += "add " + params[0] + ", " + params[1] + " 0\n";
                    break;
                case IntermediateCode.ADD:
                    // params[0] is destination register, params[1] is source register, params[2] is source register or immediate value
                    textBlock += "add " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.SUB:
                    // params[0] is destination register, params[1] is source register, params[2] is source register or immediate value
                    textBlock += "sub " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.MULT:
                    // params[0] is destination register, params[1] is source register, params[2] is source register or immediate value
                    textBlock += "mul " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.DIV:
                    // params[0] is destination register, params[1] is source register, params[2] is source register or immediate value
                    textBlock += "div " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.AND:
                    // params[0] is destination register, params[1] is source register, params[2] is source register or immediate value
                    textBlock += "and " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.OR:
                    // params[0] is destination register, params[1] is source register, params[2] is source register or immediate value
                    textBlock += "or " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.GOTO:
                    // params[0] is goto label
                    textBlock += "b " + params[0] + "\n";
                    break;
                case IntermediateCode.BREQ:
                    // params[0] is left operand register, params[1] is right operand (register or immediate value), params[2] branch label
                    textBlock += "beq " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.BRNEQ:
                    // params[0] is left operand register, params[1] is right operand (register or immediate value), params[2] branch label
                    textBlock += "bne " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.BRLT:
                    // params[0] is left operand register, params[1] is right operand (register or immediate value), params[2] branch label
                    textBlock += "blt " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.BRGT:
                    // params[0] is left operand register, params[1] is right operand (register or immediate value), params[2] branch label
                    textBlock += "bgt " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.BRLEQ:
                    // params[0] is left operand register, params[1] is right operand (register or immediate value), params[2] branch label
                    textBlock += "ble " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.BRGEQ:
                    // params[0] is left operand register, params[1] is right operand (register or immediate value), params[2] branch label
                    textBlock += "bge " + params[0] + ", " + params[1] + ", " + params[2] + "\n";
                    break;
                case IntermediateCode.RETURN:
                    // skipping return, not implementing function calls
                    break;
                case IntermediateCode.CALL:
                    // params[0] is argument (register or immediate value), params[1] is function name
                    if (params[1].equals("func_printi__0")) {
                        textBlock += "li $v0, 1\n";
                        textBlock += "add $a0, $zero, " + params[0] + "\n";
                        textBlock += "syscall\n";
                    } else if (params[1].equals("func_readi__0")) {
                        textBlock += "li $v0, 5\n";
                        textBlock += "syscall\n";
                    }
                    break;
                case IntermediateCode.CALLR:
                    // params[0] is destination register, params[1] is function name
                    if (params[1].equals("func_readi__0")) {
                        textBlock += "li $v0, 5\n";
                        textBlock += "syscall\n";
                        textBlock += "move " + params[0] + ", $v0\n";
                    }
                    break;
                case IntermediateCode.ARRAY_STORE:
                    // params[0] is register to use|array var, params[1] is index (register or immediate value), params[2] is the register holding the value to store
                    String[] splitArrStoreParam = params[0].split("\\|");
                    textBlock += "la " + splitArrStoreParam[0] + ", " + splitArrStoreParam[1] + "\n";
                    textBlock += "add " + splitArrStoreParam[0] + ", " + splitArrStoreParam[0] + ", " + params[1] + "\n";
                    textBlock += "sw " + params[2] + " 0(" + splitArrStoreParam[0] + ")\n";
                    break;
                case IntermediateCode.ARRAY_LOAD:
                    // params[0] is register to use, params[1] is array var, params[2] is index
                    textBlock += "la " + params[0] + ", " + params[1] + "\n";
                    textBlock += "add " + params[0] + ", " + params[0] + ", " + params[2] + "\n";
                    textBlock += "lw " + params[0] + " 0(" + params[0] + ")\n";
                    break;
                case IntermediateCode.ARRAY_ASSIGN:
                    int size = Integer.parseInt(params[1]);
                    data.put(params[0], ".space " + size * 4);
                    break;
                case IntermediateCode.LDR:
                    textBlock += "lw " + params[0] + ", " + params[1] + "\n";
                    break;
                case IntermediateCode.STR:
                    data.put(params[1], ".space 4");
                    textBlock += "sw " + params[0] + ", " + params[1] + "\n";
                    break;
                default:
                    LabelCode lc = (LabelCode)instruction;
                    textBlock += lc.toString() + ":" + "\n";
                    break;
            }
        }

        for (String var : data.keySet()) {
            dataBlock += var + ": " + data.get(var) + "\n";
        }

        return ".data\n" + dataBlock + "\n.text\n" + textBlock + "jr $ra";
    }
}
