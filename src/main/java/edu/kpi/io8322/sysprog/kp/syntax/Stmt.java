package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
public abstract class Stmt extends Node {
    private List<String> listComment;

    public Stmt(Env env, int row, int col){
        super(env, row, col);
    }

    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
    }

    public void genComment(Program prg) throws CompileException, IOException {
        if(listComment!=null && !listComment.isEmpty()){
            for(String el: listComment){
                prg.outWriteln("\t;COMMENT: "+el);
            }
        }
    }
}
