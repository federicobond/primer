package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

public class AndNode extends BinaryOperationNode {

    public AndNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitAndNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ANDNODE;
    }
}
