package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class TrueNode extends Node {

    public static final TrueNode INSTANCE = new TrueNode();

    private TrueNode() { }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitTrueNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TRUENODE;
    }
}
