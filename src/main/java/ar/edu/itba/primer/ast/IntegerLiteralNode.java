package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;
import com.google.common.base.MoreObjects;

import java.util.List;

public class IntegerLiteralNode extends Node {

    private final Integer value;

    public IntegerLiteralNode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitIntegerLiteralNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.INTEGERLITERALNODE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntegerLiteralNode that = (IntegerLiteralNode) o;

        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String getNodeName() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .toString();
    }
}
