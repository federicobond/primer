package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class VariableNode extends Node {

    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitVariableNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.VARIABLENODE;
    }

    public String getName() {
        return name;
    }
}
