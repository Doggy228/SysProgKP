package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Stmt_If extends Stmt {
    private Expr_Bool cond;
    private Stmt_Block stmtTrue;

    public Stmt_If(Env env,int row, int col, Expr_Bool cond, Stmt_Block stmtTrue) {
        super(env, row, col);
        this.cond = cond;
        this.stmtTrue = stmtTrue;
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"if\"" + System.lineSeparator());
        buf.append(indent + "  {cond}" + System.lineSeparator());
        cond.printTree(buf, indent + "    ");
        buf.append(indent + "  {stmtTrue}" + System.lineSeparator());
        stmtTrue.printTree(buf, indent + "    ");
    }

    @Override
    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        genComment(prg);
        prg.outWriteln("\t;IF Statement begin");
        Expr_IdTemp cond_new = (Expr_IdTemp)cond.reduce(prg);
        cond_new.outOffsetToEbx(prg, getEnv());
        prg.outWriteln("\tcmp dword ptr [ebx],0");
        int labelAfterTrue = prg.newLabel();
        prg.outWriteln("\tje "+prg.strLabel(labelAfterTrue));
        prg.outWriteln("\t;IF True");
        stmtTrue.gen(prg, labelBegin, labelAfter);
        prg.outWriteLabel(labelAfterTrue);
        prg.outWriteln("\t;IF Statement end");
    }
}