package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class CallNode extends Node {

    private final String name;
    private final Node args;

    public CallNode(String name, Node args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public void accept(NodeVisitor visitor) {

    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public String getNodeName() {
        return super.getNodeName() + String.format("{name=%s}", name);
    }
}
