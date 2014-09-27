package ar.edu.itba.lang.ast;

import java.util.List;

public class WhileNode extends Node {

    private final Node conditionNode;
    private final Node bodyNode;

    public WhileNode(Node conditionNode, Node bodyNode) {
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(conditionNode, bodyNode);
    }
}
