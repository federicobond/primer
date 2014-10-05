package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class ReturnNode extends Node {

    private final Node value;

    public ReturnNode(Node value) {
        this.value = value;
    }


    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitReturnNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(value);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.RETURNNODE;
    }

    public Node getValue() {
        return value;
    }
}
