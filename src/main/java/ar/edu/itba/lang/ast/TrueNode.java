package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class TrueNode extends Node {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitTrueNode(this);
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
