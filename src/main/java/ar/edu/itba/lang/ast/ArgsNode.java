package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class ArgsNode extends ListNode {

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitArgsNode(this);
    }
}
