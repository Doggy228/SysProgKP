package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Stmt_Proc extends Stmt {
    private Expr_IdProc nameProc;
    private Expr_IdVar paramProc;
    private Env env;
    private Stmt body;

    public Stmt_Proc(int row, int col, Expr_IdProc nameProc){
        super(row, col);
        this.nameProc = nameProc;
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"proc\""+System.lineSeparator());
        buf.append(indent + "  {nameProc}"+System.lineSeparator());
        buf.append(indent + "    "+nameProc.getName()+System.lineSeparator());
        buf.append(indent + "  {paramProc}"+System.lineSeparator());
        if(paramProc==null){
            buf.append(indent + "    null"+System.lineSeparator());
        } else {
            buf.append(indent + "    "+paramProc.getName()+System.lineSeparator());
        }
        buf.append(indent + "  {bodyProc}"+System.lineSeparator());
        if(body==null){
            buf.append(indent + "    null"+System.lineSeparator());
        } else {
            body.printTree(buf, indent + "    ");
        }
    }

    @Override
    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        prg.outWriteln(";User defined proc["+nameProc.getName()+"]");
        prg.outWriteln("MY_"+nameProc.getName()+" proc");
        prg.outWriteln("\tpush ebp");
        prg.outWriteln("\tmov ebp,esp");
        env.genAllocMem(prg);
        if(paramProc!=null){
            prg.outWriteln("\t;Set param["+paramProc.getName()+"] value");
            prg.outWriteln("\tmov eax,[ebp+12]");
            paramProc.outOffsetToEbx(prg);
            prg.outWriteln("\tmov [ebx],eax");
        }
        if(body!=null){
            prg.outWriteln("\t;Body proc start");
            body.gen(prg, labelBegin, labelAfter);
        }
        prg.outWriteln("MY_"+nameProc.getName()+" endp");
    }
}
