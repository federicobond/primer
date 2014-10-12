package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

import java.util.List;

public class IfNode extends Node {
    private final Node condition;
    private final Node thenBody;

    public IfNode(Node condition, Node thenBody) {
        this.condition = condition;
        this.thenBody = thenBody;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getThenBody() {
        return thenBody;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitIfNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(condition, thenBody);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.IFNODE;
    }
}
