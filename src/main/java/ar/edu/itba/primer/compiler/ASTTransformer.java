package ar.edu.itba.primer.compiler;

import ar.edu.itba.primer.PrimerParser;
import ar.edu.itba.primer.ast.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ASTTransformer {

    static Node transform(PrimerParser.StatementListContext ctx) {
        ASTTransformer transformer = new ASTTransformer();
        return transformer.visit(ctx);
    }

    private Node visit(PrimerParser.StatementListContext ctx) {
        BlockNode block = new BlockNode();
        for (PrimerParser.StatementContext stmt : ctx.statement()) {
            block.add(visit(stmt));
        }
        return block;
    }

    private Node visit(PrimerParser.StatementContext stmt) {
        if (stmt.assignment() != null) {
            return visit(stmt.assignment());
        }
        if (stmt.breakStatement() != null) {
            return visit(stmt.breakStatement());
        }
        if (stmt.continueStatement() != null) {
            return visit(stmt.continueStatement());
        }
        if (stmt.functionCall() != null) {
            return visit(stmt.functionCall());
        }
        if (stmt.functionDeclaration() != null) {
            return visit(stmt.functionDeclaration());
        }
        if (stmt.ifStatement() != null) {
            return visit(stmt.ifStatement());
        }
        if (stmt.returnStatement() != null) {
            return visit(stmt.returnStatement());
        }
        if (stmt.whileStatement() != null) {
            return visit(stmt.whileStatement());
        }
        if (stmt.variableDeclaration() != null) {
            return visit(stmt.variableDeclaration());
        }
        throw new RuntimeException("unrecognized statement");
    }

    private Node visit(PrimerParser.VariableDeclarationContext variableDeclaration) {
        String name = variableDeclaration.Identifier().getText();
        Node value = visit(variableDeclaration.expression());

        return new DeclarationNode(name, value);
    }

    private Node visit(PrimerParser.ExpressionContext expression) {
        if (expression.variable() != null) {
            return visit(expression.variable());
        }
        if (expression.literal() != null) {
            return visit(expression.literal());
        }
        if (expression.functionCall() != null) {
            return visit(expression.functionCall());
        }
        if (expression.getChildCount() == 2) {
            Node value = visit(expression.expression(0));
            return new NegateNode(value);
        }
        if (expression.getChildCount() == 3) {
            if (expression.expression().size() == 1) {
                return visit(expression.expression(0));
            }

            Node left = visit(expression.expression(0));
            Node right = visit(expression.expression(1));
            String op = expression.getChild(1).getText();

            switch (op) {
                case "+":
                    return new AddNode(left, right);
                case "-":
                    return new SubtractNode(left, right);
                case "*":
                    return new MultiplyNode(left, right);
                case "/":
                    return new DivideNode(left, right);
                case "%":
                    return new ModulusNode(left, right);
                case "&&":
                    return new AndNode(left, right);
                case "||":
                    return new OrNode(left, right);
                case "<":
                    return new LessThanNode(left, right);
                case "<=":
                    return new LessEqualThanNode(left, right);
                case ">":
                    return new GreaterThanNode(left, right);
                case ">=":
                    return new GreaterEqualThanNode(left, right);
                case "==":
                    return new EqualNode(left, right);
                case "!=":
                    return new NotEqualNode(left, right);
            }
        }
        throw new RuntimeException("unrecognized expression");
    }

    private Node visit(PrimerParser.LiteralContext literal) {
        if (literal.IntegerLiteral() != null) {
            int value = Integer.parseInt(literal.IntegerLiteral().getText());
            return new IntegerLiteralNode(value);
        }
        if (literal.StringLiteral() != null) {
            String text = literal.StringLiteral().getText();
            String value = text.substring(1, text.length() - 1);
            return new StringLiteralNode(value);
        }

        String text = literal.getText();
        if (text.equals("true")) {
            return TrueNode.INSTANCE;
        } else if (text.equals("false")) {
            return FalseNode.INSTANCE;
        } else if (text.equals("nil")) {
            return NilNode.INSTANCE;
        }

        throw new RuntimeException("unrecognized literal type");
    }

    private Node visit(PrimerParser.VariableContext variable) {
        String name = variable.Identifier().getText();
        return new VariableNode(name);
    }

    private Node visit(PrimerParser.WhileStatementContext whileStatement) {
        Node condition = visit(whileStatement.expression());
        Node body = visit(whileStatement.statementList());

        return new WhileNode(condition, body);
    }

    private Node visit(PrimerParser.ReturnStatementContext returnStatement) {
        Node value = visit(returnStatement.expression());

        return new ReturnNode(value);
    }

    private Node visit(PrimerParser.IfStatementContext ifStatement) {
        Node condition = visit(ifStatement.expression());
        Node thenBody = visit(ifStatement.statementList(0));

        if (ifStatement.statementList(1) != null) {
            Node elseBody = visit(ifStatement.statementList(1));
            return new IfElseNode(condition, thenBody, elseBody);
        }

        return new IfNode(condition, thenBody);
    }

    private Node visit(PrimerParser.FunctionDeclarationContext functionDeclaration) {
        String name = functionDeclaration.Identifier().getText();
        Node args = visit(functionDeclaration.functionArguments());
        Node body = visit(functionDeclaration.statementList());

        return new FunctionNode(name, args, body);
    }

    private Node visit(PrimerParser.FunctionArgumentsContext functionArguments) {
        FunctionArgsNode args = new FunctionArgsNode();
        for (TerminalNode node : functionArguments.Identifier()) {
            args.add(node.getText());
        }
        return args;
    }

    private Node visit(PrimerParser.FunctionCallContext functionCall) {
        String name = functionCall.Identifier().getText();
        ListNode args = visit(functionCall.argumentList());
        return new CallNode(name, args);
    }

    private ListNode visit(PrimerParser.ArgumentListContext argumentList) {
        ArgsNode args = new ArgsNode();
        for (PrimerParser.ExpressionContext node : argumentList.expression()) {
            args.add(visit(node));
        }
        return args;
    }

    private Node visit(PrimerParser.ContinueStatementContext continueStatement) {
        return ContinueNode.INSTANCE;
    }

    private Node visit(PrimerParser.BreakStatementContext breakStatement) {
        return BreakNode.INSTANCE;
    }

    private Node visit(PrimerParser.AssignmentContext assignment) {
        String name = assignment.Identifier().getText();
        Node value = visit(assignment.expression());

        return new AssignmentNode(name, value);
    }
}
