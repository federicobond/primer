package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class ArgsNode extends ListNode {

    public ArgsNode() {
        super();
    }

    public ArgsNode(List<Node> list) {
        super(list);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitArgsNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ARGSNODE;
    }
}
