package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;

import java.util.ArrayList;
import java.util.List;

public class FlattenBlocksOptimizerVisitor extends NodeVisitorAdapter {

    @Override
    public Node visitBlockNode(BlockNode node) {
        List<Node> list = node.childNodes();
        boolean foundBlock;

        do {
            List<Node> newList = new ArrayList<Node>();
            foundBlock = false;
            for (Node child : list) {
                if (child.getNodeType() == NodeType.BLOCKNODE) {
                    foundBlock = true;
                    newList.addAll(child.childNodes());
                } else {
                    newList.add(child.accept(this));
                }
            }
            list = newList;
        } while (foundBlock);

        return new BlockNode(list);
    }

}
