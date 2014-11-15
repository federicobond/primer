package ar.edu.itba.primer.compiler;

import ar.edu.itba.primer.ast.*;

import java.util.ArrayList;
import java.util.List;

public class ConstantFoldingVisitor extends NodeVisitorAdapter {

    @Override
    public Node visitBlockNode(BlockNode node) {
        List<Node> list = node.childNodes();
        List<Node> newList = new ArrayList<>();

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
            return FalseNode.INSTANCE;
        }

        if (firstTrue && secondTrue) {
            return TrueNode.INSTANCE;
        }

        if (firstTrue) {
            return secondNode;
        }

        if (secondTrue) {
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
            return FalseNode.INSTANCE;
        }

        if (firstTrue || secondTrue) {
            return TrueNode.INSTANCE;
        }

        if (firstFalse) {
            return secondNode;
        }

        if (secondFalse) {
            return firstNode;
        }

        return super.visitOrNode(node);
    }

    @Override
    public Node visitIfNode(IfNode node) {
        Node condition = node.getCondition().accept(this);

        if (condition.getNodeType().isAlwaysTrue()) {
            return node.getThenBody().accept(this);
        }

        if (condition.getNodeType().isAlwaysFalse()) {
            return new BlockNode(); /* empty block */
        }

        Node thenBody = node.getThenBody().accept(this);
        return new IfNode(condition, thenBody);
    }

    @Override
    public Node visitIfElseNode(IfElseNode node) {
        Node condition = node.getCondition().accept(this);

        if (condition.getNodeType().isAlwaysTrue()) {
            return node.getThenBody().accept(this);
        }

        if (node.getCondition().getNodeType().isAlwaysFalse()) {
            return node.getElseBody().accept(this);
        }

        Node thenBody = node.getThenBody().accept(this);
        Node elseBody = node.getElseBody().accept(this);
        return new IfElseNode(condition, thenBody, elseBody);
    }

    @Override
    public Node visitAddNode(AddNode node) {
        int[] nums = visitIntegerOperators(node);
        if (nums != null) {
            return new IntegerLiteralNode(nums[0] + nums[1]);
        }
        return new AddNode(node.getFirstNode(), node.getSecondNode());
    }

    @Override
    public Node visitSubtractNode(SubtractNode node) {
        int[] nums = visitIntegerOperators(node);
        if (nums != null) {
            return new IntegerLiteralNode(nums[0] - nums[1]);
        }
        return new SubtractNode(node.getFirstNode(), node.getSecondNode());
    }

    @Override
    public Node visitMultiplyNode(MultiplyNode node) {
        int[] nums = visitIntegerOperators(node);
        if (nums != null) {
            return new IntegerLiteralNode(nums[0] * nums[1]);
        }
        return new MultiplyNode(node.getFirstNode(), node.getSecondNode());
    }

    @Override
    public Node visitDivideNode(DivideNode node) {
        int[] nums = visitIntegerOperators(node);
        if (nums != null) {
            return new IntegerLiteralNode(nums[0] / nums[1]);
        }
        return new DivideNode(node.getFirstNode(), node.getSecondNode());
    }

    @Override
    public Node visitModulusNode(ModulusNode node) {
        int[] nums = visitIntegerOperators(node);
        if (nums != null) {
            return new IntegerLiteralNode(nums[0] % nums[1]);
        }
        return new ModulusNode(node.getFirstNode(), node.getSecondNode());
    }

    private int[] visitIntegerOperators(BinaryOperationNode node) {
        Node first = node.getFirstNode().accept(this);
        Node second = node.getSecondNode().accept(this);

        if (first instanceof IntegerLiteralNode &&
                second instanceof IntegerLiteralNode) {

            int left = ((IntegerLiteralNode)first).getValue();
            int right = ((IntegerLiteralNode)second).getValue();

            return new int[] {left, right};
        }
        return null;
    }
}
