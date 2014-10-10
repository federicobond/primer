package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class GreaterEqualThanNode extends BinaryOperationNode {

    public GreaterEqualThanNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitGreaterEqualThanNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.GREATEREQUALTHANNODE;
    }
}
