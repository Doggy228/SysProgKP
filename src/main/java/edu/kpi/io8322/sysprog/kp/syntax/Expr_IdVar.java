package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Expr_IdVar extends Expr_Id {
    private int memOffset;

    public Expr_IdVar(int row, int col, String name){
        super(row, col, name);
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"idvar\""+System.lineSeparator());
        buf.append(indent + "  "+ getName()+System.lineSeparator());
    }

    @Override
    public String outGetValue(Program prg) throws CompileException, IOException {
        outOffsetToEbx(prg);
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

    public void outOffsetToEbx(Program prg) throws CompileException, IOException {
        prg.outWriteln("\t;Access var["+getName()+"]");
        prg.outWriteln("\tmov ebx,[MemBuf]");
        prg.outWriteln("\tadd ebx,MemSp");
        prg.outWriteln("\tsub ebx,"+(memOffset+4));
    }
}
