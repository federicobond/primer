package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.Kernel;
import ar.edu.itba.lang.ast.*;
import org.objectweb.asm.*;
import org.objectweb.asm.signature.SignatureWriter;

import java.lang.reflect.Method;

public class ASMVisitor implements NodeVisitor<Void>, Opcodes {

    private ClassVisitor cw;
    private MethodVisitor mv;

    public ASMVisitor(Node root, ClassVisitor cw) {
        this.cw = cw;

        cw.visit(49,
                ACC_PUBLIC + ACC_SUPER,
                "Main",
                null,
                "java/lang/Object",
                null);

        cw.visitSource("Main.java", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
                    "main",
                    "([Ljava/lang/String;)V",
                    null,
                    null);
            root.accept(this);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
    }

    public byte[] getByteArray() {
        if (cw instanceof ClassWriter) {
            return ((ClassWriter)cw).toByteArray();
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Void visitAddNode(AddNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IADD);

        return null;
    }

    @Override
    public Void visitAndNode(AndNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IAND);

        return null;
    }

    @Override
    public Void visitArgsNode(ArgsNode node) {
        for (Node child : node.childNodes()) {
            child.accept(this);
        }
        return null;
    }

    @Override
    public Void visitBlockNode(BlockNode node) {
        for (Node child : node.childNodes()) {
            child.accept(this);
        }
        return null;
    }

    @Override
    public Void visitCallNode(CallNode node) {
        SignatureWriter signatureWriter = new SignatureWriter();

        int numArgs = node.getArgs().childNodes().size();
        signatureWriter.visitParameterType();
        for (int i = 0; i < numArgs; i++) {
            signatureWriter.visitClassType("java/lang/Object");
        }
        signatureWriter.visitEnd();
        signatureWriter.visitReturnType();
        signatureWriter.visitClassType("java/lang/Object");
        signatureWriter.visitEnd();


        Method method = null;
        Method[] methods = Kernel.class.getMethods();

        for (Method m : methods) {
            if (m.getName().equals(node.getName())) {
                method = m;
            }
        }

        if (method == null) {
            throw new Script.ScriptException("Undefined method: " + node.getName());
        }

        node.getArgs().accept(this);
        mv.visitMethodInsn(INVOKESTATIC,
                Type.getInternalName(Kernel.class),
                node.getName(),
                Type.getMethodDescriptor(method),
                false);

        if (method.getReturnType() != Void.TYPE) {
            // Discard result for now
            mv.visitInsn(POP);
        }

        return null;
    }

    @Override
    public Void visitDivideNode(DivideNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IDIV);

        return null;
    }

    @Override
    public Void visitEqualNode(EqualNode node) {
        Label l1 = new Label();
        Label l2 = new Label();

        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitJumpInsn(IF_ICMPNE, l1);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, l2);
        mv.visitLabel(l1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(l2);

        return null;
    }

    @Override
    public Void visitFalseNode(FalseNode node) {
        mv.visitInsn(ICONST_0);

        return null;
    }

    @Override
    public Void visitGreaterThanNode(GreaterThanNode node) {
        Label l1 = new Label();
        Label l2 = new Label();

        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitJumpInsn(IF_ICMPLE, l1);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, l2);
        mv.visitLabel(l1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(l2);

        return null;
    }

    @Override
    public Void visitIfNode(IfNode node) {
        Label l1 = new Label();

        node.getCondition().accept(this);
        mv.visitJumpInsn(IFEQ, l1);
        node.getThenBody().accept(this);
        mv.visitLabel(l1);

        return null;
    }

    @Override
    public Void visitIfElseNode(IfElseNode node) {
        Label l1 = new Label();
        Label l2 = new Label();

        node.getCondition().accept(this);
        mv.visitJumpInsn(IFEQ, l1);
        node.getThenBody().accept(this);
        mv.visitJumpInsn(GOTO, l2);
        mv.visitLabel(l1);
        node.getElseBody().accept(this);
        mv.visitLabel(l2);

        return null;
    }

    @Override
    public Void visitLessThanNode(LessThanNode node) {
        Label l1 = new Label();
        Label l2 = new Label();

        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitJumpInsn(IF_ICMPGE, l1);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, l2);
        mv.visitLabel(l1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(l2);

        return null;
    }

    @Override
    public Void visitLiteralNode(LiteralNode node) {
        Object value = node.getValue();
        mv.visitLdcInsn(value);

        return null;
    }

    @Override
    public Void visitMultiplyNode(MultiplyNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IMUL);

        return null;
    }

    @Override
    public Void visitNegateNode(NegateNode node) {
        node.getNode().accept(this);
        mv.visitInsn(INEG);

        return null;
    }

    @Override
    public Void visitOrNode(OrNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IOR);

        return null;
    }

    @Override
    public Void visitSubstractNode(SubstractNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(ISUB);

        return null;
    }

    @Override
    public Void visitTrueNode(TrueNode node) {
        mv.visitInsn(ICONST_1);

        return null;
    }

    @Override
    public Void visitWhileNode(WhileNode node) {
        Label conditionLabel = new Label();
        Label endLabel = new Label();

        mv.visitLabel(conditionLabel);
        node.getConditionNode().accept(this);
        mv.visitJumpInsn(IFEQ, endLabel);
        node.getBodyNode().accept(this);
        mv.visitJumpInsn(GOTO, conditionLabel);
        mv.visitLabel(endLabel);

        return null;
    }
}
