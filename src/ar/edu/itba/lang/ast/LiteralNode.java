package ar.edu.itba.lang.ast;

import java.util.List;

public class LiteralNode extends Node {

    private final Object value;

    public LiteralNode(Object value) {
        this.value = value;
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }
}
