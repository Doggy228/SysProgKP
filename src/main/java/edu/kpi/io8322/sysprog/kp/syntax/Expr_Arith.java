package edu.kpi.io8322.sysprog.kp.syntax;

import lombok.Getter;

@Getter
public abstract class Expr_Arith extends Expr {
    private Expr value1;
    private Expr value2;
    private Expr_IdTemp result;

    public Expr_Arith(Env env, int row, int col, Expr value1, Expr value2, Expr_IdTemp result) {
        super(env, row, col);
        this.value1 = value1;
        this.value2 = value2;
        this.result = result;
    }

    public Expr_Arith(Env env, int row, int col, Expr value1, Expr value2) {
        this(env, row, col, value1, value2, env.newTemp(row, col));
    }
}