package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.List;

public class FunctionNode extends Node {

    private final String name;
    private final Node args;
    private final Node body;

    public FunctionNode(String name, Node args, Node body) {
        this.name = name;
        this.args = args;
        this.body = body;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitFunctionNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return Node.createList(args, body);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.FUNCTIONNODE;
    }

    public String getName() {
        return name;
    }

    public FunctionArgsNode getArgs() {
        return (FunctionArgsNode)args;
    }

    public Node getBody() {
        return body;
    }
}
