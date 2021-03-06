package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;

import java.io.IOException;

public class Expr_ArithPlus extends Expr_Arith {
    public Expr_ArithPlus(Env env, int row, int col, Expr value1, Expr value2) {
        super(env, row, col, value1, value2);
    }

    public Expr_ArithPlus(Env env, int row, int col, Expr value1, Expr value2, Expr_IdTemp result) {
        super(env, row, col, value1, value2, result);
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"+\""+System.lineSeparator());
        buf.append(indent + "  {value1}"+System.lineSeparator());
        getValue1().printTree(buf, indent + "    ");
        buf.append(indent + "  {value2}"+System.lineSeparator());
        getValue2().printTree(buf, indent + "    ");
    }


    @Override
    public Expr gen(Program prg) throws CompileException, IOException {
        Expr value1r = getValue1().reduce(prg);
        Expr value2r = getValue2().reduce(prg);
        return new Expr_ArithPlus(getEnv(), getRow(), getCol(), value1r, value2r, getResult());
    }

    @Override
    public Expr reduce(Program prg) throws CompileException, IOException {
        prg.outWriteln("\t;ArithPlus begin");
        Expr_ArithPlus expr = (Expr_ArithPlus)gen(prg);
        String value1str = expr.getValue1().outGetValue(prg, getEnv());
        prg.outWriteln("\tmov eax,"+value1str);
        String value2str = expr.getValue2().outGetValue(prg, getEnv());
        prg.outWriteln("\tadd eax,"+value2str);
        expr.getResult().outOffsetToEbx(prg, getEnv());
        prg.outWriteln("\tmov [ebx],eax");
        prg.outWriteln("\t;ArithPlus end");
        return expr.getResult();
    }

}
