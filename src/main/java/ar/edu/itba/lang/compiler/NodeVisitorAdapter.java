package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;

import java.util.ArrayList;
import java.util.List;

public class NodeVisitorAdapter implements NodeVisitor<Node> {

    @Override
    public Node visitAddNode(AddNode node) {
        return new AddNode(node.getFirstNode().accept(this),
                           node.getSecondNode().accept(this));
    }

    @Override
    public Node visitAndNode(AndNode node) {
        return new AndNode(node.getFirstNode().accept(this),
                node.getSecondNode().accept(this));
    }

    @Override
    public Node visitArgsNode(ArgsNode node) {
        List<Node> newList = new ArrayList<Node>();
        for (Node child : node.childNodes()) {
            newList.add(child.accept(this));
        }
        return new ArgsNode(newList);
    }

    @Override
    public Node visitAssignmentNode(AssignmentNode node) {
        return new AssignmentNode(node.getName(), node.getValue().accept(this));
    }

    @Override
    public Node visitBlockNode(BlockNode node) {
        List<Node> newList = new ArrayList<Node>();
        for (Node child : node.childNodes()) {
            newList.add(child.accept(this));
        }
        return new BlockNode(newList);
    }

    @Override
    public Node visitBreakNode(BreakNode node) {
        return node;
    }

    @Override
    public Node visitCallNode(CallNode node) {
        return new CallNode(node.getName(), (ListNode)node.getArgs().accept(this));
    }

    @Override
    public Node visitContinueNode(ContinueNode node) {
        return node;
    }

    @Override
    public Node visitDeclarationNode(DeclarationNode node) {
        return new DeclarationNode(node.getName(), node.getValue().accept(this));
    }

    @Override
    public Node visitDivideNode(DivideNode node) {
        return new DivideNode(node.getFirstNode().accept(this),
                              node.getSecondNode().accept(this));
    }

    @Override
    public Node visitEqualNode(EqualNode node) {
        return new EqualNode(node.getFirstNode().accept(this),
                             node.getSecondNode().accept(this));
    }

    @Override
    public Node visitFalseNode(FalseNode node) {
        return node;
    }

    @Override
    public Node visitFunctionNode(FunctionNode node) {
        return new FunctionNode(node.getName(),
                node.getArgs().accept(this),
                node.getBody().accept(this));
    }


    @Override
    public Node visitFunctionArgsNode(FunctionArgsNode node) {
        return node;
    }

    @Override
    public Node visitGreaterThanNode(GreaterThanNode node) {
        return new GreaterThanNode(node.getFirstNode().accept(this),
                                   node.getSecondNode().accept(this));
    }

    @Override
    public Node visitGreaterEqualThanNode(GreaterEqualThanNode node) {
        return new GreaterEqualThanNode(node.getFirstNode().accept(this),
                node.getSecondNode().accept(this));

    }
    @Override
    public Node visitIfNode(IfNode node) {
        return new IfNode(node.getCondition().accept(this),
                          node.getThenBody().accept(this));
    }

    @Override
    public Node visitIfElseNode(IfElseNode node) {
        return new IfElseNode(node.getCondition().accept(this),
                              node.getThenBody().accept(this),
                              node.getElseBody().accept(this));
    }

    @Override
    public Node visitLessThanNode(LessThanNode node) {
        return new LessThanNode(node.getFirstNode().accept(this),
                                node.getSecondNode().accept(this));
    }
    @Override
    public Node visitLessEqualThanNode(LessEqualThanNode node) {
        return new LessEqualThanNode(node.getFirstNode().accept(this),
                node.getSecondNode().accept(this));
    }
    @Override
    public Node visitLiteralNode(LiteralNode node) {
        return node;
    }


    @Override
    public Node visitMultiplyNode(MultiplyNode node) {
        return new MultiplyNode(node.getFirstNode().accept(this),
                                node.getSecondNode().accept(this));
    }

    @Override
    public Node visitNegateNode(NegateNode node) {
        return new NegateNode(node.getNode().accept(this));
    }

    @Override
    public Node visitNilNode(NilNode node) {
        return node;
    }

    @Override
    public Node visitNotEqualNode(NotEqualNode node) {
        return new NotEqualNode(node.getFirstNode().accept(this),
                node.getSecondNode().accept(this));
    }

    @Override
    public Node visitModulusNode(ModulusNode node) {
        return new ModulusNode(node.getFirstNode().accept(this),
                node.getSecondNode().accept(this));
    }

    @Override
    public Node visitOrNode(OrNode node) {
        return new OrNode(node.getFirstNode().accept(this),
                          node.getSecondNode().accept(this));
    }

    @Override
    public Node visitReturnNode(ReturnNode node) {
        return new ReturnNode(node.getValue().accept(this));
    }

    @Override
    public Node visitStringLiteralNode(StringLiteralNode node) {
        return node;
    }

    @Override
    public Node visitSubstractNode(SubstractNode node) {
        return new SubstractNode(node.getFirstNode().accept(this),
                                 node.getSecondNode().accept(this));
    }

    @Override
    public Node visitTrueNode(TrueNode node) {
        return node;
    }

    @Override
    public Node visitVariableNode(VariableNode node) {
        return node;
    }

    @Override
    public Node visitWhileNode(WhileNode node) {
        return new WhileNode(node.getConditionNode().accept(this),
                             node.getBodyNode().accept(this));
    }

}
