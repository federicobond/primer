package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class CallNode extends Node {

    private final String name;
    private final ListNode args;

    public CallNode(String name, ListNode args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public ListNode getArgs() {
        return args;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visitCallNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(args);
    }

    @Override
    public String getNodeName() {
        return super.getNodeName() + String.format("{name=%s}", name);
    }
}
