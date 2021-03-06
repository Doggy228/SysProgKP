package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class Stmt_Block extends Stmt {
    private Stmt_Seq body;

    public Stmt_Block(Env env, int row, int col){
        super(env, row, col);
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"block\""+System.lineSeparator());
        if(body==null){
            buf.append(indent+"  null"+System.lineSeparator());
        } else {
            body.printTree(buf, indent+"  ");
        }
    }

    @Override
    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        if(body==null) return;
        genComment(prg);
        getEnv().genAllocMem(prg);
        int labelBeginBlock = prg.newLabel();
        int labelAfterBlock = prg.newLabel();
        prg.outWriteLabel(labelBeginBlock);
        body.gen(prg, labelBeginBlock, labelAfterBlock);
        prg.outWriteLabel(labelAfterBlock);
        getEnv().genFreeMem(prg, getEnv());
    }

}

