package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;

import java.io.IOException;

public class Expr_GetVarValue extends Expr {
    private Expr_IdVar nameVar;

    public Expr_GetVarValue(int row, int col, Expr_IdVar nameVar){
        super(row, col);
        this.nameVar = nameVar;
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"getvarvalue\""+System.lineSeparator());
        buf.append(indent + "  {varname}"+System.lineSeparator());
        buf.append(indent + "    "+nameVar.getName()+System.lineSeparator());
    }

    @Override
    public Expr gen(Program prg) throws CompileException, IOException {
        return nameVar.reduce(prg);
    }

    @Override
    public Expr reduce(Program prg) throws CompileException, IOException {
        return gen(prg);
    }
}
