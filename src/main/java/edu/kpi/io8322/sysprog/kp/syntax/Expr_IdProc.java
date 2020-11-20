package edu.kpi.io8322.sysprog.kp.syntax;

public class Expr_IdProc extends Expr_Id {
    public Expr_IdProc(Env env, int row, int col, String name){
        super(env, row, col, name);
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"idproc\""+System.lineSeparator());
        buf.append(indent + "  "+ getName()+System.lineSeparator());
    }

}
