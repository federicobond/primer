package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class BlockNode extends ListNode {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitBlockNode(this);
    }
}
