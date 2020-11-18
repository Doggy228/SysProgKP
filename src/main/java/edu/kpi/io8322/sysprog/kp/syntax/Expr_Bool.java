package edu.kpi.io8322.sysprog.kp.syntax;

import lombok.Getter;

@Getter
public abstract class Expr_Bool extends Expr {
    private Expr expr1;
    private Expr expr2;
    private Expr_IdTemp result;

    public Expr_Bool(int row, int col, Expr expr1, Expr expr2, Expr_IdTemp result) {
        super(row, col);
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.result = result;
    }
    public Expr_Bool(int row, int col, Expr expr1, Expr expr2, Env env) {
        this(row, col, expr1, expr2, env.newTemp(row, col));
    }
}
