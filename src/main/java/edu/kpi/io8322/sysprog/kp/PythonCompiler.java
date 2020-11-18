package edu.kpi.io8322.sysprog.kp;

import edu.kpi.io8322.sysprog.kp.core.CompileException;
import edu.kpi.io8322.sysprog.kp.lexical.LexicalAnalyzer;
import edu.kpi.io8322.sysprog.kp.syntax.SyntaxAnalyzer;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Getter
public class PythonCompiler {
    static {
        InputStream is = PythonCompiler.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Logger logger = Logger.getLogger(PythonCompiler.class.getName());
    public static PythonCompiler app;

    private String srcname;
    private String dstname;
    private List<String> srclines;
    private LexicalAnalyzer lexicalAnalyzer;
    private SyntaxAnalyzer syntaxAnalyzer;

    public PythonCompiler(String srcname) {
        this.srcname = srcname;
        if (srcname.endsWith(".py")) {
            dstname = srcname.substring(0, srcname.length() - ".py".length()) + ".asm";
        } else {
            dstname = srcname + ".asm";
        }
    }

    public int exec() {
        logInfo(null, null, "Source file: " + srcname);
        srclines = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(srcname));
            String line = br.readLine();
            while (line != null) {
                srclines.add(line);
                line = br.readLine();
            }
            br.close();
            logInfo(null, null, "Read " + srclines.size() + " rows.");
        } catch (Throwable e) {
            logError(null, null, "Error read file " + srcname);
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable e1) {
                }
            }
            return 1;
        }
        lexicalAnalyzer = new LexicalAnalyzer(srclines);
        try {
            lexicalAnalyzer.exec();
        } catch(CompileException e){
            lexicalAnalyzer.logError("Lexical", null, e.toString());
            return 1;
        }
        syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer.getTokenList());
        try {
            syntaxAnalyzer.exec();
            System.out.println("Syntax Tree::");
            StringBuilder bufPrint = new StringBuilder();
            syntaxAnalyzer.getProgram().getRoot().printTree(bufPrint, "");
            System.out.println(bufPrint);

        } catch(CompileException e){
            syntaxAnalyzer.logError("Syntax", null, e.toString());
            return 1;
        }
        try {
            logInfo(null, null, "Generate destination files");
            StringWriter stringWriter = new StringWriter();
            BufferedWriter writer = new BufferedWriter(stringWriter);
            syntaxAnalyzer.getProgram().execOut(writer);
            writer.close();
            String bodyResultFile = new String(stringWriter.getBuffer());
            System.out.println(bodyResultFile);
            BufferedWriter writerFile = new BufferedWriter(new FileWriter(dstname));
            writerFile.write(bodyResultFile);
            writerFile.close();
        } catch (Throwable e){
            logError("Generator", null, e.toString());
            return 1;
        }
        logInfo(null, null, "Result file: " + dstname);
        return 0;
    }

    public void logInfo(String sourceClass, String sourceMethod, String msg) {
        if (sourceClass == null) {
            logger.logp(Level.INFO, "Compiler", sourceMethod, msg);
        } else {
            logger.logp(Level.INFO, sourceClass, sourceMethod, msg);
        }
    }

    public void logError(String sourceClass, String sourceMethod, String msg) {
        if (sourceClass == null) {
            logger.logp(Level.SEVERE, "Compiler", sourceMethod, msg);
        } else {
            logger.logp(Level.SEVERE, sourceClass, sourceMethod, msg);
        }
    }

    public static void main(String[] argc) {
        if (argc.length < 1) {
            System.err.println("Usage: PythonCompiler <source_file>");
            System.err.println();
            System.exit(1);
        }
        PythonCompiler.app = new PythonCompiler(argc[0]);
        long timeexec = System.currentTimeMillis();
        PythonCompiler.app.logInfo(null, null, "Python compiler");
        int exitcode = PythonCompiler.app.exec();
        switch (exitcode) {
            case 0:
                PythonCompiler.app.logInfo(null, null, "Python Compiler finished OK. [ " + (System.currentTimeMillis() - timeexec) + " ms]");
                break;
            default:
                PythonCompiler.app.logError(null, null, "Python Compiler finished ERROR. [ " + (System.currentTimeMillis() - timeexec) + " ms]");
                break;
        }
        System.exit(exitcode);
    }
}


