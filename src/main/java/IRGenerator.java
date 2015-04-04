import java.util.LinkedList;

/**
 * Created by andrewmehlberg on 4/2/15.
 */
public class IRGenerator {

    private int labelCount, tempCount = 0;
    private SymbolTable symbolTable;

    private LinkedList<IntermediateCode> operations;

    public IRGenerator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
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

    public void emitCallWithReturn(int opcode, String name, String retAddress, String[] params) {
        operations.addLast(new MultiAddressCode(opcode, name, retAddress, params));
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
}
