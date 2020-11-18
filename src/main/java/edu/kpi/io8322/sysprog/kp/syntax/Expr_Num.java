package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Expr_Num extends Expr {
    private int value;

    public Expr_Num(int row, int col, int value) {
        super(row, col);
        this.value = value;
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"" + value + "\"" + System.lineSeparator());
    }

    @Override
    public Expr gen(Program prg) throws CompileException, IOException {
        return this;
    }

    @Override
    public Expr reduce(Program prg) throws CompileException, IOException {
        return this;
    }

    @Override
    public String outGetValue(Program prg) throws CompileException, IOException {
        return value+" ;Constant";
    }

}
