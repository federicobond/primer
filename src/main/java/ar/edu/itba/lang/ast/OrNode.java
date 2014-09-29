package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class OrNode extends BinaryOperationNode {

    public OrNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public void accept(NodeVisitor visitor) {

    }
}
