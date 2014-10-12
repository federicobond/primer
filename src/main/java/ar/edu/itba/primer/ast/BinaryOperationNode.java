package ar.edu.itba.primer.ast;

import java.util.List;

public abstract class BinaryOperationNode extends Node {

    private final Node first;
    private final Node second;

    protected BinaryOperationNode(Node first, Node second) {
        this.first = first;
        this.second = second;
    }

    public Node getFirstNode() {
        return first;
    }

    public Node getSecondNode() {
        return second;
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(first, second);
    }
}
