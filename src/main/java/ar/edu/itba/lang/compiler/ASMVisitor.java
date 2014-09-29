package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMVisitor implements NodeVisitor, Opcodes {

    private final ClassWriter cw = new ClassWriter(0);
    private MethodVisitor mv;

    public ASMVisitor(Node root) {
        cw.visit(49,
                ACC_PUBLIC + ACC_SUPER,
                "Main",
                null,
                "java/lang/Object",
                null);

        cw.visitSource("Main.java", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL,
                    "java/lang/Object",
                    "<init>",
                    "()V");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
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
        return cw.toByteArray();
    }

    @Override
    public void visitAddNode(AddNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IADD);
    }

    @Override
    public void visitAndNode(AndNode node) {
        mv.visitInsn(IAND);
    }

    @Override
    public void visitArgsNode(ArgsNode node) {
         for (Node child : node.childNodes()) {
             child.accept(this);
         }
    }

    @Override
    public void visitBlockNode(BlockNode node) {
        for (Node children : node.childNodes()) {
            children.accept(this);
        }
    }

    @Override
    public void visitCallNode(CallNode node) {
        mv.visitFieldInsn(GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;");
        node.getArgs().accept(this);
        mv.visitMethodInsn(INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V");
    }

    @Override
    public void visitDivideNode(DivideNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IDIV);
    }

    @Override
    public void visitFalseNode(FalseNode node) {
        mv.visitInsn(ICONST_0);
    }

    @Override
    public void visitIfNode(IfNode node) {
        Label l = new Label();
        Label l2 = new Label();

        node.getCondition().accept(this);
        mv.visitIntInsn(BIPUSH, 1);
        mv.visitJumpInsn(IF_ICMPNE, l);
        node.getThenBody().accept(this);
        mv.visitLabel(l);
        node.getElseBody();
    }

    @Override
    public void visitLiteralNode(LiteralNode node) {
        Object value = node.getValue();
        mv.visitLdcInsn(value);
    }

    @Override
    public void visitNegateNode(NegateNode node) {
        mv.visitInsn(INEG);
    }

    @Override
    public void visitOrNode(OrNode node) {
        mv.visitInsn(IOR);
    }

    @Override
    public void visitSubstractNode(SubstractNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(ISUB);
    }

    @Override
    public void visitTrueNode(TrueNode node) {
        mv.visitInsn(ICONST_1);
    }

    @Override
    public void visitWhileNode(WhileNode node) {

    }
}
