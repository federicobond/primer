package ar.edu.itba.primer.ast;

import ar.edu.itba.primer.compiler.NodeVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Node {

    static final List<Node> EMPTY_LIST = new ArrayList<>(0);

    public abstract <T> T accept(NodeVisitor<T> visitor);

    public abstract List<Node> childNodes();

    protected static List<Node> createList(Node... nodes) {
        List<Node> list = new ArrayList<>();

        Collections.addAll(list, nodes);

        return list;
    }

    public abstract NodeType getNodeType();

    public String getNodeName() {
        return getClass().getSimpleName();
    }

    public String toString() {
        return toString(true, 0);
    }

    public String toString(boolean indent, int indentation) {
        StringBuilder sb = new StringBuilder();

        if (indent) {
            indent(indentation, sb);
        }

        sb.append(getNodeName());
        sb.append('\n');

        for (Node node : childNodes()) {
            sb.append(node.toString(indent, indentation + 1));
        }
        return sb.toString();
    }

    private void indent(int indentation, StringBuilder sb) {
        for (int i = 0; i < indentation; i++) {
            sb.append("  ");
        }
    }
}
