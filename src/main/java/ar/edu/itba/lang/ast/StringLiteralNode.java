package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class StringLiteralNode extends Node {

    private final String value;

    public StringLiteralNode(String value) {
        this.value = value;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitStringLiteralNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.STRINGLITERALNODE;
    }

    public String getValue() {
        return value;
    }
}
