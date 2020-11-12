package edu.kpi.io8322.sysprog.kp.lexical;

import edu.kpi.io8322.sysprog.kp.PythonCompiler;
import edu.kpi.io8322.sysprog.kp.core.CompileException;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class LexicalAnalyzer {
    private static final Logger logger = Logger.getLogger(LexicalAnalyzer.class.getName());
    private List<String> srclines;

    public LexicalAnalyzer(List<String> srclines) {
        this.srclines = srclines;
    }

    public void exec() throws CompileException {
        logInfo(null, null, "Lexical analyzer starting");
        if(!srclines.isEmpty()) throw new CompileException(3, 12, "Error 1", null);
        logInfo(null, null, "Lexical analyzer finished OK");
    }

    public void logInfo(String sourceClass, String sourceMethod, String msg) {
        if(sourceClass==null){
            logger.logp(Level.INFO, "Lexical", sourceMethod, msg);
        } else {
            logger.logp(Level.INFO, sourceClass, sourceMethod, msg);
        }
    }
    public void logError(String sourceClass, String sourceMethod, String msg) {
        if(sourceClass==null){
            logger.logp(Level.SEVERE, "Lexical", sourceMethod, msg);
        } else {
            logger.logp(Level.SEVERE, sourceClass, sourceMethod, msg);
        }
    }
}
