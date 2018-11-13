package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

public class FunctionArgsNode extends Node {

    private final List<String> list = new ArrayList<>();

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
        return MoreObjects.toStringHelper(this)
                .add("list", list)
                .toString();
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
