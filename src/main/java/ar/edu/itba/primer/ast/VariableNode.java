package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;
import com.google.common.base.MoreObjects;

import java.util.List;

public class VariableNode extends Node {

    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitVariableNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.VARIABLENODE;
    }

    @Override
    public String getNodeName() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }

    public String getName() {
        return name;
    }
}
