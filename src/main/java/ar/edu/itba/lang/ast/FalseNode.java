package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class FalseNode extends Node {

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitFalseNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.FALSENODE;
    }
}
