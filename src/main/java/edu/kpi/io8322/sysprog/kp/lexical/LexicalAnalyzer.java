package edu.kpi.io8322.sysprog.kp.lexical;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class LexicalAnalyzer {
    private static final Logger logger = Logger.getLogger(LexicalAnalyzer.class.getName());
    private List<String> srclines;
    private List<Token> tokenList;
    private LexFabric lexFabric;

    public LexicalAnalyzer(List<String> srclines) {
        this.srclines = srclines;
        this.lexFabric = new LexFabric(this);
    }

    public void exec() throws CompileException {
        logInfo(null, null, "Lexical analyzer starting");
        tokenList = new ArrayList<>();
        tokenList.add(new Token(lexFabric.getLexType(LexFabric.LEXTYPE_BLOCKB), 1, 0, null));
        List<Integer> treeBlockIndent = new ArrayList<>();
        treeBlockIndent.add(Integer.valueOf(0));
        for (int row = 0; row < srclines.size(); row++) {
            String line = srclines.get(row);
            boolean commentadd = false;
            int pos_comment = line.indexOf('#');
            if (pos_comment >= 0) {
                if (pos_comment != line.length() - 1) {
                    tokenList.add(new Token(lexFabric.getLexType(LexFabric.LEXTYPE_COMMENT), row, pos_comment, line.substring(pos_comment + 1, line.length())));
                    commentadd = true;
                }
                if (pos_comment > 0) {
                    line = line.substring(0, pos_comment);
                } else {
                    line = "";
                }
            }
            if (line.trim().isEmpty()) continue;
            int col = 0;
            int indent = parseSpace(line, col);
            while (!treeBlockIndent.isEmpty() && treeBlockIndent.get(treeBlockIndent.size() - 1).intValue() != indent) {
                if (commentadd) {
                    tokenList.add(tokenList.size() - 1, new Token(lexFabric.getLexType(LexFabric.LEXTYPE_BLOCKE), row + 1, col + 1, null));
                } else {
                    tokenList.add(new Token(lexFabric.getLexType(LexFabric.LEXTYPE_BLOCKE), row + 1, col + 1, null));
                }
                treeBlockIndent.remove(treeBlockIndent.size() - 1);
            }
            if (treeBlockIndent.isEmpty()) throw new CompileException(row + 1, col + 1, "Bad block indent.", null);
            col = indent;
            while (col < line.length()) {
                if (line.charAt(col) == ' ') {
                    col++;
                    continue;
                }
                if (line.charAt(col) == ':') {
                    if (col + 1 < line.length() && !line.substring(col + 1, line.length()).trim().isEmpty())
                        throw new CompileException(row + 1, col + 2, "Not empty after colon.", null);
                    tokenList.add(new Token(lexFabric.getLexType(LexFabric.LEXTYPE_BLOCKB), row + 1, col + 1, null));
                    if (row == srclines.size() - 1)
                        throw new CompileException(row + 1, col + 1, "The body of the nested block is missing.", null);
                    int blockIndent = parseSpace(srclines.get(row + 1), 0) - 0;
                    if (blockIndent <= treeBlockIndent.get(treeBlockIndent.size() - 1).intValue())
                        throw new CompileException(row + 2, 1, "Invalid indentation of nested block.", null);
                    treeBlockIndent.add(Integer.valueOf(blockIndent));
                    break;
                }
                LexType lexType = lexFabric.getLexType_symb(line.charAt(col));
                if (lexType != null) {
                    tokenList.add(new Token(lexType, row + 1, col + 1, null));
                    col++;
                    continue;
                }
                int next = parseIdent(line, col);
                if (next > col) {
                    String value = line.substring(col, next);
                    lexType = lexFabric.getLexType_dict(value);
                    if (lexType != null) {
                        tokenList.add(new Token(lexType, row + 1, col + 1, null));
                    } else if (checkNum(value)) {
                        tokenList.add(new Token(lexFabric.getLexType(LexFabric.LEXTYPE_NUM), row + 1, col + 1, value));
                    } else {
                        checkId(value, row + 1, col + 1);
                        tokenList.add(new Token(lexFabric.getLexType(LexFabric.LEXTYPE_ID), row + 1, col + 1, value));
                    }
                    col = next;
                    continue;
                }
                throw new CompileException(row + 1, col + 1, "Invalid symbol", null);
            }
        }
        tokenList.add(new Token(lexFabric.getLexType(LexFabric.LEXTYPE_BLOCKE), srclines.size(), srclines.size() > 0 ? srclines.get(srclines.size() - 1).length() + 1 : 1, null));
        for (Token token : tokenList) {
            System.out.println(token);
        }
        logInfo(null, null, "Lexical analyzer finished OK [" + tokenList.size() + " lexems]");
    }

    private boolean checkNum(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) < '0' || value.charAt(i) > '9') return false;
        }
        return true;
    }

    private boolean checkId(String value, int row, int col) throws CompileException {
        if (!((value.charAt(0) >= 'A' && value.charAt(0) <= 'Z') || (value.charAt(0) >= 'a' && value.charAt(0) <= 'z')))
            throw new CompileException(row, col, "The first character must be a letter.", null);
        for (int i = 1; i < value.length(); i++) {
            if (!((value.charAt(i) >= 'A' && value.charAt(i) <= 'Z') ||
                    (value.charAt(i) >= 'a' && value.charAt(i) <= 'z') ||
                    (value.charAt(i) >= '0' && value.charAt(i) <= '9') ||
                    value.charAt(i)=='_'))
                throw new CompileException(row, col, "Erroneous character in identifier.", null);
        }
        return true;
    }

    private int parseSpace(String line, int start) {
        int cur = start;
        while (cur < line.length() && line.charAt(cur) == ' ') cur++;
        return cur;
    }

    private int parseIdent(String line, int start) {
        int cur = start;
        while (cur < line.length()) {
            if ((line.charAt(cur) >= 'A' && line.charAt(cur) <= 'Z') || (line.charAt(cur) >= 'a' && line.charAt(cur) <= 'z') ||
                    (line.charAt(cur) >= '0' && line.charAt(cur) <= '9') || line.charAt(cur) == '_') {
                cur++;
            } else {
                break;
            }
        }
        return cur;
    }

    public void logInfo(String sourceClass, String sourceMethod, String msg) {
        if (sourceClass == null) {
            logger.logp(Level.INFO, "Lexical", sourceMethod, msg);
        } else {
            logger.logp(Level.INFO, sourceClass, sourceMethod, msg);
        }
    }

    public void logError(String sourceClass, String sourceMethod, String msg) {
        if (sourceClass == null) {
            logger.logp(Level.SEVERE, "Lexical", sourceMethod, msg);
        } else {
            logger.logp(Level.SEVERE, sourceClass, sourceMethod, msg);
        }
    }
}
