import java.util.LinkedList;

public class IRGenerator {

    private int labelCount, tempCount = 0;

    private LinkedList<IntermediateCode> operations;

    public IRGenerator() {
        operations = new LinkedList<IntermediateCode>();
    }

    public void emit(int opcode, String param1, String param2, String param3) {
        operations.addLast(new FourAddressCode(opcode, param1, param2, param3));
    }

    public void emitLabel(String label) {
        operations.addLast(new LabelCode(label));
    }

    public void emitCall(int opcode, String name, String[] params) {
        operations.addLast(new MultiAddressCode(opcode, name, params));
    }

    public void emitCallWithReturn(int opcode, String retAddress, String name, String[] params) {
        operations.addLast(new MultiAddressCode(opcode, retAddress, name, params));
    }

    public String createLabel() {
        String ret = "label_" + labelCount;
        labelCount++;
        return ret;
    }

    public String createTemp(Scope scope) {
        while (scope.exists("t" + tempCount)) tempCount++;
        String ret = "t" + tempCount;
        tempCount++;
        return ret;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IntermediateCode ic : operations) {
            sb.append(ic + "\n");
        }
        return sb.toString();
    }
}
