package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

public class SubtractNode extends BinaryOperationNode {

    public SubtractNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitSubtractNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.SUBTRACTNODE;
    }
}
