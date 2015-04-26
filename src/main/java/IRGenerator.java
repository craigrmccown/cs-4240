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

    /**
    * Used for inserting loads and stores into the linkedlist at location index
    * Note that the third param is empty because it isn't needed
    *
    * @param index The index of the linked list entry
    * @param opcode The IR opcode
    * @param param1
    * @param param2
    */
    public void emitAtIndex(int index, int opcode, String param1, String param2) {
        operations.add(index, new FourAddressCode(opcode, param1, param2, ""));
    }
    
    /**
    * Gets the parameters of the intermediate code at location
    * index in the linked list and returns them as an array
    *
    * @param index The index of the linked list entry
    * @return The parameters in an array
    */
    public String[] getParams(int index) {
        return operations.get(index).getParams();
    }

    /**
    * Returns the opcode associated with a certain Intermediate Code instruction
    * 
    * @param index The index of the linked list entry
    * @return The opcode if it had one, -1 if it is a label
    */
    public int getOpcode(int index) {
        return operations.get(index).getOpcode();
    }

    /**
    * Used to change the parameters of intermediate code
    * Needed because the temporary variables are used as 
    * the storage locations and are replaced in operations by registers
    *
    * @param index The index of the linked list entry
    * @param paramNum The parameter number
    * @param param The new parameter name
    */
    public void changeParam(int index, int paramNum, String param) {
        operations.get(index).changeParam(paramNum, param);
    }

    /**
    * Gets the return address if it is a CALLR
    * otherwise returns an empty string
    *
    * @param index The index of the linked list entry
    * @return The return address
    */
    public String getRetAddress(int index) {
        return operations.get(index).getRetAddress();
    }

    /**
    * Changes the return address if it is a CALLR
    * otherwise does nothing
    *
    * @param index The index of the linked list entry
    * @param retAddress The string to change the return address to
    */
    public void changeRetAddress(int index, String retAddress) {
        operations.get(index).changeRetAddress(retAddress);
    }

/*
    public IRGenerator getSegment(int start, int end) {

    }*/

    public int getSize() {
        return operations.size();
    }
    
    public String toString(int index) {
        return operations.get(index).toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IntermediateCode ic : operations) {
            sb.append(ic + "\n");
        }
        return sb.toString();
    }
}
