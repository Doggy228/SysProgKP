package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Stmt_Return extends Stmt {
    private Stmt_Proc proc;
    private Expr retValue;

    public Stmt_Return(Env env, int row, int col, Stmt_Proc proc, Expr retValue) {
        super(env, row, col);
        this.proc = proc;
        this.retValue = retValue;
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"return\"" + System.lineSeparator());
        buf.append(indent + "  {nameProc}" + System.lineSeparator());
        buf.append(indent + "    " + proc.getNameProc().getName() + System.lineSeparator());
        buf.append(indent + "  {retValue}" + System.lineSeparator());
        retValue.printTree(buf, indent + "    ");
    }

    @Override
    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        genComment(prg);
        prg.outWriteln("\t;Return from proc["+proc.getNameProc().getName()+"] begin");
        Expr retValue_new = retValue.reduce(prg);
        prg.outWriteln("\tmov eax," + retValue_new.outGetValue(prg, getEnv()));
        prg.outWriteln("\tmov ebx,[ebp+8]");
        prg.outWriteln("\tmov [ebx],eax");
        proc.getEnv().genFreeMem(prg, getEnv());
        prg.outWriteln("\tpop ebp");
        if (proc.getParamProc() == null) {
            prg.outWriteln("\tret 4");
        } else {
            prg.outWriteln("\tret 8");
        }
        prg.outWriteln("\t;Return from proc["+proc.getNameProc().getName()+"] end");
    }
}
