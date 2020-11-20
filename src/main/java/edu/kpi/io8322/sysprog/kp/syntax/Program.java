package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import edu.kpi.io8322.sysprog.kp.lexical.LexTypeEnum;
import edu.kpi.io8322.sysprog.kp.lexical.Token;
import lombok.Getter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Getter
public class Program {
    private SyntaxAnalyzer syntaxAnalyzer;
    private List<Stmt_Proc> procList;
    private int labelIndex;
    private int tokenIndexCur;
    private Stmt_Prog root;
    private Env env;
    private Deque<Stmt_Proc> stackProc;
    private Writer out;
    private int labelIndexCur;


    public Program(SyntaxAnalyzer syntaxAnalyzer) {
        this.syntaxAnalyzer = syntaxAnalyzer;
    }

    public Env newEnv() {
        env = new Env(env);
        return env;
    }

    public Env restorePrevEnv() {
        env = env.getPrev();
        return env;
    }

    public void putProc(Stmt_Proc node) {
        procList.add(node);
        root.getProcList().add(node);
    }

    public Stmt_Proc getProc(String key) {
        for (Stmt_Proc el : procList) {
            if (el.getNameProc().getName().equals(key)) return el;
        }
        return null;
    }

    public void execSyntax() throws CompileException {
        procList = new ArrayList<>();
        stackProc = new ArrayDeque<>();
        labelIndex = 1;
        tokenIndexCur = 0;
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKB)
            throw new CompileException(tokenCur(), "Not start program.", null);
        root = new Stmt_Prog();
        root.setEnv(newEnv());
        tokenNext();
        Stmt_ProcPrint procPrint = new Stmt_ProcPrint();
        procPrint.setEnv(newEnv());
        Expr_IdVar paramPrint = new Expr_IdVar(1, 0, "printValue");
        procPrint.setParamProc(paramPrint);
        getEnv().putVar(paramPrint);
        putProc(procPrint);
        restorePrevEnv();
        Stmt_Seq bodyProg = new Stmt_Seq(tokenCur().getRow(), tokenCur().getCol(), null, parseStmtSeq());
        root.setBody(bodyProg);
        restorePrevEnv();
        tokenNext();
    }

    public int newLabel() {
        labelIndexCur++;
        return labelIndexCur;
    }

    public String strLabel(int num) {
        if(num>0) return "L"+num;
        return null;
    }

    public void outWriteLabel(int num) throws IOException {
        if (num > 0)
            out.write("L" + num + ":" + System.lineSeparator());
    }

    public void outWriteln(String str) throws IOException {
        if (str != null && !str.isEmpty()) out.write(str);
        out.write(System.lineSeparator());
    }

    public void execOut(Writer out) throws CompileException, IOException {
        this.out = out;
        labelIndexCur = 0;
        root.gen(this, 0, 0);
    }

    public Token tokenCur() {
        return syntaxAnalyzer.getTokenList().get(tokenIndexCur);
    }

    public Token tokenPeek(int step) {
        if (tokenIndexCur + step < syntaxAnalyzer.getTokenList().size())
            return syntaxAnalyzer.getTokenList().get(tokenIndexCur + step);
        return null;
    }

    public Token tokenNext() {
        tokenIndexCur++;
        if (tokenIndexCur < syntaxAnalyzer.getTokenList().size())
            return syntaxAnalyzer.getTokenList().get(tokenIndexCur);
        return null;
    }

    public Stmt_Block parseBlock() throws CompileException {
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKB)
            throw new CompileException(tokenCur(), "Not start block.", null);
        Stmt_Block stmtBlock = new Stmt_Block(tokenCur().getRow(), tokenCur().getCol());
        stmtBlock.setEnv(newEnv());
        tokenNext();
        stmtBlock.setBody(parseStmtSeq());
        restorePrevEnv();
        tokenNext();
        return stmtBlock;
    }

    public Stmt_Seq parseStmtSeq() throws CompileException {
        if (tokenCur().getLexType().getType() == LexTypeEnum.BLOCKE) return null;
        return new Stmt_Seq(tokenCur().getRow(), tokenCur().getCol(), parseStmt(), parseStmtSeq());
    }

    public Stmt parseStmt() throws CompileException {
        if (tokenCur().getLexType().getType() == LexTypeEnum.DEF) {
            Token token_ProcDef = tokenCur();
            Token token_ProcName = tokenNext();
            if (token_ProcName.getLexType().getType() != LexTypeEnum.ID)
                throw new CompileException(tokenCur(), "Bad procedure name.", null);
            if (getProc(token_ProcName.getValue()) != null) {
                throw new CompileException(tokenCur(), "Dublicate procedure name.", null);
            }
            if (tokenNext().getLexType().getType() != LexTypeEnum.BKTB)
                throw new CompileException(tokenCur(), "Symbol expected \"(\".", null);
            Token token_ProcParam = tokenNext();
            if (token_ProcParam.getLexType().getType() != LexTypeEnum.ID) {
                if (tokenNext().getLexType().getType() != LexTypeEnum.BKTE)
                    throw new CompileException(tokenCur(), "Symbol expected \")\".", null);
                token_ProcParam = null;
            } else if (tokenNext().getLexType().getType() != LexTypeEnum.BKTE) {
                throw new CompileException(tokenCur(), "Symbol expected \")\".", null);
            }
            if (tokenNext().getLexType().getType() != LexTypeEnum.BLOCKB)
                throw new CompileException(tokenCur(), "Symbol expected \":\".", null);
            newEnv();
            Expr_IdVar paramProc = null;
            if (token_ProcParam != null) {
                paramProc = new Expr_IdVar(token_ProcParam.getRow(), token_ProcParam.getCol(), token_ProcParam.getValue());
                getEnv().putVar(paramProc);
            }
            Expr_IdProc nameProc = new Expr_IdProc(token_ProcName.getRow(), token_ProcName.getCol(), token_ProcName.getValue());
            Stmt_Proc stmtProc = new Stmt_Proc(token_ProcDef.getRow(), token_ProcDef.getCol(), nameProc);
            putProc(stmtProc);
            stmtProc.setEnv(getEnv());
            stmtProc.setParamProc(paramProc);
            stackProc.push(stmtProc);
            stmtProc.setBody(parseBlock());
            stackProc.pop();
            restorePrevEnv();
            return null;
        }
        if (tokenCur().getLexType().getType() == LexTypeEnum.IF) {
            Token tokenIf = tokenCur();
            tokenNext();
            Expr_Bool cond = parseExprBool();
            return new Stmt_If(tokenIf.getRow(), tokenIf.getCol(), cond, parseBlock());
        }
        if (tokenCur().getLexType().getType() == LexTypeEnum.RETURN) {
            Token tokenReturn = tokenCur();
            if (stackProc.isEmpty()) throw new CompileException(tokenCur(), "Return non defined procedure.", null);
            tokenNext();
            Expr retValue = parseExpr();
            if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKE)
                throw new CompileException(tokenCur(), "Statement after return.", null);
            return new Stmt_Return(tokenReturn.getRow(), tokenReturn.getCol(), stackProc.peekLast(), retValue);
        }
        if (tokenCur().getLexType().getType() == LexTypeEnum.ID) {
            if (tokenPeek(1).getLexType().getType() == LexTypeEnum.EQUAL) {
                Token tokenVar = tokenCur();
                Expr_IdVar nameVar = getEnv().getVar(tokenCur().getValue());
                if (nameVar == null) {
                    nameVar = new Expr_IdVar(tokenVar.getRow(), tokenVar.getCol(), tokenVar.getValue());
                    getEnv().putVar(nameVar);
                }
                tokenNext();
                tokenNext();
                return new Stmt_Set(tokenVar.getRow(), tokenVar.getCol(), nameVar, parseExpr());
            }
            if (tokenPeek(1).getLexType().getType() == LexTypeEnum.BKTB) {
                Token tokenProc = tokenCur();
                Stmt_Proc declProc = getProc(tokenProc.getValue());
                if (declProc == null) throw new CompileException(tokenProc, "Bad identifier.", null);
                Expr exprParam = null;
                if (tokenPeek(2).getLexType().getType() != LexTypeEnum.BKTE) {
                    tokenNext();
                    tokenNext();
                    exprParam = parseExpr();
                    if (tokenCur().getLexType().getType() != LexTypeEnum.BKTE)
                        throw new CompileException(tokenCur(), "Symbol expected \")\".", null);
                    tokenNext();
                } else {
                    tokenNext();
                    tokenNext();
                    tokenNext();
                }
                return new Stmt_Set(tokenProc.getRow(), tokenProc.getCol(), null, new Expr_Call(tokenProc.getRow(), tokenProc.getCol(), declProc, exprParam, getEnv()));
            }
            throw new CompileException(tokenPeek(1), "Bad token.", null);
        }
        throw new CompileException(tokenCur(), "Bad token.", null);
    }

    public Expr_Unary parseExprUnary() throws CompileException {
        Token token = tokenCur();
        if (token.getLexType().getType() == LexTypeEnum.NUM) {
            try {
                Expr_Num valueNum = new Expr_Num(token.getRow(), token.getCol(), Integer.parseInt(token.getValue(), 10));
                tokenNext();
                return new Expr_Unary(token.getRow(), token.getCol(), valueNum);
            } catch (Throwable e) {
                throw new CompileException(token, "Bad number value.", null);
            }
        }
        if (token.getLexType().getType() == LexTypeEnum.ID) {
            Stmt_Proc proc = getProc(token.getValue());
            if (proc != null) {
                tokenNext();
                if (tokenCur().getLexType().getType() != LexTypeEnum.BKTB)
                    throw new CompileException(tokenCur(), "Symbol expected \"(\".", null);
                tokenNext();
                Expr exprParam = null;
                if (tokenCur().getLexType().getType() != LexTypeEnum.BKTE) {
                    exprParam = parseExpr();
                    if (tokenCur().getLexType().getType() != LexTypeEnum.BKTE)
                        throw new CompileException(tokenCur(), "Symbol expected \")\".", null);
                    tokenNext();
                } else {
                    tokenNext();
                }
                if ((proc.getParamProc() == null && exprParam != null) || (proc.getParamProc() != null && exprParam == null)) {
                    throw new CompileException(token, "Mismatch parameters procedure.", null);
                }
                Expr_Call callProc = new Expr_Call(token.getRow(), token.getCol(), proc, exprParam, getEnv());
                return new Expr_Unary(token.getRow(), token.getCol(), callProc);
            }
            Expr_IdVar nameVar = getEnv().getVar(token.getValue());
            if (nameVar != null) {
                Expr_GetVarValue getVarValue = new Expr_GetVarValue(token.getRow(), token.getCol(), nameVar);
                tokenNext();
                return new Expr_Unary(token.getRow(), token.getCol(), getVarValue);
            }
            throw new CompileException(token, "Identifier not defined.", null);
        }
        throw new CompileException(token, "Bad expession value.", null);
    }

    public Expr parseExpr() throws CompileException {
        Expr_Unary value1 = parseExprUnary();
        Token token = tokenCur();
        if (token.getLexType().getType() == LexTypeEnum.PLUS) {
            tokenNext();
            return new Expr_ArithPlus(token.getRow(), token.getCol(), value1, parseExpr(), getEnv());
        }
        if (token.getLexType().getType() == LexTypeEnum.MINUS) {
            tokenNext();
            return new Expr_ArithMinus(token.getRow(), token.getCol(), value1, parseExpr(), getEnv());
        }
        return value1;
    }

    public Expr_Bool parseExprBool() throws CompileException {
        Token tokenExpr = tokenCur();
        Expr expr1 = parseExpr();
        if (tokenCur().getLexType().getType() == LexTypeEnum.GREATER) {
            tokenNext();
            return new Expr_BoolGreater(tokenExpr.getRow(), tokenExpr.getCol(), expr1, parseExpr(), getEnv());
        }
        throw new CompileException(tokenCur(), "Bad condition symbol.", null);
    }

}