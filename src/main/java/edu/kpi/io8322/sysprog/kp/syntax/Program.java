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
        tokenIndexCur = -1;
        tokenNext();
        int tokenStart = tokenIndexCur;
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKB)
            throw new CompileException(tokenCur(), "Not start program.", null);
        root = new Stmt_Prog(newEnv());
        stmtSetComment(root, tokenStart);
        tokenNext();
        Stmt_ProcPrint procPrint = new Stmt_ProcPrint(newEnv());
        Expr_IdVar paramPrint = new Expr_IdVar(getEnv(),1, 0, "printValue");
        procPrint.setParamProc(paramPrint);
        getEnv().putVar(paramPrint);
        putProc(procPrint);
        restorePrevEnv();
        Stmt_Seq bodyProg = new Stmt_Seq(getEnv(), tokenCur().getRow(), tokenCur().getCol(), null, parseStmtSeq());
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
        while(true) {
            tokenIndexCur++;
            if (tokenIndexCur < syntaxAnalyzer.getTokenList().size()) {
                if(syntaxAnalyzer.getTokenList().get(tokenIndexCur).getLexType().getType()!=LexTypeEnum.COMMENT){
                    return syntaxAnalyzer.getTokenList().get(tokenIndexCur);
                }
            } else {
                return null;
            }
        }
    }

    public void stmtSetComment(Stmt stmt, int tokenStart){
        List<String> listComment = new ArrayList<>();
        while(tokenStart>0){
            if(syntaxAnalyzer.getTokenList().get(tokenStart-1).getLexType().getType()==LexTypeEnum.COMMENT){
                listComment.add(0, syntaxAnalyzer.getTokenList().get(tokenStart-1).getValue());
                tokenStart--;
            } else {
                break;
            }
        }
        if(!listComment.isEmpty()) stmt.setListComment(listComment);
    }

    public Stmt_Block parseBlock() throws CompileException {
        if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKB)
            throw new CompileException(tokenCur(), "Not start block.", null);
        int tokenStart = tokenIndexCur;
        Stmt_Block stmtBlock = new Stmt_Block(newEnv(), tokenCur().getRow(), tokenCur().getCol());
        stmtSetComment(stmtBlock, tokenStart);
        tokenNext();
        stmtBlock.setBody(parseStmtSeq());
        restorePrevEnv();
        tokenNext();
        return stmtBlock;
    }

    public Stmt_Seq parseStmtSeq() throws CompileException {
        if (tokenCur().getLexType().getType() == LexTypeEnum.BLOCKE) return null;
        Stmt_Seq stmt_seq = new Stmt_Seq(getEnv(), tokenCur().getRow(), tokenCur().getCol(), parseStmt(), parseStmtSeq());
        return stmt_seq;
    }

    public Stmt parseStmt() throws CompileException {
        if (tokenCur().getLexType().getType() == LexTypeEnum.DEF) {
            int tokenStart = tokenIndexCur;
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
                paramProc = new Expr_IdVar(getEnv(), token_ProcParam.getRow(), token_ProcParam.getCol(), token_ProcParam.getValue());
                getEnv().putVar(paramProc);
            }
            Expr_IdProc nameProc = new Expr_IdProc(getEnv(), token_ProcName.getRow(), token_ProcName.getCol(), token_ProcName.getValue());
            Stmt_Proc stmtProc = new Stmt_Proc(getEnv(), token_ProcDef.getRow(), token_ProcDef.getCol(), nameProc);
            stmtSetComment(stmtProc, tokenStart);
            putProc(stmtProc);
            stmtProc.setParamProc(paramProc);
            stackProc.push(stmtProc);
            stmtProc.setBody(parseBlock());
            stackProc.pop();
            restorePrevEnv();
            return null;
        }
        if (tokenCur().getLexType().getType() == LexTypeEnum.IF) {
            int tokenStart = tokenIndexCur;
            Token tokenIf = tokenCur();
            tokenNext();
            Expr_Bool cond = parseExprBool();
            Stmt_If stmt_if = new Stmt_If(getEnv(), tokenIf.getRow(), tokenIf.getCol(), cond, parseBlock());
            stmtSetComment(stmt_if, tokenStart);
            return stmt_if;
        }
        if (tokenCur().getLexType().getType() == LexTypeEnum.RETURN) {
            int tokenStart = tokenIndexCur;
            Token tokenReturn = tokenCur();
            if (stackProc.isEmpty()) throw new CompileException(tokenCur(), "Return non defined procedure.", null);
            tokenNext();
            Expr retValue = parseExpr();
            if (tokenCur().getLexType().getType() != LexTypeEnum.BLOCKE)
                throw new CompileException(tokenCur(), "Statement after return.", null);
            Stmt_Return stmt_return = new Stmt_Return(getEnv(), tokenReturn.getRow(), tokenReturn.getCol(), stackProc.peekLast(), retValue);
            stmtSetComment(stmt_return, tokenStart);
            return stmt_return;
        }
        if (tokenCur().getLexType().getType() == LexTypeEnum.ID) {
            int tokenStart = tokenIndexCur;
            if (tokenPeek(1).getLexType().getType() == LexTypeEnum.EQUAL) {
                Token tokenVar = tokenCur();
                Expr_IdVar nameVar = getEnv().getVar(tokenCur().getValue());
                if (nameVar == null) {
                    nameVar = new Expr_IdVar(getEnv(), tokenVar.getRow(), tokenVar.getCol(), tokenVar.getValue());
                    getEnv().putVar(nameVar);
                }
                tokenNext();
                tokenNext();
                Stmt_Set stmt_set = new Stmt_Set(getEnv(), tokenVar.getRow(), tokenVar.getCol(), nameVar, parseExpr());
                stmtSetComment(stmt_set, tokenStart);
                return stmt_set;
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
                Stmt_Set stmt_set = new Stmt_Set(getEnv(), tokenProc.getRow(), tokenProc.getCol(), null, new Expr_Call(getEnv(), tokenProc.getRow(), tokenProc.getCol(), declProc, exprParam));
                stmtSetComment(stmt_set, tokenStart);
                return stmt_set;
            }
            throw new CompileException(tokenPeek(1), "Bad token.", null);
        }
        throw new CompileException(tokenCur(), "Bad token.", null);
    }

    public Expr_Unary parseExprUnary() throws CompileException {
        Token token = tokenCur();
        if (token.getLexType().getType() == LexTypeEnum.NUM) {
            try {
                Expr_Num valueNum = new Expr_Num(getEnv(), token.getRow(), token.getCol(), Integer.parseInt(token.getValue(), 10));
                tokenNext();
                return new Expr_Unary(getEnv(), token.getRow(), token.getCol(), valueNum);
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
                Expr_Call callProc = new Expr_Call(getEnv(), token.getRow(), token.getCol(), proc, exprParam);
                return new Expr_Unary(getEnv(), token.getRow(), token.getCol(), callProc);
            }
            Expr_IdVar nameVar = getEnv().getVar(token.getValue());
            if (nameVar != null) {
                Expr_GetVarValue getVarValue = new Expr_GetVarValue(getEnv(), token.getRow(), token.getCol(), nameVar);
                tokenNext();
                return new Expr_Unary(getEnv(), token.getRow(), token.getCol(), getVarValue);
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
            return new Expr_ArithPlus(getEnv(), token.getRow(), token.getCol(), value1, parseExpr());
        }
        if (token.getLexType().getType() == LexTypeEnum.MINUS) {
            tokenNext();
            return new Expr_ArithMinus(getEnv(), token.getRow(), token.getCol(), value1, parseExpr());
        }
        return value1;
    }

    public Expr_Bool parseExprBool() throws CompileException {
        Token tokenExpr = tokenCur();
        Expr expr1 = parseExpr();
        if (tokenCur().getLexType().getType() == LexTypeEnum.GREATER) {
            tokenNext();
            return new Expr_BoolGreater(getEnv(), tokenExpr.getRow(), tokenExpr.getCol(), expr1, parseExpr());
        }
        throw new CompileException(tokenCur(), "Bad condition symbol.", null);
    }

}