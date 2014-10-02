package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class EqualNode extends BinaryOperationNode {

    public EqualNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitEqualNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.EQUALNODE;
    }
}
