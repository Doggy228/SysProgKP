package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;

import java.io.IOException;

public class Stmt_ProcPrint extends Stmt_Proc {
    public Stmt_ProcPrint(){
        super(1, 0, new Expr_IdProc(1, 0, "print"));
    }

    @Override
    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        prg.outWriteln(";Standart proc[print]");
        prg.outWriteln("MY_"+getNameProc().getName()+" proc");
        prg.outWriteln("\tpush ebp");
        prg.outWriteln("\tmov ebp,esp");
        prg.outWriteln("\tmov eax,[ebp+12]");
        prg.outWriteln("\tinvoke MessageBoxA, 0, eax, ADDR Caption, 0");
        prg.outWriteln("\tmov [ebp+8],0");
        prg.outWriteln("\tpop ebp");
        prg.outWriteln("\tret 8");
        prg.outWriteln("MY_"+getNameProc().getName()+" endp");
    }
}
