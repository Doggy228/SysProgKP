package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;

import java.io.IOException;

@Getter
public class Expr_Call extends Expr {
    private Stmt_Proc proc;
    private Expr exprParam;
    private Expr_IdTemp result;

    public Expr_Call(Env env, int row, int col, Stmt_Proc proc, Expr exprParam, Expr_IdTemp result){
        super(env, row, col);
        this.proc = proc;
        this.exprParam = exprParam;
        this.result = result;
    }

    public Expr_Call(Env env, int row, int col, Stmt_Proc proc, Expr exprParam){
        this(env, row, col, proc, exprParam, env.newTemp(row, col));
    }

    @Override
    public void printTree(StringBuilder buf, String indent) {
        buf.append(indent + "\"call\""+System.lineSeparator());
        buf.append(indent + "  {procname}"+System.lineSeparator());
        buf.append(indent + "    "+proc.getNameProc().getName()+System.lineSeparator());
        buf.append(indent + "  {param}"+System.lineSeparator());
        if(exprParam==null){
            buf.append(indent + "    null"+System.lineSeparator());
        } else {
            exprParam.printTree(buf, indent + "    ");
        }
    }

    @Override
    public Expr gen(Program prg) throws CompileException, IOException {
        if(exprParam!=null){
            Expr exprParam_new = exprParam.reduce(prg);
            return new Expr_Call(getEnv(), getRow(), getCol(), proc, exprParam_new, result);
        }
        return this;
    }

    @Override
    public Expr reduce(Program prg) throws CompileException, IOException {
        prg.outWriteln("\t;Call proc["+getProc().getNameProc().getName()+"] begin");
        Expr_Call expr = (Expr_Call)gen(prg);
        if(expr.getExprParam()!=null){
            prg.outWriteln("\tpush "+expr.getExprParam().outGetValue(prg, getEnv()));
        }
        expr.getResult().outOffsetToEbx(prg, getEnv());
        prg.outWriteln("\tpush ebx");
        prg.outWriteln("\tcall MY_"+proc.getNameProc().getName());
        prg.outWriteln("\t;Call proc["+getProc().getNameProc().getName()+"] end");
        return expr.getResult();
    }
}
