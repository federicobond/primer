package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

public class AddNode extends BinaryOperationNode {

    public AddNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitAddNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ADDNODE;
    }
}
