package ar.edu.itba.lang.ast;

import java.util.List;

public class IfNode extends Node {
    private final Node condition;
    private final Node thenBody;
    private final Node elseBody;

    public IfNode(Node condition, Node thenBody, Node elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    @Override
    public List<Node> childNodes() {
        if (elseBody == null) {
            return Node.createList(condition, thenBody);
        }
        return Node.createList(condition, thenBody, elseBody);
    }
}
