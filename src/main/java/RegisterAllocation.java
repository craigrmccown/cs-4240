import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RegisterAllocation {
    private static final int NUM_INT_REGISTERS = 27;
    private static final int NUM_FP_REGISTERS = 15;
    //don't judge

    private class EbbTuple {
        public BasicBlock root;
        public HashSet<BasicBlock> blocks;

        public EbbTuple(BasicBlock root, HashSet<BasicBlock> blocks) {
            this.root = root;
            this.blocks = blocks;
        }
    }

    private HashSet<BasicBlock> EbbRoots = new HashSet<BasicBlock>();
    private HashSet<EbbTuple> AllEbbs = new HashSet<EbbTuple>();


    public static List<IntermediateCode> naive(List<IntermediateCode> ir) {
        ArrayList<IntermediateCode> naiveIR = new ArrayList<IntermediateCode>();

        for (int i = 0; i < ir.size(); i ++) {
            int opcode = ir.get(i).getOpcode();
            String[] s = ir.get(i).getParams();

            if (opcode == IntermediateCode.ASSIGN) {
                // s[0] is left operand, s[1] is right operand
                String register;
                if(!isLiteral(s[1])) { // s[1] is a var
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t0", s[1], ""));
                    naiveIR.add(new FourAddressCode(IntermediateCode.STR, "$t0", s[0], ""));
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

    public LinkedList<IntermediateCode> cfgConstruction(IRGenerator input) {
        //ArrayList<IntermediateCode> cfgIR = new ArrayList<IntermediateCode>();
        RegisterAllocation reg = new RegisterAllocation();
        ArrayList<BasicBlock> arr = reg.getCFGConstruction(input);
        int opcode;

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
                    if(!isLiteral(s[1]) && !storeVars.contains(s[1])) {
                        loadVars.add(s[1]);
                    }
                } else if(opcode>=IntermediateCode.ADD && opcode <=IntermediateCode.OR) {
                    if(!isLiteral(s[0]) && !isLiteral(s[1])) { //both vars
                        if(s[0].equals(s[2]) && s[1].equals(s[2]) && !storeVars.contains(s[0])) { //ex: a = a+a
                            loadStoreVars.add(s[2]);
                        } else if(s[0].equals(s[2]) && !s[1].equals(s[2])) { //ex: a = a+b
                            if(!storeVars.contains(s[2])) {
                                loadStoreVars.add(s[2]);
                            }
                            if(!storeVars.contains(s[1]) && !loadVars.contains(s[1])) {
                                loadVars.add(s[1]);
                            }
                        } else if(!s[0].equals(s[2]) && s[1].equals(s[2])) { //ex: a = b+a
                            if(!storeVars.contains(s[2])) {
                                loadStoreVars.add(s[2]);
                            }
                            if(!storeVars.contains(s[0]) && !loadVars.contains(s[0])) {
                                loadVars.add(s[0]);
                            }
                        } else { //ex: a = b+c
                            if(!storeVars.contains(s[0]) && !loadVars.contains(s[0])) {
                                loadVars.add(s[0]);
                            }
                            if(!storeVars.contains(s[1]) && !loadVars.contains(s[1])) {
                                loadVars.add(s[1]);
                            }
                            if(!storeVars.contains(s[2])) {
                                storeVars.add(s[2]);
                            }
                        }
                    } else if(isLiteral(s[0]) && !isLiteral(s[1])) { //s[0] = num, s[1] = var
                        if(s[1].equals(s[2])) { //ex: a = 3+a
                            loadStoreVars.add(s[2]);
                        } else { //ex: a = 3+b
                            if(!storeVars.contains(s[1]) && !loadVars.contains(s[1])) {
                                loadVars.add(s[1]);
                            }
                            if(!storeVars.contains(s[2])) {
                                storeVars.add(s[2]);
                            }
                        }
                    } else if(!isLiteral(s[0]) && isLiteral(s[1])) { //s[0] = var, s[1] = num
                        if(s[0].equals(s[2])) { //ex: a = a+3
                            loadStoreVars.add(s[2]);
                        } else { //ex: a = b+3
                            if(!storeVars.contains(s[0]) && !loadVars.contains(s[0])){
                                loadVars.add(s[0]);
                            }
                            if(!storeVars.contains(s[2])) {
                                storeVars.add(s[2]);
                            }
                        }
                    } else if(isLiteral(s[0]) && isLiteral(s[1])) { //both nums
                        //ex: a = 3+5
                        if(storeVars.contains(s[2])) {
                            storeVars.add(s[2]);
                        }
                    }
                } else if(opcode >= IntermediateCode.BREQ && opcode <= IntermediateCode.BRGEQ) {
                    if(!isLiteral(s[0]) && !storeVars.contains(s[0]) && !loadVars.contains(s[0])) {
                        loadVars.add(s[0]);
                    }
                    if(!isLiteral(s[1]) && !storeVars.contains(s[1]) && !loadVars.contains(s[1])) {
                        loadVars.add(s[1]);
                    }
                } else if(opcode == IntermediateCode.RETURN) {
                    if(s[0].length() > 0) { //param in the 1st
                        if(!isLiteral(s[0]) && !storeVars.contains(s[0]) && !loadVars.contains(s[0])) { //it is a variable
                            loadVars.add(s[0]);
                        }
                    }
                } else if(opcode == IntermediateCode.CALL || opcode == IntermediateCode.CALLR) {
                    int numParams = s.length;
                    for(int k = 0; k<numParams; k++) {
                        if(!isLiteral(s[k]) && !storeVars.contains(s[k]) && !loadVars.contains(s[k])) { //not a number
                            loadVars.add(s[k]);
                        }
                    }
                    String retAddress = code.get(j).getRetAddress();
                    if(opcode == IntermediateCode.CALLR && retAddress.length() > 0 && !storeVars.contains(retAddress)) {
                        storeVars.add(retAddress);
                    }
                } else if(opcode == IntermediateCode.ARRAY_STORE) {
                    //arr[#] = a
                    if(!isLiteral(s[1]) && !storeVars.contains(s[1]) && !loadVars.contains(s[1])) {
                        loadVars.add(s[1]);
                    }
                    if(!isLiteral(s[2]) && !storeVars.contains(s[2]) && !loadVars.contains(s[2])) {
                        loadVars.add(s[2]);
                    }
                } else if(opcode == IntermediateCode.ARRAY_LOAD) {
                    //a = arr[#]
                    if(!isLiteral(s[2]) && !loadVars.contains(s[2]) && !storeVars.contains(s[2])) {
                        loadVars.add(s[2]);
                    }
                    if(!storeVars.contains(s[0])) {
                        storeVars.add(s[0]);
                    }
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

            arr.get(i).setLoadVars(loadVars);
            arr.get(i).setStoreVars(storeVars);
            arr.get(i).setLoadStoreVars(loadStoreVars);

            //
            if(loadVars.size() + storeVars.size() + loadStoreVars.size() <= 29) {
                //can't use $zero, $k0 and $k1 so we are left with 29 available registers
                //1-25, 28-31
                String orig;
                int num = 0;

                

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
                    arr.get(i).replaceVariable(orig, getNextIntegerRegister(num));
                    if(labelStarted) {
                        arr.get(i).addCode(1, new FourAddressCode(IntermediateCode.LDR, getNextIntegerRegister(num), orig, ""));
                    } else {
                        arr.get(i).addCode(0, new FourAddressCode(IntermediateCode.LDR, getNextIntegerRegister(num), orig, ""));
                    }

                    num++;
                }

                for(int j = 0; j<loadStoreVars.size(); j++) {
                    orig = loadStoreVars.get(j);
                    arr.get(i).replaceVariable(orig, getNextIntegerRegister(num));
                    if(labelStarted) {
                        arr.get(i).addCode(1, new FourAddressCode(IntermediateCode.LDR, getNextIntegerRegister(num), orig, ""));
                    } else {
                        arr.get(i).addCode(0, new FourAddressCode(IntermediateCode.LDR, getNextIntegerRegister(num), orig, ""));
                    }


                    if(branchEnded) {
                        arr.get(i).addCode(code.size()-1, new FourAddressCode(IntermediateCode.STR, getNextIntegerRegister(num), orig, ""));
                    } else {
                        arr.get(i).addCode(new FourAddressCode(IntermediateCode.STR, getNextIntegerRegister(num), orig, ""));
                    }

                    num++;
                }

                for(int j = 0; j<storeVars.size(); j++) {
                    orig = storeVars.get(j);
                    arr.get(i).replaceVariable(orig, getNextIntegerRegister(num));

                    if(branchEnded) {
                        arr.get(i).addCode(code.size()-1, new FourAddressCode(IntermediateCode.STR, getNextIntegerRegister(num), orig, ""));
                    } else {
                        arr.get(i).addCode(new FourAddressCode(IntermediateCode.STR, getNextIntegerRegister(num), orig, ""));
                    }

                    num++;
                }
            } else {
                //need to calculate lowest spill costs



            }
        }

        LinkedList<IntermediateCode> temp;
        LinkedList<IntermediateCode> list = new LinkedList<IntermediateCode>();
        for(int i = 0; i<arr.size(); i++) {
            temp = arr.get(i).getEditedBlockCode();
            for(int j = 0; j<temp.size(); j++) {
                list.add(temp.get(j));
            }

        }

        return list;
        //return input;
    }

    public void ebbConstruction(IRGenerator input) {
        ArrayList<BasicBlock> bb = getCFGConstruction(input);
        BasicBlock x;
        EbbRoots.add(bb.get(0));
        while (!EbbRoots.isEmpty()) {
            x = EbbRoots.iterator().next();
            EbbRoots.remove(x);
            boolean containsX = false;
            for (EbbTuple tuple : AllEbbs) {
                if (tuple.root == x) containsX = true;
            }
            if (!containsX) {
                AllEbbs.add(buildEbb(x));
            }
        }
    }

    private EbbTuple buildEbb(BasicBlock root) {
        HashSet<BasicBlock> ebb = new HashSet<BasicBlock>();
        AddBbs(root, ebb);
        return new EbbTuple(root, ebb) ;
    }

    private void AddBbs(BasicBlock root, HashSet<BasicBlock> ebb) {
        ebb.add(root);
        for (BasicBlock block : root.getNextBlocks()) {
            if (block.getPrevBlocks().size() == 1 && !ebb.contains(block))
                AddBbs(block, ebb);
            else if (!EbbRoots.contains(block)) EbbRoots.add(block);
        }
    }


    public ArrayList<BasicBlock> getCFGConstruction(IRGenerator input) {

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


        return arr;
    }

    public static String getNextIntegerRegister(int numUsed) {
        String[] array = new String[NUM_INT_REGISTERS];
        array[0] = "$t0";
        array[1] = "$t1";
        array[2] = "$t2";
        array[3] = "$t3";
        array[4] = "$t4";
        array[5] = "$t5";
        array[6] = "$t6";
        array[7] = "$t7";
        array[8] = "$t8";
        array[9] = "$t9";

        array[10] = "$s0";
        array[11] = "$s1";
        array[12] = "$s2";
        array[13] = "$s3";
        array[14] = "$s4";
        array[15] = "$s5";
        array[16] = "$s6";
        array[17] = "$s7";

        array[18] = "$a1";
        array[19] = "$a2";
        array[20] = "$a3";

        array[21] = "$at";

        array[22] = "$v1";

        array[23] = "$gp";
        array[24] = "$sp";
        array[25] = "$fp";
        array[26] = "$ra";

        return array[numUsed];
    }

    public static String getNextFloatRegister(int numUsed) {
        String[] array = new String[NUM_FP_REGISTERS];
        array[0] = "$f0";
        array[1] = "$f1";
        array[2] = "$f2";
        array[3] = "$f3";
        array[4] = "$f4";
        array[5] = "$f5";
        array[6] = "$f6";
        array[7] = "$f7";
        array[8] = "$f8";
        array[9] = "$f9";
        array[10] = "$f10";
        array[11] = "$f11";
        array[12] = "$f13";
        array[13] = "$f14";
        array[14] = "$f15";

        return array[numUsed];
    }

    public static int getSpillCost(BasicBlock block, String variable) {
        LinkedList<IntermediateCode> code = block.getBlockCode();
        int count = 0;
        for(int i = 0; i<code.size(); i++) {
            int opcode = code.get(i).getOpcode();
            String[] s = code.get(i).getParams();
            if(opcode == IntermediateCode.ASSIGN && (s[0].equals(variable) || s[1].equals(variable))) {
                count++;
            } else if(opcode >= IntermediateCode.BREQ && opcode <= IntermediateCode.BRGEQ && (s[0].equals(variable) || s[1].equals(variable))) {
                count++;
            } else if(opcode>=IntermediateCode.ADD && opcode<=IntermediateCode.OR) {
                if((s[0].equals(variable) || s[1].equals(variable))) {
                    count++;
                }
                if(s[2].equals(variable)) {
                    count++;
                }
            } else if(opcode==IntermediateCode.RETURN && s.length > 0 && s[0].equals(variable)) {
                count++;
            } else if(opcode == IntermediateCode.ARRAY_LOAD) {
                if(variable.equals(s[0])) {
                    count++;
                } 
                if(variable.equals(s[2])) {
                    count++;
                }
            } else if(opcode == IntermediateCode.ARRAY_STORE) {
                if(variable.equals(s[1]) || variable.equals(s[2])) {
                    count++;
                }
            } else if(opcode == IntermediateCode.CALLR) {
                count++;
            } else if(opcode == IntermediateCode.CALL) {
                if(s[0].equals(variable)) {
                    count++;
                }
            }
        }
        return count;
    }

    /*public static ArrayList<String> getLowestSpillCosts(BasicBlock block, ArrayList<String> vars, int numLowest) {
        ArrayList<String> returnList = new ArrayList<String>();
        int lowest = getSpillCost(block, vars.get(0));
        for(int i = 0; i<numLowest; i++) {
            for(int j = 0; j<vars.size(); j++) {
                if(getSpillCost(block,vars.get(j)) < lowest && !returnList.contains(vars.get(j))) {
                    lowest = 
                }
            }
        }

        return returnList;
    }*/


    public class BasicBlock {
        private LinkedList<IntermediateCode> block;
        private LinkedList<IntermediateCode> editedBlock;
        private ArrayList<BasicBlock> nextBlocks;
        private ArrayList<BasicBlock> prevBlocks;
        int start;
        int end;
        private ArrayList<String> loadVars;
        private ArrayList<String> storeVars;
        private ArrayList<String> loadStoreVars;

        public BasicBlock(int start, int end, LinkedList<IntermediateCode> block) {
            this.start = start;
            this.end = end;
            this.block = block;
            editedBlock = block;
            nextBlocks = new ArrayList<BasicBlock>();
            prevBlocks = new ArrayList<BasicBlock>();
            loadVars = new ArrayList<String>();
            storeVars = new ArrayList<String>();
            loadStoreVars = new ArrayList<String>();
        }

        public BasicBlock(int start, int end) {
            this.start = start;
            this.end = end;
            nextBlocks = new ArrayList<BasicBlock>();
            prevBlocks = new ArrayList<BasicBlock>();
            loadVars = new ArrayList<String>();
            storeVars = new ArrayList<String>();
            loadStoreVars = new ArrayList<String>();
        }

        public void setBlockCode(LinkedList<IntermediateCode> blockCode) {
            block = blockCode;
            editedBlock = blockCode;
        }

        public LinkedList<IntermediateCode> getBlockCode() {
            return block;
        }

        public LinkedList<IntermediateCode> getEditedBlockCode() {
            return editedBlock;
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
            editedBlock.add(index, code);
        }

        public void addCode(IntermediateCode code) {
            editedBlock.add(code);
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

        public void setLoadVars(ArrayList<String> loadVars) {
            this.loadVars = loadVars;
        }

        public void setStoreVars(ArrayList<String> storeVars) {
            this.storeVars = storeVars;
        }

        public void setLoadStoreVars(ArrayList<String> loadStoreVars) {
            this.loadStoreVars = loadStoreVars;
        }
    }
}