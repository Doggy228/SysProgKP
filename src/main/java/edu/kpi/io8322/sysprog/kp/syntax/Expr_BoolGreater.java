package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;

import java.io.IOException;

public class Expr_BoolGreater extends Expr_Bool {
    public Expr_BoolGreater(int row, int col, Expr expr1, Expr expr2, Expr_IdTemp result) {
        super(row, col, expr1, expr2, result);
    }

    public Expr_BoolGreater(int row, int col, Expr expr1, Expr expr2, Env env) {
        super(row, col, expr1, expr2, env);
    }

    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\">\"" + System.lineSeparator());
        buf.append(indent + "  {expr1}" + System.lineSeparator());
        getExpr1().printTree(buf, indent + "    ");
        buf.append(indent + "  {expr2}" + System.lineSeparator());
        getExpr2().printTree(buf, indent + "    ");
    }

    @Override
    public Expr gen(Program prg) throws CompileException, IOException {
        Expr expr1r = getExpr1().reduce(prg);
        Expr expr2r = getExpr2().reduce(prg);
        return new Expr_BoolGreater(getRow(), getCol(), expr1r, expr2r, getResult());
    }

    @Override
    public Expr reduce(Program prg) throws CompileException, IOException {
        prg.outWriteln("\t;BoolGreater start");
        Expr_BoolGreater expr = (Expr_BoolGreater) gen(prg);
        String expr1str = expr.getExpr1().outGetValue(prg);
        prg.outWriteln("\tmov eax," + expr1str);
        String expr2str = expr.getExpr2().outGetValue(prg);
        prg.outWriteln("\tcmp eax," + expr2str);
        int labelTrue = prg.newLabel();
        prg.outWriteln("\tjg "+prg.strLabel(labelTrue));
        expr.getResult().outOffsetToEbx(prg);
        prg.outWriteln("\tmov dword ptr [ebx],0");
        int labelEnd = prg.newLabel();
        prg.outWriteln("\tjmp "+prg.strLabel(labelEnd));
        prg.outWriteLabel(labelTrue);
        expr.getResult().outOffsetToEbx(prg);
        prg.outWriteln("\tmov dword ptr [ebx],1");
        prg.outWriteLabel(labelEnd);
        prg.outWriteln("\t;BoolGreater end");
        return expr.getResult();
    }
}
