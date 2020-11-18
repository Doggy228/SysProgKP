package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Stmt_Seq extends Stmt {
    private Stmt stmt1;
    private Stmt stmt2;

    public Stmt_Seq(int row, int col, Stmt stmt1, Stmt stmt2){
        super(row, col);
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"Seq\""+System.lineSeparator());
        buf.append(indent + "  {stmt1}"+System.lineSeparator());
        if(stmt1==null){
            buf.append(indent + "    null"+System.lineSeparator());
        } else {
            stmt1.printTree(buf, indent+"    ");
        }
        buf.append(indent + "  {stmt2}"+System.lineSeparator());
        if(stmt2==null){
            buf.append(indent + "    null"+System.lineSeparator());
        } else {
            stmt2.printTree(buf, indent+"    ");
        }
    }

    @Override
    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        if(stmt1!=null) stmt1.gen(prg, labelBegin, labelAfter);
        if(stmt2!=null) stmt2.gen(prg, labelBegin, labelAfter);
    }
}
