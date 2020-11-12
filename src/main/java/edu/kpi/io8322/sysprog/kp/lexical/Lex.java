package edu.kpi.io8322.sysprog.kp.lexical;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Lex {
    private LexType lexType;
    private int row;
    private int col;
    private String value;

    @Override
    public String toString(){
        return "["+row+","+col+"]["+lexType.getTypeName()+"]"+(value==null?"":" -> "+value);
    }
}
