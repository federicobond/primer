package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class MultiplyNode extends BinaryOperationNode {

    public MultiplyNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitMultiplyNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.MULTIPLYNODE;
    }
}
