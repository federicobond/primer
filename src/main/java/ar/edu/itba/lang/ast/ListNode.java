package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

public abstract class ListNode extends Node {

    private final List<Node> list;

    public ListNode() {
        list = new ArrayList<Node>();
    }

    public ListNode(List<Node> list) {
        this.list = list;
    }

    public void add(Node node) {
        list.add(0, node);
    }

    public List<Node> childNodes() {
        return list;
    }
}
