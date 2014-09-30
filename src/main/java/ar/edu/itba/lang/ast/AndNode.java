package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class AndNode extends BinaryOperationNode {

    public AndNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitAndNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ANDNODE;
    }
}