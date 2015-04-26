import java.util.ArrayList;

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
        ArrayList<Block> arr = new ArrayList<Block>();
        boolean prevBranchOrReturn = false;
        int called = 0;

        //determining all the basic blocks
        for(int i = 0; i<size; i++) {
            opcode = input.getOpcode(i);

            if(i!=0 && (prevBranchOrReturn || opcode==-1)) { //new leader statement
                //creates a new basic block
                prevBranchOrReturn = false;
                arr.add(new Block(startBlock, i-1));

                startBlock = i;
            }

            //reached the end of the intermediate code
            if(i+1==size) {
                arr.add(new Block(startBlock, i));
            }

            if(opcode >= IntermediateCode.GOTO && opcode <= IntermediateCode.RETURN) {
                //statements after goto, branches and returns are the beginning
                //of a basic block
                called++;
                prevBranchOrReturn = true;
            }
        }

        System.out.println("Called: "+called);
        for(int i = 0; i<arr.size(); i++) {
            System.out.println("Block " + i + ":");
            System.out.println("    Start: "+ arr.get(i).getStart());
            System.out.println("    End: " + arr.get(i).getEnd());
            for(int j = arr.get(i).getStart(); j<=arr.get(i).getEnd(); j++) {
                System.out.println("        "+input.toString(j));
            }
        }

        //return input;
    }

    public void ebbConstruction(IRGenerator input) {
        
    }

/*
    private class BasicBlock {
        private IRGenerator block;
        private ArrayList<BasicBlock> nextBlocks;
        private ArrayList<BasicBlock> prevBlocks;

        public BasicBlock(IRGenerator block) {
            this.block = block;
            nextBlocks = new ArrayList<BasicBlock>();
            prevBlocks = new ArrayList<BasicBlock>();
        }

        public IRGenerator getBlockCode() {
            return block;
        }

        public void addNext(BasicBlock next) {
            nextBlocks.add(next);
        }

        public void addPrev(BasicBlock prev) {
            prevBlocks.add(prev);
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
    }*/

    private class Block {
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
    }
}