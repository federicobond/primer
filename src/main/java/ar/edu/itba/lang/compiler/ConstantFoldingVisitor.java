package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;

import java.util.ArrayList;
import java.util.List;

public class ConstantFoldingVisitor extends NodeVisitorAdapter {

    @Override
    public Node visitBlockNode(BlockNode node) {
        List<Node> list = node.childNodes();
        List<Node> newList = new ArrayList<Node>();

        for (Node child : list) {
            child = child.accept(this);
            if (child instanceof BlockNode) {
                newList.addAll(child.childNodes());
            } else {
                newList.add(child);
            }
        }

        return new BlockNode(newList);
    }

    @Override
    public Node visitAndNode(AndNode node) {
        Node firstNode = node.getFirstNode().accept(this);
        Node secondNode = node.getSecondNode().accept(this);

        boolean firstTrue = firstNode.getNodeType().isAlwaysTrue();
        boolean firstFalse = firstNode.getNodeType().isAlwaysFalse();
        boolean secondTrue = secondNode.getNodeType().isAlwaysTrue();
        boolean secondFalse = secondNode.getNodeType().isAlwaysFalse();

        if (firstFalse || secondFalse) {
            return new FalseNode();
        } else if (firstTrue && secondTrue) {
            return new TrueNode();
        } else if (firstTrue) {
            return secondNode;
        } else if (secondTrue) {
            return firstNode;
        }
        return super.visitAndNode(node);
    }

    @Override
    public Node visitOrNode(OrNode node) {
        Node firstNode = node.getFirstNode().accept(this);
        Node secondNode = node.getSecondNode().accept(this);

        boolean firstTrue = firstNode.getNodeType().isAlwaysTrue();
        boolean firstFalse = firstNode.getNodeType().isAlwaysFalse();
        boolean secondTrue = secondNode.getNodeType().isAlwaysTrue();
        boolean secondFalse = secondNode.getNodeType().isAlwaysFalse();

        if (firstFalse && secondFalse) {
            return new FalseNode();
        } else if (firstTrue || secondTrue) {
            return new TrueNode();
        } else if (firstFalse) {
            return secondNode;
        } else if (secondFalse) {
            return firstNode;
        }
        return super.visitOrNode(node);
    }

    @Override
    public Node visitIfNode(IfNode node) {
        Node condition = node.getCondition().accept(this);
        if (condition.getNodeType().isAlwaysTrue()) {
            return node.getThenBody().accept(this);
        } else if (condition.getNodeType().isAlwaysFalse()) {
            return new BlockNode(); /* empty block */
        }
        return new IfNode(condition, node.getThenBody());
    }

    @Override
    public Node visitIfElseNode(IfElseNode node) {
        Node condition = node.getCondition().accept(this);
        if (condition.getNodeType().isAlwaysTrue()) {
            return node.getThenBody().accept(this);
        } else if (node.getCondition().getNodeType().isAlwaysFalse()) {
            return node.getElseBody().accept(this);
        }
        return new IfElseNode(condition, node.getThenBody(), node.getElseBody());
    }
}
