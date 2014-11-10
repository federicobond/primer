package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;
import com.google.common.base.MoreObjects;

import java.util.List;

public class StringLiteralNode extends Node {

    private final String value;

    public StringLiteralNode(String value) {
        this.value = value;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitStringLiteralNode(this);
    }

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.STRINGLITERALNODE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringLiteralNode that = (StringLiteralNode) o;

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

    public String getValue() {
        return value;
    }
}
