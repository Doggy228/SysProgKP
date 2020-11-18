package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;

import java.io.IOException;

public abstract class Expr extends Node {
    public Expr(int row, int col){
        super(row, col);
    }

    public Expr gen(Program prg) throws CompileException, IOException {
        return this;
    }

    public Expr reduce(Program prg) throws CompileException, IOException {
        return this;
    }

    public String outGetValue(Program prg) throws CompileException, IOException {
        return null;
    }
}
