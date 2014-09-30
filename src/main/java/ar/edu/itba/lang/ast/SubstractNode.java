package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class SubstractNode extends BinaryOperationNode {

    public SubstractNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitSubstractNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.SUBSTRACTNODE;
    }
}
