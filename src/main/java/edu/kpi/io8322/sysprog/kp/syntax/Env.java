package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Env {
    private static int SEQ_ID = 1;
    private List<Expr_IdVar> varList;
    private List<Expr_IdTemp> tmpList;
    protected int memOffsetPos;
    protected Env prev;
    private int id;
    private int level;

    public Env(Env prev) {
        this.id = SEQ_ID;
        SEQ_ID++;
        memOffsetPos = 0;
        this.prev = prev;
        if(prev==null){
            level = 1;
        } else {
            level = prev.getLevel()+1;
        }
        varList = new ArrayList<>();
        tmpList = new ArrayList<>();
    }

    public Env getPrev() {
        return prev;
    }

    public int getId(){
        return id;
    }

    public int getLevel(){
        return level;
    }

    public int calcSubLevel(Env env) throws CompileException {
        if(env.getId()==id) return 0;
        if(prev==null) throw new CompileException(1,0,"Error calc sublevel env.", null);
        return prev.calcSubLevel(env)+1;
    }

    public void putVar(Expr_IdVar node) {
        varList.add(node);
        node.setMemOffset(memOffsetPos);
        memOffsetPos += 4;
    }

    public Expr_IdVar getVar(String key) {
        for(Expr_IdVar node: varList){
            if(node.getName().equals(key)) return node;
        }
        if (prev != null) return prev.getVar(key);
        return null;
    }

    public Expr_IdTemp newTemp(int row, int col) {
        Expr_IdTemp node = new Expr_IdTemp(this, row, col, "_tmp"+(tmpList.size()+1));
        node.setMemOffset(memOffsetPos);
        memOffsetPos += 4;
        tmpList.add(node);
        return node;
    }

    public int getMemBlockSize() {
        return memOffsetPos;
    }

    public void genAllocMem(Program prg) throws CompileException, IOException {
        if(getMemBlockSize()>0) {
            prg.outWriteln("\t;Save prev & init new env VarTable.");
            if(prev!=null) {
                prg.outWriteln("\tmov ebx,offset[EnvBuf]");
                prg.outWriteln("\tadd ebx,EnvSp");
                prg.outWriteln("\tmov eax,MemSp");
                prg.outWriteln("\tmov [ebx],eax");
                prg.outWriteln("\tadd EnvSp,4");
            }
            prg.outWriteln("\tadd MemSp," + getMemBlockSize());
        }
    }

    public void genFreeMem(Program prg, Env envCur) throws CompileException, IOException {
        int envSubLevel = envCur.calcSubLevel(this);
        if(envSubLevel>0){
            prg.outWriteln("\t;Destroy tree env VarTable & restore prev.");
            prg.outWriteln("\tmov ebx,offset[EnvBuf]");
            prg.outWriteln("\tadd ebx,EnvSp");
            prg.outWriteln("\tsub ebx,"+(4*(envSubLevel+1)));
            prg.outWriteln("\tpush eax");
            prg.outWriteln("\tmov eax,[ebx]");
            prg.outWriteln("\tmov MemSp,eax");
            prg.outWriteln("\tpop eax");
            if(prev!=null) {
                prg.outWriteln("\tsub EnvSp," + (4 * (envSubLevel + 1)));
            } else {
                prg.outWriteln("\tmov EnvSp,0");
            }
        } else {
            if (getMemBlockSize() > 0) {
                prg.outWriteln("\t;Destroy env VarTable & restore prev.");
                if(prev!=null) prg.outWriteln("\tsub EnvSp,4");
                prg.outWriteln("\tsub MemSp," + getMemBlockSize());
            }
        }
    }
}
