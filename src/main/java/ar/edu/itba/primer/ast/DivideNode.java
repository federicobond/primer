package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

public class DivideNode extends BinaryOperationNode {

    public DivideNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitDivideNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.DIVIDENODE;
    }
}
