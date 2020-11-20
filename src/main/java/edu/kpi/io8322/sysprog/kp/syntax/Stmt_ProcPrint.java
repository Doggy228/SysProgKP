package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;

import java.io.IOException;

public class Stmt_ProcPrint extends Stmt_Proc {
    public Stmt_ProcPrint(Env env){
        super(env,1, 0, new Expr_IdProc(env, 1, 0, "print"));
    }

    @Override
    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        prg.outWriteln(";Standart proc[print]");
        prg.outWriteln("MY_"+getNameProc().getName()+" proc");
        prg.outWriteln("\tpush ebp");
        prg.outWriteln("\tmov ebp,esp");
        prg.outWriteln("\tmov eax,[ebp+12]");
        prg.outWriteln("\tmov edx,0");
        prg.outWriteln("\tpush dx");
        prg.outWriteln("\tmov cx,1");
        prg.outWriteln("\tmov ebx,10");
        prg.outWriteln("LL1:");
        prg.outWriteln("\tmov edx,0");
        prg.outWriteln("\tdiv ebx");
        prg.outWriteln("\tadd dx,'0'");
        prg.outWriteln("\tpush dx");
        prg.outWriteln("\tinc cx");
        prg.outWriteln("\tcmp eax,0");
        prg.outWriteln("\tjne LL1");
        prg.outWriteln("\tmov ebx,offset[TextDword]");
        prg.outWriteln("LL2:");
        prg.outWriteln("\tpop ax");
        prg.outWriteln("\tmov [ebx],al");
        prg.outWriteln("\tinc ebx");
        prg.outWriteln("\tdec cx");
        prg.outWriteln("\tcmp cx,0");
        prg.outWriteln("\tjne LL2");
        prg.outWriteln("\tinvoke MessageBoxA, 0, ADDR TextDword, ADDR Caption, 0");
        prg.outWriteln("\tmov dword ptr [ebp+8],0");
        prg.outWriteln("\tpop ebp");
        prg.outWriteln("\tret 8");
        prg.outWriteln("MY_"+getNameProc().getName()+" endp");
    }
}
