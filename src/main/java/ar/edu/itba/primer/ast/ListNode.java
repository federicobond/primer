package ar.edu.itba.primer.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class ListNode extends Node {

    private final List<Node> list;

    public ListNode() {
        list = new ArrayList<>();
    }

    ListNode(List<Node> list) {
        this.list = list;
    }

    public void add(Node node) {
        list.add(node);
    }

    public List<Node> childNodes() {
        return list;
    }
}
