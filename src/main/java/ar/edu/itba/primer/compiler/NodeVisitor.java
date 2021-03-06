package ar.edu.itba.primer.compiler;

import ar.edu.itba.primer.ast.*;

public interface NodeVisitor<T> {

    public T visitAddNode(AddNode node);
    public T visitAndNode(AndNode node);
    public T visitArgsNode(ArgsNode node);
    public T visitAssignmentNode(AssignmentNode node);
    public T visitBlockNode(BlockNode node);
    public T visitBreakNode(BreakNode node);
    public T visitCallNode(CallNode node);
    public T visitContinueNode(ContinueNode node);
    public T visitDeclarationNode(DeclarationNode node);
    public T visitDivideNode(DivideNode node);
    public T visitEqualNode(EqualNode node);
    public T visitFalseNode(FalseNode node);
    public T visitFunctionArgsNode(FunctionArgsNode node);
    public T visitFunctionNode(FunctionNode node);
    public T visitGreaterEqualThanNode(GreaterEqualThanNode node);
    public T visitGreaterThanNode(GreaterThanNode node);
    public T visitIfNode(IfNode node);
    public T visitIfElseNode(IfElseNode node);
    public T visitLessThanNode(LessThanNode node);
    public T visitLessEqualThanNode(LessEqualThanNode node);
    public T visitIntegerLiteralNode(IntegerLiteralNode node);
    public T visitModulusNode(ModulusNode modulusNode);
    public T visitMultiplyNode(MultiplyNode node);
    public T visitNegateNode(NegateNode node);
    public T visitNilNode(NilNode node);
    public T visitNotEqualNode(NotEqualNode notEqualNode);
    public T visitOrNode(OrNode node);
    public T visitReturnNode(ReturnNode node);
    public T visitStringLiteralNode(StringLiteralNode node);
    public T visitSubtractNode(SubtractNode node);
    public T visitTrueNode(TrueNode node);
    public T visitVariableNode(VariableNode node);
    public T visitWhileNode(WhileNode node);
}
