package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

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
