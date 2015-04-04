/**
 * Created by andrewmehlberg on 4/2/15.
 */
public class MultiAddressCode extends IntermediateCode {

    private int opcode;
    private String functionName;
    private String retAddress = null;
    private String[] params;

    public MultiAddressCode(int opcode, String functionName, String[] params) {
        this.opcode = opcode;
        this.functionName = functionName;
        this.params = params;
    }

    public MultiAddressCode(int opcode, String functionName, String retAddress, String[] params) {
        this.opcode = opcode;
        this.functionName = functionName;
        this.retAddress = retAddress;
        this.params = params;
    }

    public String toString() {
        String prefix = IntermediateCode.opString(opcode) + ", " + functionName;
        if (retAddress != null) prefix += ", " + retAddress;
        String postfix = "";
        for (String param : params) {
            postfix += ", " + param;
        }
        return prefix + postfix;
    }
}
