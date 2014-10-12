package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

public class SubstractNode extends BinaryOperationNode {

    public SubstractNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitSubstractNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.SUBSTRACTNODE;
    }
}
