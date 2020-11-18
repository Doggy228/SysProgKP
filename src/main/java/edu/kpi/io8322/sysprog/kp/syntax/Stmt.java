package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;

import java.io.IOException;

public abstract class Stmt extends Node {
    public Stmt(int row, int col){
        super(row, col);
    }

    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
    }
}
