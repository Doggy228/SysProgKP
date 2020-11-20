package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Expr_IdVar extends Expr_Id {
    private int memOffset;

    public Expr_IdVar(Env env, int row, int col, String name){
        super(env, row, col, name);
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"idvar\""+System.lineSeparator());
        buf.append(indent + "  "+ getName()+System.lineSeparator());
    }

    @Override
    public String outGetValue(Program prg, Env envCur) throws CompileException, IOException {
        outOffsetToEbx(prg, envCur);
        return "[ebx]";
    }

    @Override
    public Expr gen(Program prg) throws CompileException, IOException {
        return this;
    }

    @Override
    public Expr reduce(Program prg) throws CompileException, IOException {
        return this;
    }

    public void outOffsetToEbx(Program prg, Env envCur) throws CompileException, IOException {
        prg.outWriteln("\t;Access var["+getName()+"]");
        prg.outWriteln("\tmov ebx,offset[MemBuf]");
        int envSubLevel = envCur.calcSubLevel(getEnv());
        if(envSubLevel==0) {
            prg.outWriteln("\tadd ebx,MemSp");
        } else {
            prg.outWriteln("\tpush eax");
            prg.outWriteln("\tmov eax,offset[EnvBuf]");
            prg.outWriteln("\tadd eax,EnvSp");
            prg.outWriteln("\tsub eax,"+(4*envSubLevel));
            prg.outWriteln("\tadd ebx,[eax]");
            prg.outWriteln("\tpop eax");
        }
        prg.outWriteln("\tsub ebx,"+(memOffset+4));
    }
}
