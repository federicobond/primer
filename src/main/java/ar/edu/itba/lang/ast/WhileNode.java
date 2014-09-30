package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

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
    public void accept(NodeVisitor visitor) {
        visitor.visitWhileNode(this);
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
