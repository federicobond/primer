package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class DeclarationNode extends Node {

    private final Node value;
    private final String name;

    public DeclarationNode(String name, Node value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitDeclarationNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(value);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.DECLARATIONNODE;
    }

    public String getName() {
        return name;
    }

    public Node getValue() {
        return value;
    }
}
