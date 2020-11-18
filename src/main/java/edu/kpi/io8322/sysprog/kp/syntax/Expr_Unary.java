package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Expr_Unary extends Expr {
    private Expr value;

    public Expr_Unary(int row, int col, Expr value) {
        super(row, col);
        this.value = value;
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"unary\""+System.lineSeparator());
        buf.append(indent + "  {value}"+System.lineSeparator());
        value.printTree(buf, indent+"    ");
    }

    @Override
    public Expr gen(Program prg) throws CompileException, IOException {
        return value.reduce(prg);
    }

    @Override
    public Expr reduce(Program prg) throws CompileException, IOException {
        return gen(prg);
    }

}
