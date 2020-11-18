package edu.kpi.io8322.sysprog.kp.syntax;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import edu.kpi.io8322.sysprog.kp.lexical.Token;
import lombok.Getter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class SyntaxAnalyzer {
    private static final Logger logger = Logger.getLogger(SyntaxAnalyzer.class.getName());
    private List<Token> tokenList;
    private Program program;

    public SyntaxAnalyzer(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public void exec() throws CompileException {
        logInfo(null, null, "Syntax analyzer starting");
        program = new Program(this);
        program.execSyntax();
        logInfo(null, null, "Syntax analyzer finished OK ["+ tokenList.size()+" lexems]");
    }

    public void logInfo(String sourceClass, String sourceMethod, String msg) {
        if (sourceClass == null) {
            logger.logp(Level.INFO, "Syntax", sourceMethod, msg);
        } else {
            logger.logp(Level.INFO, sourceClass, sourceMethod, msg);
        }
    }

    public void logError(String sourceClass, String sourceMethod, String msg) {
        if (sourceClass == null) {
            logger.logp(Level.SEVERE, "Syntax", sourceMethod, msg);
        } else {
            logger.logp(Level.SEVERE, sourceClass, sourceMethod, msg);
        }
    }

}
