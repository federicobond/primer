package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.ast.*;
import ar.edu.itba.lang.compiler.NodeVisitor;
import org.objectweb.asm.*;

public class ASMVisitor implements NodeVisitor, Opcodes {

    private final ClassWriter cw = new ClassWriter(0);
    private MethodVisitor mv;

    public ASMVisitor() {
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
            mv.visitIntInsn(BIPUSH, 42);
            mv.visitIntInsn(ISTORE, 2);
            mv.visitFieldInsn(GETSTATIC,
                    "java/lang/System",
                    "out",
                    "Ljava/io/PrintStream;");
            mv.visitLdcInsn("hello");
            mv.visitMethodInsn(INVOKEVIRTUAL,
                    "java/io/PrintStream",
                    "println",
                    "(Ljava/lang/String;)V");
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

    }

    @Override
    public void visitAndNode(AndNode node) {
        mv.visitInsn(IAND);
    }

    @Override
    public void visitBlockNode(BlockNode node) {

    }

    @Override
    public void visitDivideNode(DivideNode node) {

    }

    @Override
    public void visitFalseNode(FalseNode node) {
        mv.visitIntInsn(ISTORE, 0);
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

    }

    @Override
    public void visitTrueNode(TrueNode node) {

    }

    @Override
    public void visitWhileNode(WhileNode node) {

    }
}
