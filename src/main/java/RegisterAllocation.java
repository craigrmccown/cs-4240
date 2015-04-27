import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RegisterAllocation {
    public static List<IntermediateCode> naive(List<IntermediateCode> ir, Map<String, Symbol> symbolTable) {
        ArrayList<IntermediateCode> naiveIR = new ArrayList<IntermediateCode>();

        for (int i = 0; i < ir.size(); i ++) {
            int opcode = ir.get(i).getOpcode();
            String[] s = ir.get(i).getParams();

            if (opcode == IntermediateCode.ASSIGN) {
                // s[0] is left operand, s[1] is right operand
                String register;
                if(!isLiteral(s[1])) { // s[1] is a var
                    if (symbolTable.get(s[1]).getDataType().equals("fixedpt")) {
                        register = "$f0";
                        naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$f0", s[1], ""));
                        naiveIR.add(new FourAddressCode(IntermediateCode.STR, "$f0", s[0], ""));
                    } else if (symbolTable.get(s[1]).getDataType().equals("int")) {
                        register = "$t0";
                        naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t0", s[1], ""));
                        naiveIR.add(new FourAddressCode(IntermediateCode.STR, "$t0", s[0], ""));
                    } else {
                        register = "$t0";
                        System.out.println("Something went wrong in assign");
                    }

                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, register, s[1], ""));
                    naiveIR.add(new FourAddressCode(IntermediateCode.STR, register, s[0], ""));
                } else {
                    naiveIR.add(new FourAddressCode(IntermediateCode.ADD, "$t0", "$zero", s[1]));
                    naiveIR.add(new FourAddressCode(IntermediateCode.STR, "$t0", s[0], ""));
                }
            } else if(opcode>=IntermediateCode.ADD && opcode<=IntermediateCode.OR) {
                // add, sub, mult, div, and, or
                // s[0] is left operand, s[1] is right operand, s[2] is destination
                if(!isLiteral(s[0]) && !isLiteral(s[1])) { //both vars
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t0", s[0], ""));
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t1", s[1], ""));
                    naiveIR.add(new FourAddressCode(opcode, "$t0", "$t0", "$t1"));
                } else if(isLiteral(s[0]) && !isLiteral(s[1])) { //s[1] is a var
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t0", s[1], ""));
                    naiveIR.add(new FourAddressCode(opcode, "$t0", "$t0", s[0]));
                } else if(!isLiteral(s[0]) && isLiteral(s[1])) { //s[0] is a var
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t0", s[0], ""));
                    naiveIR.add(new FourAddressCode(opcode, "$t0", "$t0", s[1]));
                } else { // both #s
                    naiveIR.add(new FourAddressCode(IntermediateCode.ADD, "$t0", "$zero", s[0]));
                    naiveIR.add(new FourAddressCode(opcode, "$t0", "$t0", s[1]));
                }
                naiveIR.add(new FourAddressCode(IntermediateCode.STR, "$t0", s[2], ""));
            } else if(opcode >= IntermediateCode.BREQ && opcode <= IntermediateCode.BRGEQ) { //is a branch statement
                //have a label in 3rd param and 
                //1st 2 params can be numbers or vars
                // s[0] is left operand, s[1] is right operand, s[2] is label
                if (!isLiteral(s[0]) && !isLiteral(s[1])) {//both vars
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t0", s[0], ""));
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t1", s[1], ""));
                    naiveIR.add(new FourAddressCode(opcode, "$t0", "$t1", s[2]));
                } else if (isLiteral(s[0]) && !isLiteral(s[1])) { //s[1] is a var
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t0", s[1], ""));
                    naiveIR.add(new FourAddressCode(opcode, s[0], "$t0", s[2]));
                } else if (!isLiteral(s[0]) && isLiteral(s[1])) { //s[0] is a var
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t0", s[0], ""));
                    naiveIR.add(new FourAddressCode(opcode, "$t0", s[1], s[2]));
                } else {
                    naiveIR.add(new FourAddressCode(IntermediateCode.ADD, "$t0", "$zero", s[0]));
                    naiveIR.add(new FourAddressCode(opcode, "$t0", s[1], s[2]));
                }
            } else if (opcode == IntermediateCode.GOTO) {
                naiveIR.add(new FourAddressCode(IntermediateCode.GOTO, s[0], "", ""));
            } else if(opcode == IntermediateCode.RETURN) {
                // omitting return
            } else if(opcode == IntermediateCode.CALL) { //method call
                // s[0] is function argument
                MultiAddressCode functionCall = (MultiAddressCode) ir.get(i);
                if (!isLiteral(s[0])) {
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t0", s[0], ""));
                    naiveIR.add(new FourAddressCode(IntermediateCode.CALL, "$t0", functionCall.getFunctionName(), ""));
                } else {
                    naiveIR.add(new FourAddressCode(IntermediateCode.CALL, s[0], functionCall.getFunctionName(), ""));
                }
            } else if (opcode == IntermediateCode.CALLR) {
                MultiAddressCode functionCall = (MultiAddressCode) ir.get(i);
                naiveIR.add(new FourAddressCode(IntermediateCode.CALLR, "$t0", functionCall.getFunctionName(), ""));
                naiveIR.add(new FourAddressCode(IntermediateCode.STR, "$t0", functionCall.getRetAddress(), ""));
            } else if(opcode == IntermediateCode.ARRAY_STORE) {
                // s[0] is register|array var, s[1] is index, s[2] is value
                // using '|' character as hack to include more than 3 params
                if(!isLiteral(s[1]) && !isLiteral(s[2])) { //both vars
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t1", s[1], ""));
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t2", s[2], ""));
                } else if (isLiteral(s[1]) && !isLiteral(s[2])) { //s[2] is a var
                    naiveIR.add(new FourAddressCode(IntermediateCode.ADD, "$t1", "$zero", s[1]));
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t2", s[2], ""));
                } else if (!isLiteral(s[1]) && isLiteral(s[2])) { //s[1] is a var
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t1", s[1], ""));
                    naiveIR.add(new FourAddressCode(IntermediateCode.ADD, "$t2", "$zero", s[2]));
                } else {// both #s
                    naiveIR.add(new FourAddressCode(IntermediateCode.ADD, "$t1", "$zero", s[1]));
                    naiveIR.add(new FourAddressCode(IntermediateCode.ADD, "$t2", "$zero", s[2]));
                }
                naiveIR.add(new FourAddressCode(IntermediateCode.MULT, "$t1", "$t1", "4"));
                naiveIR.add(new FourAddressCode(IntermediateCode.ARRAY_STORE, "$t0|" + s[0], "$t1", "$t2"));
            } else if(opcode == IntermediateCode.ARRAY_LOAD) {
                // s[0] is destination var, s[1] is array var, s[2] is index
                if(!isLiteral(s[2])) { // s[2] is a var
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t1", s[2], ""));
                } else { // s[2] is a #
                    naiveIR.add(new FourAddressCode(IntermediateCode.ADD, "$t1", "$zero", s[2]));
                }
                naiveIR.add(new FourAddressCode(IntermediateCode.MULT, "$t1", "$t1", "4"));
                naiveIR.add(new FourAddressCode(IntermediateCode.ARRAY_LOAD, "$t0", s[1], "$t1"));
                naiveIR.add(new FourAddressCode(IntermediateCode.STR, "$t0", s[0], ""));
            } else if(opcode == IntermediateCode.ARRAY_ASSIGN) {
                // s[0] is array var, s[1] is array size, s[2] is initial value
                naiveIR.add(new FourAddressCode(IntermediateCode.ARRAY_ASSIGN, s[0], s[1], s[2]));
            } else if(ir.get(i) instanceof  LabelCode) {
                naiveIR.add(new LabelCode(ir.get(i).getLabel()));
            } else {
                System.out.println("Something went wrong! Opcode: "+opcode);
            }
        }

        return naiveIR;
    }

    public static boolean isLiteral(String varOrLit) {
        return varOrLit.matches("^-?[0-9]*\\.?[0-9]+$");
    }

    public static boolean isFloat(String floatOrNot) {
        return floatOrNot.matches("^-?[0-9]*\\.[0-9]+$");
    }

    public static String resolveDataType(String var, Map<String, Symbol> symbolTable) {
        Symbol s = symbolTable.get(var);

        if (!s.getDataType().equals("int") && !s.getDataType().equals("fixedpt")) {
            s = symbolTable.get(s.getDataType());
        }

        return s.getDataType();
    }

    public void cfgConstruction(IRGenerator input) {
        int size = input.getSize();
        int opcode;
        int startBlock = 0;
        ArrayList<BasicBlock> arr = new ArrayList<BasicBlock>();
        boolean prevBranchOrReturn = false;

        //determining all the basic blocks
        for(int i = 0; i<size; i++) {
            opcode = input.getOpcode(i);

            if(i!=0 && (prevBranchOrReturn || opcode==-1)) { //new leader statement
                //creates a new basic block
                prevBranchOrReturn = false;
                arr.add(new BasicBlock(startBlock, i-1));

                startBlock = i;
            }

            //reached the end of the intermediate code
            if(i+1==size) {
                arr.add(new BasicBlock(startBlock, i));
            }

            if(opcode >= IntermediateCode.GOTO && opcode <= IntermediateCode.RETURN) {
                //statements after goto, branches and returns are the beginning
                //of a basic block
                prevBranchOrReturn = true;
            }
        }

        //loading intermediate code into the basic blocks
        for(int i = 0; i<arr.size(); i++) {
            arr.get(i).setBlockCode(input.getSegment(arr.get(i).getStart(), arr.get(i).getEnd()));
        }


        //linking the basic blocks together for CFG
        for(int i = 0; i<arr.size(); i++) {
            LinkedList<IntermediateCode> code = arr.get(i).getBlockCode();

            //looping through the block code
            for(int j = 0; j<code.size(); j++) {
                opcode = code.get(j).getOpcode();

                if(opcode == IntermediateCode.GOTO) { //unconditional branch to another basic block
                    //get the label that you're branching to
                    String[] array = code.get(j).getParams();
                    String label = array[0];

                    for(int k = 0; k<arr.size(); k++) {
                        if(arr.get(k).containsLabel(label)) { //branches to this block
                            arr.get(i).addNext(arr.get(k));
                            arr.get(k).addPrev(arr.get(i));
                        }
                    }
                } else if(opcode >= IntermediateCode.BREQ && opcode <= IntermediateCode.BRGEQ) { //conditional branch to another block
                    //get the label that you're branching to
                    String[] array = code.get(j).getParams();
                    String label = array[2];

                    for(int k = 0; k<arr.size(); k++) {
                        if(arr.get(k).containsLabel(label) && k!=i+1) { //conditionally branches to this block
                            arr.get(i).addNext(arr.get(k));
                            arr.get(k).addPrev(arr.get(i));
                        }
                    }

                } 

                if(j+1 == code.size() && i+1 != arr.size() && opcode != IntermediateCode.GOTO) { //not last basic block or unconditional branch
                    arr.get(i).addNext(arr.get(i+1));
                    arr.get(i+1).addPrev(arr.get(i));
                }
            }
        }

        /*
        * Loops through the list of the basic blocks.
        * Inside each basic block, it loops through the code
        * and determines which variables need a load operation,
        * store operation or both. 
        */
        for(int i = 0; i<arr.size(); i++) {
            LinkedList<IntermediateCode> code = arr.get(i).getBlockCode();
            ArrayList<String> loadVars = new ArrayList<String>();
            ArrayList<String> storeVars = new ArrayList<String>();
            ArrayList<String> loadStoreVars = new ArrayList<String>();

            //looping through code determine load/store operations needed
            for(int j = 0; j<code.size(); j++) {
                opcode = code.get(j).getOpcode();
                String[] s = code.get(j).getParams();

                if(opcode == IntermediateCode.ASSIGN) {
                    storeVars.add(s[0]);
                    if(!s[1].matches("^-?[0-9]*\\.?[0-9]+$")) {
                        loadVars.add(s[1]);
                    }
                } else if(opcode>=IntermediateCode.ADD && opcode<=IntermediateCode.OR) {
                    if(!s[0].matches("^-?[0-9]*\\.?[0-9]+$") && !s[1].matches("^-?[0-9]*\\.?[0-9]+$")) { //both vars
                        if(s[0].equals(s[2]) && s[1].equals(s[2])) { //ex: a = a+a
                            loadStoreVars.add(s[2]);
                        } else if(s[0].equals(s[2]) && !s[1].equals(s[2])) { //ex: a = a+b
                            loadStoreVars.add(s[2]);
                            loadVars.add(s[1]);
                        } else if(!s[0].equals(s[2]) && s[1].equals(s[2])) { //ex: a = b+a
                            loadStoreVars.add(s[2]);
                            loadVars.add(s[0]);
                        } else { //ex: a = b+c
                            loadVars.add(s[0]);
                            loadVars.add(s[1]);
                            storeVars.add(s[2]);
                        }
                    } else if(s[0].matches("^-?[0-9]*\\.?[0-9]+$") && !s[1].matches("^-?[0-9]*\\.?[0-9]+$")) { //s[0] = num, s[1] = var
                        if(s[1].equals(s[2])) { //ex: a = 3+a
                            loadStoreVars.add(s[2]);
                        } else { //ex: a = 3+b
                            loadVars.add(s[1]);
                            storeVars.add(s[2]);
                        }
                    } else if(!s[0].matches("^-?[0-9]*\\.?[0-9]+$") && s[1].matches("^-?[0-9]*\\.?[0-9]+$")) { //s[0] = var, s[1] = num
                        if(s[0].equals(s[2])) { //ex: a = a+3
                            loadStoreVars.add(s[2]);
                        } else { //ex: a = b+3
                            loadVars.add(s[0]);
                            storeVars.add(s[2]);
                        }
                    } else if(s[0].matches("^-?[0-9]*\\.?[0-9]+$") && s[1].matches("^-?[0-9]*\\.?[0-9]+$")) { //both nums
                        //ex: a = 3+5
                        storeVars.add(s[2]);
                    }
                } else if(opcode >= IntermediateCode.BREQ && opcode <= IntermediateCode.BRGEQ) {
                    if(!s[0].matches("^-?[0-9]*\\.?[0-9]+$")) {
                        loadVars.add(s[0]);
                    }
                    if(!s[1].matches("^-?[0-9]*\\.?[0-9]+$")) {
                        loadVars.add(s[1]);
                    }
                } else if(opcode == IntermediateCode.RETURN) {
                    if(s[0].length() > 0) { //param in the 1st
                        if(!s[0].matches("^-?[0-9]*\\.?[0-9]+$")) { //it is a variable
                            loadVars.add(s[0]);
                        }
                    }
                } else if(opcode == IntermediateCode.CALL || opcode == IntermediateCode.CALLR) {
                    int numParams = s.length;
                    for(int k = 0; k<numParams; k++) {
                        if(!s[k].matches("^-?[0-9]*\\.?[0-9]+$")) { //not a number
                            loadVars.add(s[k]);
                        }
                    }
                    String retAddress = code.get(j).getRetAddress();
                    if(opcode == IntermediateCode.CALLR && retAddress.length() > 0) {
                        storeVars.add(retAddress);
                    }
                } else if(opcode == IntermediateCode.ARRAY_STORE) {
                    //arr[#] = a
                    if(!s[2].matches("^-?[0-9]*\\.?[0-9]+$")) {
                        loadVars.add(s[2]);
                    }
                } else if(opcode == IntermediateCode.ARRAY_LOAD) {
                    //a = arr[#]
                    storeVars.add(s[0]);
                } else if(opcode == IntermediateCode.ARRAY_ASSIGN) {
                    //not sure if anything needs to be done
                }
            }

            //removing duplicates from storeVars
            for(int n = 0; n<loadStoreVars.size(); n++) {
                if(storeVars.contains(loadStoreVars.get(n))) {
                    for(int j = 0; j<storeVars.size(); j++) {
                        if(storeVars.get(j).equals(loadStoreVars.get(n))) {
                            storeVars.remove(j);
                        }
                    }
                }
            }

            //removing duplicates from loadVars
            for(int n = 0; n<loadStoreVars.size(); n++) {
                if(loadVars.contains(loadStoreVars.get(n))) {
                    for(int j = 0; j<loadVars.size(); j++) {
                        if(loadVars.get(j).equals(loadStoreVars.get(n))) {
                            loadVars.remove(j);
                        }
                    }
                }
            }

            System.out.println("Block "+i);
            System.out.println("Load vars");
            for(int n = 0; n<loadVars.size(); n++) {
                System.out.println("    "+loadVars.get(n));
            }
            System.out.println();

            System.out.println("Load Store Vars");
            for(int n = 0; n<loadStoreVars.size(); n++) {
                System.out.println("    "+loadStoreVars.get(n));
            }
            System.out.println();

            System.out.println("store vars");
            for(int n = 0; n<storeVars.size(); n++) {
                System.out.println("    "+storeVars.get(n));
            }
            System.out.println();

            //
            if(loadVars.size() + storeVars.size() + loadStoreVars.size() <= 29) {
                //can't use $zero, $k0 and $k1 so we are left with 29 available registers
                //1-25, 28-31
                String orig;
                int num = 1;

                

                //make sure the loads and stores are after labels
                // and before branches
                boolean labelStarted = false;
                boolean branchEnded = false;
                opcode = code.get(0).getOpcode();

                if(opcode == -1) {
                    labelStarted = true;
                }

                opcode = code.get(code.size()-1).getOpcode();

                if(opcode >= IntermediateCode.GOTO && opcode <= IntermediateCode.BRGEQ) {
                    branchEnded = true;
                }

                for(int j = 0; j<loadVars.size(); j++) {
                    orig = loadVars.get(j);
                    arr.get(i).replaceVariable(orig, "lv$"+num);
                    if(labelStarted) {
                        arr.get(i).addCode(1, new FourAddressCode(IntermediateCode.LDR, "lv$"+num, orig, ""));
                    } else {
                        arr.get(i).addCode(0, new FourAddressCode(IntermediateCode.LDR, "lv$"+num, orig, ""));
                    }

                    if(num==25) {
                        num=28;
                    } else {
                        num++;
                    }
                }

                for(int j = 0; j<loadStoreVars.size(); j++) {
                    orig = loadStoreVars.get(j);
                    arr.get(i).replaceVariable(orig, "lsv$"+num);
                    if(labelStarted) {
                        arr.get(i).addCode(1, new FourAddressCode(IntermediateCode.LDR, "lsv$"+num, orig, ""));
                    } else {
                        arr.get(i).addCode(0, new FourAddressCode(IntermediateCode.LDR, "lsv$"+num, orig, ""));
                    }


                    if(branchEnded) {
                        arr.get(i).addCode(code.size()-1, new FourAddressCode(IntermediateCode.STR, "lsv$"+num, orig, ""));
                    } else {
                        arr.get(i).addCode(new FourAddressCode(IntermediateCode.STR, "lsv$"+num, orig, ""));
                    }

                    if(num==25) {
                        num=28;
                    } else {
                        num++;
                    }
                }

                for(int j = 0; j<storeVars.size(); j++) {
                    orig = storeVars.get(j);
                    arr.get(i).replaceVariable(orig, "sv$"+num);

                    if(branchEnded) {
                        arr.get(i).addCode(code.size()-1, new FourAddressCode(IntermediateCode.STR, "sv$"+num, orig, ""));
                    } else {
                        arr.get(i).addCode(new FourAddressCode(IntermediateCode.STR, "sv$"+num, orig, ""));
                    }

                    if(num==25) {
                        num=28;
                    } else {
                        num++;
                    }
                }
            } else {
                //need to calculate lowest spill costs

            }
        }
        
        for(int i = 0; i<arr.size(); i++) {
            System.out.println("Basic Block " + i + ":");
            System.out.println("    Start: "+ arr.get(i).getStart());
            System.out.println("    End: " + arr.get(i).getEnd());
            LinkedList<IntermediateCode> list = arr.get(i).getBlockCode();
            for(int j = 0; j<list.size(); j++) {
                System.out.println("        "+ list.get(j).toString());
            }
            
            ArrayList<BasicBlock> arrlist = arr.get(i).getNextBlocks();
            for(int k = 0; k<arrlist.size(); k++) {
                System.out.println("            Start: " + arrlist.get(k).getStart());
                System.out.println("            End: "+ arrlist.get(k).getEnd());
            }
        }


        //return input;
    }

    public void ebbConstruction(IRGenerator input) {
        
    }


    private class BasicBlock {
        private LinkedList<IntermediateCode> block;
        private ArrayList<BasicBlock> nextBlocks;
        private ArrayList<BasicBlock> prevBlocks;
        int start;
        int end;

        public BasicBlock(int start, int end, LinkedList<IntermediateCode> block) {
            this.start = start;
            this.end = end;
            this.block = block;
            nextBlocks = new ArrayList<BasicBlock>();
            prevBlocks = new ArrayList<BasicBlock>();
        }

        public BasicBlock(int start, int end) {
            this.start = start;
            this.end = end;
            nextBlocks = new ArrayList<BasicBlock>();
            prevBlocks = new ArrayList<BasicBlock>();
        }

        public void setBlockCode(LinkedList<IntermediateCode> blockCode) {
            block = blockCode;
        }

        public LinkedList<IntermediateCode> getBlockCode() {
            return block;
        }

        public void addNext(BasicBlock next) {
            nextBlocks.add(next);
        }

        public void addPrev(BasicBlock prev) {
            prevBlocks.add(prev);
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public boolean containsLabel(String label) {
            for(int i = 0; i<block.size(); i++) {
                if(block.get(i).getOpcode()==-1) {
                    if(block.get(i).getLabel()==label) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void replaceVariable(String orig, String repl) {
            for(int i = 0; i<block.size(); i++) {
                String[] s = block.get(i).getParams();
                for(int j = 0; j<s.length; j++) {
                    if(s[j].equals(orig)) {
                        block.get(i).changeParam(j, repl);
                    }
                }
            }
        }

        public void addCode(int index, IntermediateCode code) {
            block.add(index, code);
        }

        public void addCode(IntermediateCode code) {
            block.add(code);
        }

        public ArrayList<BasicBlock> getNextBlocks() {
            return nextBlocks;
        }

        public ArrayList<BasicBlock> getPrevBlocks() {
            return prevBlocks;
        }

        public ArrayList<BasicBlock> getAllBlocks() {
            ArrayList<BasicBlock> allBlocks = new ArrayList<BasicBlock>();
            allBlocks.addAll(nextBlocks);
            allBlocks.addAll(prevBlocks);
            return allBlocks;
        }
    }

    /*private class Block {
        private int start;
        private int end;

        public Block(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
    }*/
}