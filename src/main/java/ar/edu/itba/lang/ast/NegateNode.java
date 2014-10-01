package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class NegateNode extends Node {

    private final Node node;

    public NegateNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitNegateNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(node);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NEGATENODE;
    }
}
