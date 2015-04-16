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
                //so need to load x regardless and y is conditional
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
                //3 param is always a var, 1st 2 might be
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
                //have a label in 3rd param and 1st 2 params
                //can be numbers or vars
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
                //either empty params or only a param in the 1st
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

    }

    public void ebbConstruction(IRGenerator input) {
        
    }
}