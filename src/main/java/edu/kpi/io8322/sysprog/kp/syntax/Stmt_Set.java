package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Stmt_Set extends Stmt {
    private Expr_IdVar varName;
    private Expr varValue;

    public Stmt_Set(int row, int col, Expr_IdVar varName, Expr varValue){
        super(row, col);
        this.varName = varName;
        this.varValue = varValue;
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"Set\""+System.lineSeparator());
        buf.append(indent + "  {nameVar}"+System.lineSeparator());
        if(varName==null){
            buf.append(indent + "    null"+System.lineSeparator());
        } else {
            buf.append(indent + "     "+varName.getName()+System.lineSeparator());
        }
        buf.append(indent + "  {varValue}"+System.lineSeparator());
        if(varValue==null){
            buf.append(indent + "    null"+System.lineSeparator());
        } else {
            varValue.printTree(buf, indent+"    ");
        }
    }

    @Override
    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        if(varValue!=null) {
            Expr varValue_new = varValue.reduce(prg);
            if(varName!=null) {
                prg.outWriteln("\t;Set var["+varName.getName()+"] begin");
                String valuestr = varValue_new.outGetValue(prg);
                prg.outWriteln("\tmov eax,"+valuestr);
                varName.outOffsetToEbx(prg);
                prg.outWriteln("\tmov [ebx],eax");
            }
        }
    }
}
