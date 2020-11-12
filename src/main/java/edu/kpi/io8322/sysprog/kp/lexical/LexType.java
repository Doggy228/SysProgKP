package edu.kpi.io8322.sysprog.kp.lexical;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class LexType {
    private LexicalAnalyzer lexicalAnalyzer;
    private String typeName;
}
