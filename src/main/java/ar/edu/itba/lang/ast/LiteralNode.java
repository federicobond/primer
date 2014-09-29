package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class LiteralNode extends Node {

    private final Object value;

    public LiteralNode(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public void accept(NodeVisitor visitor) {

    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }
}
