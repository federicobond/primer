package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class BreakNode extends Node {

    public static final BreakNode INSTANCE = new BreakNode();

    private BreakNode() { }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitBreakNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.BREAKNODE;
    }
}
