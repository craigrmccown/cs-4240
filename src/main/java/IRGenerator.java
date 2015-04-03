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

    public void emitFunctionCall(int opcode, String... params) {
        operations.addLast(new MultiAddressCode(opcode, params));
    }

    public String createLabel() {
        return "label_" + labelCount;
    }

    public String createTemp(Scope scope) {
        while (scope.lookup("t" + tempCount) != null) tempCount++;
        return "t" + tempCount;
    }
}
