package ar.edu.itba.primer.compiler;

import ar.edu.itba.primer.ast.*;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class ConstantFoldingVisitorTest {

    private NodeVisitor<Node> visitor = new ConstantFoldingVisitor();

    @Test
    public void constantAdd() {
        assertThat(
                new AddNode(new IntegerLiteralNode(2), new IntegerLiteralNode(2)).accept(visitor),
                IsEqual.<Node>equalTo(new IntegerLiteralNode(4))
        );
    }

    @Test
    public void constantSubtract() {
        assertThat(
                new SubtractNode(new IntegerLiteralNode(3), new IntegerLiteralNode(1)).accept(visitor),
                IsEqual.<Node>equalTo(new IntegerLiteralNode(2))
        );
    }

    @Test
    public void constantMultiply() {
        assertThat(
                new MultiplyNode(new IntegerLiteralNode(3), new IntegerLiteralNode(2)).accept(visitor),
                IsEqual.<Node>equalTo(new IntegerLiteralNode(6))
        );
    }

    @Test
    public void constantDivide() {
        assertThat(
                new DivideNode(new IntegerLiteralNode(4), new IntegerLiteralNode(2)).accept(visitor),
                IsEqual.<Node>equalTo(new IntegerLiteralNode(2))
        );
    }

    @Test
    public void constantModulus() {
        assertThat(
                new ModulusNode(new IntegerLiteralNode(3), new IntegerLiteralNode(2)).accept(visitor),
                IsEqual.<Node>equalTo(new IntegerLiteralNode(1))
        );
    }

    @Test
    public void constantTrueBranch() {
        BlockNode thenBodyNode = new BlockNode();
        thenBodyNode.add(new CallNode("foo", new ArgsNode()));

        Node node = new IfNode(TrueNode.INSTANCE, thenBodyNode).accept(visitor);

        assertThat(node, instanceOf(BlockNode.class));

        BlockNode result = (BlockNode)node;
        assertThat(result.childNodes().size(), equalTo(1));
        assertThat(result.childNodes().get(0), instanceOf(CallNode.class));
        assertThat(((CallNode)result.childNodes().get(0)).getName(), equalTo("foo"));
    }

    @Test
    public void constantFalseBranch() {
        BlockNode thenBodyNode = new BlockNode();
        thenBodyNode.add(new CallNode("foo", new ArgsNode()));

        BlockNode elseBodyNode = new BlockNode();
        elseBodyNode.add(new CallNode("bar", new ArgsNode()));

        Node node = new IfNode(FalseNode.INSTANCE, thenBodyNode).accept(visitor);

        assertThat(node, instanceOf(BlockNode.class));

        BlockNode result = (BlockNode)node;
        assertThat(result.childNodes().size(), equalTo(0));

        node = new IfElseNode(FalseNode.INSTANCE, thenBodyNode, elseBodyNode).accept(visitor);

        assertThat(node, instanceOf(BlockNode.class));

        result = (BlockNode)node;
        assertThat(result.childNodes().size(), equalTo(1));
        assertThat(result.childNodes().get(0), instanceOf(CallNode.class));
        assertThat(((CallNode)result.childNodes().get(0)).getName(), equalTo("bar"));
    }

    @Test
    public void constantAnd() {
        assertThat(
                new AndNode(TrueNode.INSTANCE, TrueNode.INSTANCE).accept(visitor),
                Is.<Node>is(TrueNode.INSTANCE)
        );
        assertThat(
                new AndNode(FalseNode.INSTANCE, TrueNode.INSTANCE).accept(visitor),
                Is.<Node>is(FalseNode.INSTANCE)
        );
        assertThat(
                new AndNode(TrueNode.INSTANCE, FalseNode.INSTANCE).accept(visitor),
                Is.<Node>is(FalseNode.INSTANCE)
        );
        assertThat(
                new AndNode(FalseNode.INSTANCE, FalseNode.INSTANCE).accept(visitor),
                Is.<Node>is(FalseNode.INSTANCE)
        );
    }

    @Test
    public void constantOr() {
        assertThat(
                new OrNode(TrueNode.INSTANCE, TrueNode.INSTANCE).accept(visitor),
                Is.<Node>is(TrueNode.INSTANCE)
        );
        assertThat(
                new OrNode(FalseNode.INSTANCE, TrueNode.INSTANCE).accept(visitor),
                Is.<Node>is(TrueNode.INSTANCE)
        );
        assertThat(
                new OrNode(TrueNode.INSTANCE, FalseNode.INSTANCE).accept(visitor),
                Is.<Node>is(TrueNode.INSTANCE)
        );
        assertThat(
                new OrNode(FalseNode.INSTANCE, FalseNode.INSTANCE).accept(visitor),
                Is.<Node>is(FalseNode.INSTANCE)
        );
    }
}
