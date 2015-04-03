/**
 * Created by andrewmehlberg on 4/2/15.
 */
public class FourAddressCode extends IntermediateCode {

    private int opcode;
    private String param1, param2, param3;

    public FourAddressCode(int opcode, String param1, String param2, String param3) {
        this.opcode = opcode;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public String toString() {
        return IntermediateCode.opString(opcode) + ", " + param1 + ", " + param2 + ", " + param3;
    }
}
