package ar.edu.itba.primer.ast;

import org.objectweb.asm.Type;

public enum NodeType {
    ADDNODE, ANDNODE, ARGSNODE, BLOCKNODE, CALLNODE, DIVIDENODE, FALSENODE,
    IFNODE, LITERALNODE, MULTIPLYNODE, NEGATENODE, ORNODE, SUBSTRACTNODE,
    TRUENODE, WHILENODE, LESSTHANNODE, GREATERTHANNODE, EQUALNODE, FUNCTIONNODE,
    FUNCTIONARGSNODE, RETURNNODE, NILNODE, BREAKNODE, CONTINUENODE,
    STRINGLITERALNODE, LESSEQUALSTHANNODE, GREATEREQUALTHANNODE, NOTEQUALNODE,
    MODULUSNODE, ASSIGNMENTNODE, DECLARATIONNODE, VARIABLENODE;

    public boolean isAlwaysTrue() {
        return this == TRUENODE;
    }

    public boolean isAlwaysFalse() {
        switch (this) {
            case FALSENODE:
            case NILNODE:
                return true;
            default:
                return false;
        }
    }

    public Type getType() {
        switch (this) {
            case TRUENODE:
            case FALSENODE:
            case EQUALNODE:
            case NOTEQUALNODE:
            case LESSTHANNODE:
            case LESSEQUALSTHANNODE:
            case GREATERTHANNODE:
            case GREATEREQUALTHANNODE:
                return Type.BOOLEAN_TYPE;
            case LITERALNODE:
            case ADDNODE:
            case SUBSTRACTNODE:
            case MULTIPLYNODE:
            case DIVIDENODE:
            case MODULUSNODE:
                return Type.INT_TYPE;
            case STRINGLITERALNODE:
            case VARIABLENODE:
            case NILNODE:
                return Type.getType(Object.class);
            default:
                throw new RuntimeException("missing type info for node " + this);
        }
    }
}
