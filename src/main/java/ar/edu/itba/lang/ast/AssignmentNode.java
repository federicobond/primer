package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class AssignmentNode extends Node {

    private final String name;
    private final Node value;

    public AssignmentNode(String name, Node value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitAssignmentNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(value);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ASSIGNMENTNODE;
    }

    public String getName() {
        return name;
    }

    public Node getValue() {
        return value;
    }
}
