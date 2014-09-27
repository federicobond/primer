package ar.edu.itba.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class ListNode extends Node {

    private List<Node> list = new ArrayList<Node>();

    public void add(Node node) {
        list.add(node);
    }

    public List<Node> childNodes() {
        return list;
    }
}
