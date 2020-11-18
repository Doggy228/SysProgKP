package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public abstract class Node {
    private int row;
    private int col;

    public Node(int row, int col){
        this.row = row;
        this.col = col;
    }

    public abstract void printTree(StringBuilder buf, String indent);
}
