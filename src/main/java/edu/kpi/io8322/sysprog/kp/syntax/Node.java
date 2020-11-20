package edu.kpi.io8322.sysprog.kp.syntax;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Node {
    private Env env;
    private int row;
    private int col;

    public Node(Env env, int row, int col){
        this.env = env;
        this.row = row;
        this.col = col;
    }

    public abstract void printTree(StringBuilder buf, String indent);
}
