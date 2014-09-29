package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class DivideNode extends BinaryOperationNode {

    public DivideNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public void accept(NodeVisitor visitor) {

    }
}
