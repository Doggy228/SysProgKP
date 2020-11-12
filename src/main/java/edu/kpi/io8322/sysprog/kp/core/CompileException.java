package edu.kpi.io8322.sysprog.kp.core;

import edu.kpi.io8322.sysprog.kp.PythonCompiler;
import lombok.Getter;

@Getter
public class CompileException extends Exception {
    private int row;
    private int col;

    public CompileException(int row, int col, String message, Throwable cause) {
        super(message, cause);
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return PythonCompiler.app.getSrcname() +
                "[" + row + "," + col + "]->[" + PythonCompiler.app.getSrclines().get(row - 1) + "]" + System.lineSeparator() + String.format("%1$50s %2$s", "ERR: ", getMessage());
    }
}
