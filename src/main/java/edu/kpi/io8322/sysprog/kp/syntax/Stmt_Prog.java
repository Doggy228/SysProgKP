package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Stmt_Prog extends Stmt_Block {
    public List<Stmt_Proc> procList;

    public Stmt_Prog(Env env) {
        super(env,1, 0);
        procList = new ArrayList<>();
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"prog\""+System.lineSeparator());
        if(!procList.isEmpty()){
            for(Stmt_Proc el: procList){
                el.printTree(buf, indent+"  ");
            }
        }
        if(getBody()==null){
            buf.append(indent+"  \"body\""+System.lineSeparator());
            buf.append(indent+"    null"+System.lineSeparator());
        } else {
            buf.append(indent+"  \"body\""+System.lineSeparator());
            getBody().printTree(buf, indent+"    ");
        }
    }

    @Override
    public void gen(Program prg, int labelBegin, int labelAfter) throws CompileException, IOException {
        prg.outWriteln(".586");
        prg.outWriteln(".model flat, stdcall");
        prg.outWriteln("option casemap :none");
        prg.outWriteln("include kernel32.inc");
        prg.outWriteln("include user32.inc");
        prg.outWriteln("includelib kernel32.lib");
        prg.outWriteln("includelib user32.lib");
        prg.outWriteln(".data");
        prg.outWriteln("\tCaption db \"Message\", 0");
        prg.outWriteln("\tTextDword db 20 dup(0)");
        prg.outWriteln("\tMemBuf dd 16384 dup(?)");
        prg.outWriteln("\tMemSp dd 0");
        prg.outWriteln("\tEnvBuf dd 1024 dup(?)");
        prg.outWriteln("\tEnvSp dd 0");
        prg.outWriteln(".code");
        for(Stmt_Proc el: procList){
            el.gen(prg, 0, 0);
        }
        genComment(prg);
        prg.outWriteln("main:");
        getEnv().genAllocMem(prg);
        labelBegin = prg.newLabel();
        labelAfter = prg.newLabel();
        prg.outWriteLabel(labelBegin);
        getBody().gen(prg, labelBegin, labelAfter);
        prg.outWriteLabel(labelAfter);
        getEnv().genFreeMem(prg, getEnv());
        prg.outWriteln("end main");
    }
}
