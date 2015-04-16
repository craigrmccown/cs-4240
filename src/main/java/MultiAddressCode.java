/**
 * Created by andrewmehlberg on 4/2/15.
 */
public class MultiAddressCode extends IntermediateCode {

    private int opcode;
    private String functionName;
    private String retAddress = null;
    private String[] params;
    private final int NUMPARAMS;

    public MultiAddressCode(int opcode, String functionName, String[] params) {
        this.opcode = opcode;
        this.functionName = functionName;
        this.params = params;
        NUMPARAMS = params.length;
    }

    public MultiAddressCode(int opcode, String functionName, String retAddress, String[] params) {
        this.opcode = opcode;
        this.functionName = functionName;
        this.retAddress = retAddress;
        this.params = params;
        NUMPARAMS = params.length;
    }

    public String[] getParams() {
        return params;
    }

    public String getRetAddress() {
        if(retAddress!=null) {
            return retAddress;
        } else {
            return "";
        }
    }

    public void changeRetAddress(String returnAddress) {
        if(retAddress!=null) {
            retAddress = returnAddress;
        } else {
            System.out.println("This doesn't have a return address!");
        }
    }

    public void changeParam(int paramNum, String param) {
        if(paramNum<NUMPARAMS && paramNum>=0) {
            params[paramNum] = param;
        } else {
            System.out.println("You entered a parameter num that isn't valid");
            System.out.println("In multiaddresscode, paramNum ="+paramNum);
        }
    }

    public int getOpcode() {
        return opcode;
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
