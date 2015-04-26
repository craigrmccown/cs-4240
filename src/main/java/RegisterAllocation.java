import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RegisterAllocation {
    public static List<IntermediateCode> naive(List<IntermediateCode> ir) {
        ArrayList<IntermediateCode> naiveIR = new ArrayList<IntermediateCode>();

        for (int i = 0; i < ir.size(); i ++) {
            int opcode = ir.get(i).getOpcode();
            String[] s = ir.get(i).getParams();

            if(opcode == IntermediateCode.ASSIGN) {
                //either setting x = # or x = y
                //so need to store x regardless and y is conditional load
                // s[0] is left side of =, s[1] is right
                if(!isLiteral(s[1])) { //not being set to a number
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
            } else if(opcode == IntermediateCode.CALL || opcode == IntermediateCode.CALLR) { //method call
                // omitting call and callr
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
                naiveIR.add(new FourAddressCode(IntermediateCode.ARRAY_STORE, "$t0|" + s[0], "$t1", "$t2"));
            } else if(opcode == IntermediateCode.ARRAY_LOAD) {
                // s[0] is destination var, s[1] is array var, s[2] is index
                if(!isLiteral(s[2])) { // s[2] is a var
                    naiveIR.add(new FourAddressCode(IntermediateCode.LDR, "$t1", s[2], ""));
                    naiveIR.add(new FourAddressCode(IntermediateCode.ARRAY_LOAD, "$t0", s[1], "$t1"));
                } else { // s[2] is a #
                    naiveIR.add(new FourAddressCode(IntermediateCode.ARRAY_LOAD, "$t0", s[1], s[2]));
                }
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

    public void cfgConstruction(IRGenerator input) {
        //LinkedList<IRGenerator> basicBlocks = new LinkedList<IRGenerator>();

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