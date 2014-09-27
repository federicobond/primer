package ar.edu.itba.lang.ast;

import java.util.List;

public class FalseNode extends Node {

    @Override
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }
}
