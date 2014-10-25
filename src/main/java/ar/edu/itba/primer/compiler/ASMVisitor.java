package ar.edu.itba.primer.compiler;

import ar.edu.itba.primer.Kernel;
import ar.edu.itba.primer.ast.*;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

public class ASMVisitor implements NodeVisitor<Void>, Opcodes {

    private static final String MAIN_CLASS = "Main";

    private final String fileName;

    private ClassVisitor cv;
    private GeneratorAdapter mv;

    private Context context = Context.rootContext();

    public ASMVisitor(ClassVisitor cv, String fileName) {
        this.cv = cv;
        this.fileName = fileName;

        initializeSymbols();
    }

    private void initializeSymbols() {
        java.lang.reflect.Method[] methods = Kernel.class.getMethods();
        for (java.lang.reflect.Method m : methods) {
            context.setFunction(m.getName(), Type.getType(m), Type.getInternalName(Kernel.class));
        }
    }

    public byte[] getByteArray() {
        if (cv instanceof ClassWriter) {
            return ((ClassWriter) cv).toByteArray();
        }
        throw new IllegalArgumentException();
    }

    public ASMVisitor start(Node root) {
        visitRootNode(root);
        return this;
    }

    private void visitRootNode(Node root) {
        cv.visit(V1_5,
                ACC_PUBLIC + ACC_SUPER,
                MAIN_CLASS,
                null,
                "java/lang/Object",
                null);

        cv.visitSource(fileName, null);

        {
            Method m = Method.getMethod("void main (String[])");
            mv = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, m, null, null, cv);

            root.accept(this);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0); /* computed automatically */
            mv.visitEnd();
        }
        cv.visitEnd();
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
    public Void visitAssignmentNode(AssignmentNode node) {
        if (!context.hasName(node.getName())) {
            throw new ScriptException("variable " + node.getName() + " is not defined");
        }

        node.getValue().accept(this);

        // We will assume it is a reference for now.
        // TODO: check this
        mv.visitIntInsn(ASTORE, context.getVariable(node.getName()).getIndex());

        return null;
    }

    @Override
    public Void visitBlockNode(BlockNode node) {
        for (Node child : node.childNodes()) {
            child.accept(this);
            if (child instanceof CallNode) {
                Type type = getCalledMethod((CallNode)child).getType();
                if (!type.getReturnType().equals(Type.getType(Void.TYPE))) {
                    // Discard result
                    mv.visitInsn(POP);
                }
            }
        }
        return null;
    }

    @Override
    public Void visitBreakNode(BreakNode node) {
        if (!context.isLoop()) {
            throw new ScriptException("nowhere to break to");
        }
        mv.visitJumpInsn(GOTO, context.breakLabel());

        return null;
    }

    @Override
    public Void visitCallNode(CallNode node) {
        node.getArgs().accept(this);

        FunctionSymbol sym = getCalledMethod(node);

        mv.visitMethodInsn(INVOKESTATIC,
                sym.getContainer(),
                node.getName(),
                sym.getType().getDescriptor(),
                false);

        return null;
    }

    @Override
    public Void visitContinueNode(ContinueNode node) {
        if (!context.isLoop()) {
            throw new ScriptException("nowhere to continue to");
        }
        mv.visitJumpInsn(GOTO, context.continueLabel());
        return null;
    }

    @Override
    public Void visitDeclarationNode(DeclarationNode node) {
        if (context.hasName(node.getName())) {
            throw new ScriptException("variable " + node.getName() + " is already defined");
        }

        int index = context.setVariable(node.getName());

        node.getValue().accept(this);

        // We will assume it is a string for now.
        mv.visitIntInsn(ASTORE, index);
        return null;
    }

    private FunctionSymbol getCalledMethod(CallNode node) {
        if (!context.hasName(node.getName())) {
            throw new ScriptException("undefined method " + node.getName());
        }
        return context.getFunction(node.getName());
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
    public Void visitNotEqualNode(NotEqualNode node) {
        Label l1 = new Label();
        Label l2 = new Label();

        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitJumpInsn(IFEQ, l1);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, l2);
        mv.visitLabel(l1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(l2);

        return null;
    }

    @Override
    public Void visitModulusNode(ModulusNode node) {
        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitInsn(IREM);

        return null;
    }

    @Override
    public Void visitFunctionNode(FunctionNode node) {
        GeneratorAdapter old = mv;

        Type[] args = new Type[node.getArgs().getList().size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = Type.getType(Object.class);
        }

        String name = node.getName();
        Type type = Type.getMethodType(Type.getType(Object.class), args);

        if (context.hasName(name)) {
            throw new ScriptException("symbol " + name + " already defined");
        }
        context.setFunction(name, type, MAIN_CLASS);

        Method m = new Method(name, type.getDescriptor());
        mv = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, m, null, null, cv);

        Node body = node.getBody();
        Node lastInstruction = body.childNodes().get(body.childNodes().size() - 1);

        context = Context.childOf(context);
        body.accept(this);
        context = context.parentContext();

        if (!(lastInstruction instanceof ReturnNode)) {
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
        }

        mv.visitMaxs(0, 0);

        mv.visitEnd();

        mv = old;

        return null;
    }


    @Override
    public Void visitFunctionArgsNode(FunctionArgsNode node) {
        return null;
    }

    @Override
    public Void visitFalseNode(FalseNode node) {
        mv.visitInsn(ICONST_0);

        return null;
    }

    @Override
    public Void visitGreaterEqualThanNode(GreaterEqualThanNode node) {
        Label l1 = new Label();
        Label l2 = new Label();

        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitJumpInsn(IF_ICMPLT, l1);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, l2);
        mv.visitLabel(l1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(l2);

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
    public Void visitLessEqualThanNode(LessEqualThanNode node) {
        Label l1 = new Label();
        Label l2 = new Label();

        node.getFirstNode().accept(this);
        node.getSecondNode().accept(this);
        mv.visitJumpInsn(IF_ICMPGT, l1);
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
    public Void visitNilNode(NilNode node) {
        mv.visitInsn(ACONST_NULL);

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
    public Void visitReturnNode(ReturnNode node) {
        Node value = node.getValue();

        if (value == null) {
            mv.visitInsn(ACONST_NULL);
        } else {
            value.accept(this);
        }
        mv.visitInsn(ARETURN);

        return null;
    }

    @Override
    public Void visitStringLiteralNode(StringLiteralNode node) {
        String value = node.getValue();
        mv.visitLdcInsn(value);

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
    public Void visitVariableNode(VariableNode node) {
        if (!context.hasName(node.getName())) {
            throw new ScriptException("variable " + node.getName() + " is not defined");
        }

        mv.visitIntInsn(ALOAD, context.getVariable(node.getName()).getIndex());

        return null;
    }

    @Override
    public Void visitWhileNode(WhileNode node) {
        Label conditionLabel = new Label();
        Label endLabel = new Label();

        context.pushLoop(conditionLabel, endLabel);

        mv.visitLabel(conditionLabel);
        node.getConditionNode().accept(this);
        mv.visitJumpInsn(IFEQ, endLabel);
        node.getBodyNode().accept(this);
        mv.visitJumpInsn(GOTO, conditionLabel);
        mv.visitLabel(endLabel);

        context.popLoop();

        return null;
    }
}
