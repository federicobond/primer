package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class FunctionArgsNode extends Node {

    private final List<String> list = new ArrayList<String>();

    public void add(String arg) {
        list.add(arg);
    }

    public List<String> getList() {
        return list;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitFunctionArgsNode(this);
    }

    @Override
    public String getNodeName() {
        return super.getNodeName() + "{list=" + list + "}";
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.FUNCTIONARGSNODE;
    }
}
