import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RegisterAllocation {


    public IRGenerator naive(IRGenerator input) {
        int size = input.getSize();
        int nodesTraversed = 0;
        int nodesAdded = 0;

        while(nodesTraversed<size) {
            int opcode = input.getOpcode(nodesTraversed+nodesAdded);
            String[] s = input.getParams(nodesTraversed+nodesAdded);

            if(opcode == IntermediateCode.ASSIGN) {
                //either setting x = # or x = y
                //so need to store x regardless and y is conditional load
                input.changeParam(nodesTraversed+nodesAdded, 0, "$t0");
                if(!s[1].matches("^-?[0-9]*\\.?[0-9]+$")) { //not being set to a number
                    input.changeParam(nodesTraversed+nodesAdded, 1, "$t1");
                    input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t1", s[1]);
                    nodesAdded++;
                }
                input.emitAtIndex(nodesTraversed+nodesAdded+1, IntermediateCode.STR, "$t0", s[0]);
                nodesAdded++;
            } else if(opcode>=IntermediateCode.ADD && opcode<=IntermediateCode.OR) {
                // add, sub, mult, div, and, or
                //3 param is always a var(the destination), 1st 2 might be
                input.changeParam(nodesTraversed+nodesAdded, 2, "$t0");
                if(!s[0].matches("^-?[0-9]*\\.?[0-9]+$") && !s[1].matches("^-?[0-9]*\\.?[0-9]+$")) { //both vars
                    input.changeParam(nodesTraversed+nodesAdded, 0, "$t1");
                    input.changeParam(nodesTraversed+nodesAdded, 1, "$t2");
                    input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t1", s[0]);
                    nodesAdded++;
                    input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t2", s[1]);
                    nodesAdded++;
                } else if(s[0].matches("^-?[0-9]*\\.?[0-9]+$") && !s[1].matches("^-?[0-9]*\\.?[0-9]+$")) { //s[1] is a var
                    input.changeParam(nodesTraversed+nodesAdded, 1, "$t1");
                    input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t1", s[1]);
                    nodesAdded++;
                } else if(!s[0].matches("^-?[0-9]*\\.?[0-9]+$") && s[1].matches("^-?[0-9]*\\.?[0-9]+$")) { //s[0] is a var
                    input.changeParam(nodesTraversed+nodesAdded, 0, "$t1");
                    input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t1", s[0]);
                    nodesAdded++;
                }
                input.emitAtIndex(nodesTraversed+nodesAdded+1, IntermediateCode.STR, "$t0", s[2]);
                nodesAdded++;
            } else if(opcode >= IntermediateCode.BREQ && opcode <= IntermediateCode.BRGEQ) { //is a branch statement
                //have a label in 3rd param and 
                //1st 2 params can be numbers or vars
                if(!s[0].matches("^-?[0-9]*\\.?[0-9]+$") && !s[1].matches("^-?[0-9]*\\.?[0-9]+$")) {//both vars
                    input.changeParam(nodesTraversed+nodesAdded, 0, "$t0");
                    input.changeParam(nodesTraversed+nodesAdded, 1, "$t1");
                    input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t0", s[0]);
                    nodesAdded++;
                    input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t1", s[1]);
                    nodesAdded++;
                } else if(s[0].matches("^-?[0-9]*\\.?[0-9]+$") && !s[1].matches("^-?[0-9]*\\.?[0-9]+$")) { //s[1] is a var
                    input.changeParam(nodesTraversed+nodesAdded, 1, "$t0");
                    input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t0", s[1]);
                    nodesAdded++;
                } else if(!s[0].matches("^-?[0-9]*\\.?[0-9]+$") && s[1].matches("^-?[0-9]*\\.?[0-9]+$")) { //s[0] is a var
                    input.changeParam(nodesTraversed+nodesAdded, 0, "$t0");
                    input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t0", s[0]);
                    nodesAdded++;
                }
            } else if(opcode == IntermediateCode.RETURN) {
                //either empty params or only a param in the 1st (return value)
                if(s[0].length() > 0) { //param in the 1st
                    if(!s[0].matches("^-?[0-9]*\\.?[0-9]+$")) { //it is a variable
                        input.changeParam(nodesTraversed+nodesAdded, 0, "$t0");
                        input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t0", s[0]);
                        nodesAdded++;
                    }
                }
            } else if(opcode == IntermediateCode.CALL || opcode == IntermediateCode.CALLR) { //method call
                int numParams = s.length;
                int regsUsed = 0;
                String[] temp = s;
                for(int i = 0; i<numParams; i++) {
                    if(!s[i].matches("^-?[0-9]*\\.?[0-9]+$")) { //not a number
                        input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$a"+regsUsed, s[i]);
                        nodesAdded++;
                        input.changeParam(nodesTraversed+nodesAdded, i, "$a"+regsUsed);
                        regsUsed++;
                    }
                }
                String retAddress = input.getRetAddress(nodesTraversed+nodesAdded);
                if(opcode == IntermediateCode.CALLR && retAddress.length() > 0) {
                    input.changeRetAddress(nodesTraversed+nodesAdded, "$t"+regsUsed);
                    input.emitAtIndex(nodesTraversed+nodesAdded+1, IntermediateCode.STR, "$t"+regsUsed, retAddress);
                    nodesAdded++;
                }
            } else if(opcode == IntermediateCode.ARRAY_STORE) { 
                //arr[#]=b
                if(!s[2].matches("^-?[0-9]*\\.?[0-9]+$")) { //b is a variable
                    input.changeParam(nodesTraversed+nodesAdded, 2, "$t0");
                    input.emitAtIndex(nodesTraversed+nodesAdded, IntermediateCode.LDR, "$t0", s[2]);
                    nodesAdded++;
                }
                /*
                * not sure if anything else needs to be done here
                *
                */
            } else if(opcode == IntermediateCode.ARRAY_LOAD) {
                //a=arr[#]
                input.changeParam(nodesTraversed+nodesAdded, 0, "$t0");
                input.emitAtIndex(nodesTraversed+nodesAdded+1, IntermediateCode.STR, "$t0", s[0]);
                nodesAdded++;
            } else if(opcode == IntermediateCode.ARRAY_ASSIGN) {
                /*
                * Not sure what do to for array assignment
                *
                */
            } else if(opcode!=-1 && opcode!=7) {
                //opcode==7 is goto which doesn't need any loads/stores
                //opcode==-1 is a label which doesn't need any loads/stores
                System.out.println("Something went wrong! Opcode: "+opcode);
            }
            nodesTraversed++;
        }
        return input;
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