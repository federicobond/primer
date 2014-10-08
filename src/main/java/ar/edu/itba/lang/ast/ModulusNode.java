package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

/**
 * Created by administrator on 10/8/14.
 */
public class ModulusNode extends BinaryOperationNode {

    public ModulusNode(Node first, Node second) {
        super(first, second);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitModulusNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.MODULUSNODE;
    }

}
