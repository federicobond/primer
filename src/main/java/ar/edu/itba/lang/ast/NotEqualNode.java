package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

/**
 * Created by jorge on 06/10/2014.
 */
public class NotEqualNode extends BinaryOperationNode{


    public NotEqualNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitNotEqualNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NOTEQUALNODE;
    }
}
