package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

import java.util.List;

public class IfElseNode extends Node {
    private final Node condition;
    private final Node thenBody;
    private final Node elseBody;

    public IfElseNode(Node condition, Node thenBody, Node elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getThenBody() {
        return thenBody;
    }

    public Node getElseBody() {
        return elseBody;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitIfElseNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(condition, thenBody, elseBody);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.IFNODE;
    }
}
