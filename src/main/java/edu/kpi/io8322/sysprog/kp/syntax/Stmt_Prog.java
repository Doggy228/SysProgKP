package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;

import java.io.IOException;

public class Stmt_Prog extends Stmt_Block {
    public Stmt_Prog() {
        super(1, 0);
    }

    @Override
    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        prg.outWriteln(".586");
        prg.outWriteln(".model flat stdcall");
        prg.outWriteln("option casemap :none");
        prg.outWriteln(".data");
        prg.outWriteln("MemBuf dd 16384 dup(?)");
        prg.outWriteln("MemSp dd 0");
        prg.outWriteln(".code");
        prg.outWriteln("main:");
        labelBegin = prg.newLabel();
        labelAfter = prg.newLabel();
        prg.outWriteLabel(labelBegin);
        getBody().gen(prg, labelBegin, labelAfter);
        prg.outWriteLabel(labelAfter);
        prg.outWriteln("end main");
        System.err.println("Step2");
    }
}
