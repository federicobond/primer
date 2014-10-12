package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

import java.util.List;

public class WhileNode extends Node {

    private final Node conditionNode;
    private final Node bodyNode;

    public WhileNode(Node conditionNode, Node bodyNode) {
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
    }

    public Node getBodyNode() {
        return bodyNode;
    }

    public Node getConditionNode() {
        return conditionNode;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitWhileNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(conditionNode, bodyNode);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.WHILENODE;
    }
}
