package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

public class OrNode extends BinaryOperationNode {

    public OrNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitOrNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ORNODE;
    }
}
