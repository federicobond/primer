package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class NegateNode extends Node {

    private final Node node;

    public NegateNode(Node node) {
        this.node = node;
    }

    @Override
    public void accept(NodeVisitor visitor) {

    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(node);
    }
}
