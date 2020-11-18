package edu.kpi.io8322.sysprog.kp.syntax;

import lombok.Getter;

@Getter
public abstract class Expr_Id extends Expr {
    private String name;

    public Expr_Id(int row, int col, String name){
        super(row, col);
        this.name = name;
    }
}
