package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;

public class ConstantOptimizerVisitor extends NodeVisitorAdapter {

    @Override
    public Node visitAndNode(AndNode node) {
        if (node.getFirstNode().getNodeType().isAlwaysFalse()
            || node.getSecondNode().getNodeType().isAlwaysFalse()) {
            return new FalseNode();
        } else if (node.getFirstNode().getNodeType().isAlwaysTrue()
                && node.getSecondNode().getNodeType().isAlwaysTrue()) {
            return new TrueNode();
        }
        return super.visitAndNode(node);
    }

    @Override
    public Node visitOrNode(OrNode node) {
        if (node.getFirstNode().getNodeType().isAlwaysTrue()
                || node.getSecondNode().getNodeType().isAlwaysTrue()) {
            return new TrueNode();
        } else if (node.getFirstNode().getNodeType().isAlwaysFalse()
                && node.getSecondNode().getNodeType().isAlwaysFalse()) {
            return new FalseNode();
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
