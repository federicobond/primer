package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

import java.util.List;

public class BlockNode extends ListNode {

    public BlockNode() {
        super();
    }

    public BlockNode(List<Node> list) {
        super(list);
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitBlockNode(this);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.BLOCKNODE;
    }
}
