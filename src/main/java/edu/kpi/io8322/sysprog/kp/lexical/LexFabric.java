package edu.kpi.io8322.sysprog.kp.lexical;

import java.util.HashMap;
import java.util.Map;

public class LexFabric {
    public static final String LEXTYPE_BKTB = "bktb";
    public static final String LEXTYPE_BKTE = "bkte";
    public static final String LEXTYPE_BLOCKB = "blockb";
    public static final String LEXTYPE_BLOCKE = "blocke";
    public static final String LEXTYPE_DEF = "def";
    public static final String LEXTYPE_EQUAL = "equal";
    public static final String LEXTYPE_GREATER = "greater";
    public static final String LEXTYPE_IF = "if";
    public static final String LEXTYPE_MINUS = "minus";
    public static final String LEXTYPE_NUM = "num";
    public static final String LEXTYPE_PLUS = "plus";
    public static final String LEXTYPE_RETURN = "return";
    public static final String LEXTYPE_ID = "id";
    public static final String LEXTYPE_COMMENT = "comment";


    private Map<String, LexType> lexTypeMap;
    private Map<Character, LexType> lexTypeSymbMap;
    private Map<String, LexType> lexTypeDictMap;

    public LexFabric(LexicalAnalyzer lexicalAnalyzer) {
        lexTypeMap = new HashMap<>();
        lexTypeMap.put(LEXTYPE_BKTB, new LexType_bktb());
        lexTypeMap.put(LEXTYPE_BKTE, new LexType_bkte());
        lexTypeMap.put(LEXTYPE_BLOCKB, new LexType_blockb());
        lexTypeMap.put(LEXTYPE_BLOCKE, new LexType_blocke());
        lexTypeMap.put(LEXTYPE_DEF, new LexType_def());
        lexTypeMap.put(LEXTYPE_EQUAL, new LexType_equal());
        lexTypeMap.put(LEXTYPE_GREATER, new LexType_greater());
        lexTypeMap.put(LEXTYPE_IF, new LexType_if());
        lexTypeMap.put(LEXTYPE_MINUS, new LexType_minus());
        lexTypeMap.put(LEXTYPE_NUM, new LexType_num());
        lexTypeMap.put(LEXTYPE_PLUS, new LexType_plus());
        lexTypeMap.put(LEXTYPE_RETURN, new LexType_return());
        lexTypeMap.put(LEXTYPE_ID, new LexType_id());
        lexTypeMap.put(LEXTYPE_COMMENT, new LexType_comment());
        for(String typeName: lexTypeMap.keySet()){
            LexType lexType = lexTypeMap.get(typeName);
            lexType.setLexicalAnalyzer(lexicalAnalyzer);
            lexType.setTypeName(typeName);
        }

        lexTypeSymbMap = new HashMap<>();
        lexTypeSymbMap.put(Character.valueOf('('), getLexType(LEXTYPE_BKTB));
        lexTypeSymbMap.put(Character.valueOf(')'), getLexType(LEXTYPE_BKTE));
        lexTypeSymbMap.put(Character.valueOf('='), getLexType(LEXTYPE_EQUAL));
        lexTypeSymbMap.put(Character.valueOf('>'), getLexType(LEXTYPE_GREATER));
        lexTypeSymbMap.put(Character.valueOf('-'), getLexType(LEXTYPE_MINUS));
        lexTypeSymbMap.put(Character.valueOf('+'), getLexType(LEXTYPE_PLUS));

        lexTypeDictMap = new HashMap<>();
        lexTypeDictMap.put("def", getLexType(LEXTYPE_DEF));
        lexTypeDictMap.put("if", getLexType(LEXTYPE_IF));
        lexTypeDictMap.put("return", getLexType(LEXTYPE_RETURN));

    }

    public LexType getLexType(String typeName){
        return lexTypeMap.get(typeName);
    }

    public LexType getLexType_symb(char value){
        return lexTypeSymbMap.get(Character.valueOf(value));
    }

    public LexType getLexType_dict(String value){
        return lexTypeDictMap.get(value);
    }
}
