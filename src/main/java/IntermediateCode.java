/**
 * Created by andrewmehlberg on 4/2/15.
 */
public abstract class IntermediateCode {

    public static final int ASSIGN = 0;
    public static final int ADD = 1;
    public static final int SUB = 2;
    public static final int MULT = 3;
    public static final int DIV = 4;
    public static final int AND = 5;
    public static final int OR = 6;
    public static final int GOTO = 7;
    public static final int BREQ = 8;
    public static final int BRNEQ = 9;
    public static final int BRLT = 10;
    public static final int BRGT = 11;
    public static final int BRLEQ = 12;
    public static final int BRGEQ = 13;
    public static final int RETURN = 14;
    public static final int CALL = 15;
    public static final int CALLR = 16;
    public static final int ARRAY_STORE = 17;
    public static final int ARRAY_LOAD = 18;
    public static final int ARRAY_ASSIGN = 19;

    public static String opString(int opcode) {
        switch (opcode) {
            case ASSIGN: return "assign";
            case ADD: return "add";
            case SUB: return "sub";
            case MULT: return "mult";
            case DIV: return "div";
            case AND: return "and";
            case OR: return "or";
            case GOTO: return "goto";
            case BREQ: return "breq";
            case BRNEQ: return "brneq";
            case BRLT: return "brlt";
            case BRGT: return "brgt";
            case BRLEQ: return "brleq";
            case BRGEQ: return "brgeq";
            case RETURN: return "return";
            case CALL: return "call";
            case CALLR: return "callr";
            case ARRAY_STORE: return "arr_store";
            case ARRAY_LOAD: return "arr_load";
            case ARRAY_ASSIGN: return "arr_assign";
            default: return "";
        }
    }
}
