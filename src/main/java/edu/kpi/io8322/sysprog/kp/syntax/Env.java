package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Env {
    private List<Expr_IdVar> varList;
    private List<Expr_IdTemp> tmpList;
    protected int memOffsetPos;
    protected Env prev;

    public Env(Env prev) {
        varList = new ArrayList<>();
        tmpList = new ArrayList<>();
        memOffsetPos = 0;
        this.prev = prev;
    }

    public Env getPrev() {
        return prev;
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
        Expr_IdTemp node = new Expr_IdTemp(row, col, "_tmp"+(tmpList.size()+1));
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
            prg.outWriteln("\t;Init Var tables.");
            prg.outWriteln("\tadd MemSp," + getMemBlockSize());
        }
    }

    public void genFreeMem(Program prg) throws CompileException, IOException {
        if(getMemBlockSize()>0) {
            prg.outWriteln("\t;Destroy Var tables.");
            prg.outWriteln("\tsub MemSp," + getMemBlockSize());
        }
    }
}
