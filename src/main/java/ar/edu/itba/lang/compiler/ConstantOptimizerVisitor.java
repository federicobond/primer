package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;

public class ConstantOptimizerVisitor extends NodeVisitorAdapter {

    @Override
    public Node visitAndNode(AndNode node) {
        boolean firstTrue = node.getFirstNode().getNodeType().isAlwaysTrue();
        boolean firstFalse = node.getFirstNode().getNodeType().isAlwaysFalse();
        boolean secondTrue = node.getSecondNode().getNodeType().isAlwaysTrue();
        boolean secondFalse = node.getSecondNode().getNodeType().isAlwaysFalse();

        if (firstFalse || secondFalse) {
            return new FalseNode();
        } else if (firstTrue && secondTrue) {
            return new TrueNode();
        } else if (firstTrue) {
            return node.getSecondNode().accept(this);
        } else if (secondTrue) {
            return node.getFirstNode().accept(this);
        }
        return super.visitAndNode(node);
    }

    @Override
    public Node visitOrNode(OrNode node) {
        boolean firstTrue = node.getFirstNode().getNodeType().isAlwaysTrue();
        boolean firstFalse = node.getFirstNode().getNodeType().isAlwaysFalse();
        boolean secondTrue = node.getSecondNode().getNodeType().isAlwaysTrue();
        boolean secondFalse = node.getSecondNode().getNodeType().isAlwaysFalse();

        if (firstFalse && secondFalse) {
            return new FalseNode();
        } else if (firstTrue || secondTrue) {
            return new TrueNode();
        } else if (firstFalse) {
            return node.getSecondNode().accept(this);
        } else if (secondFalse) {
            return node.getFirstNode().accept(this);
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
