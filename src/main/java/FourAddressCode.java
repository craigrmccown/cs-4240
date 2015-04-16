/**
 * Created by andrewmehlberg on 4/2/15.
 */
public class FourAddressCode extends IntermediateCode {

    private int opcode;
    private String param1, param2, param3;
    private final int NUMPARAMS = 3;

    public FourAddressCode(int opcode, String param1, String param2, String param3) {
        this.opcode = opcode;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public int getOpcode() {
        return opcode;
    }

    public String[] getParams() {
        String[] s = new String[NUMPARAMS];
        s[0] = param1;
        s[1] = param2;
        s[2] = param3;
        return s;
    }

    public void changeParam(int paramNum, String param) {
        if(paramNum==0) {
            param1 = param;
        } else if(paramNum == 1) {
            param2 = param;
        } else if(paramNum == 2) {
            param3 = param;
        } else {
            System.out.println("You entered a parameter num that isn't valid");
            System.out.println("In fouraddresscode, paramNum ="+paramNum);
        }
    }

    public String toString() {
        return IntermediateCode.opString(opcode) + ", " + param1 + ", " + param2 + ", " + param3;
    }
}
