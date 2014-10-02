package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class LessThanNode extends BinaryOperationNode {

    public LessThanNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitLessThanNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.LESSTHANNODE;
    }
}
