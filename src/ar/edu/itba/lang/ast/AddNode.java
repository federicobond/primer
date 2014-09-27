package ar.edu.itba.lang.ast;

import org.objectweb.asm.MethodVisitor;

public class AddNode extends BinaryOperationNode {

    public AddNode(Node first, Node second) {
        super(first, second);
    }
}
