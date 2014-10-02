package ar.edu.itba.lang.ast;

public enum NodeType {
    ADDNODE, ANDNODE, ARGSNODE, BLOCKNODE, CALLNODE, DIVIDENODE, FALSENODE, IFNODE,
    LITERALNODE, MULTIPLYNODE, NEGATENODE, ORNODE, SUBSTRACTNODE, TRUENODE, WHILENODE,
    LESSTHANNODE, GREATERTHANNODE, EQUALNODE;

    public boolean isAlwaysTrue() {
        switch(this) {
            case TRUENODE:
            case LITERALNODE:
                return true;
            default:
                return false;
        }
    }

    public boolean isAlwaysFalse() {
        switch(this) {
            case FALSENODE:
                return true;
            default:
                return false;
        }
    }
}
