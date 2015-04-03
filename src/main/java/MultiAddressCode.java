/**
 * Created by andrewmehlberg on 4/2/15.
 */
public class MultiAddressCode extends IntermediateCode {

    private int opcode;
    private String[] params;

    public MultiAddressCode(int opcode, String[] params) {
        this.opcode = opcode;
        this.params = params;
    }

    public String toString() {
        String prefix = IntermediateCode.opString(opcode);
        String postfix = "";
        for (String param : params) {
            postfix += ", " + param;
        }
        return prefix + postfix;
    }
}
