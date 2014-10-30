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
            Method m = Method.getMethod("void main (java.util.ArrayList)");
            mv = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, m, null, null, cv);

            /* declare args parameter as local */
            context.setVariable("ARGV");

            Label start = new Label();
            mv.visitLabel(start);

            root.accept(this);
            mv.visitInsn(RETURN);

            Label end = new Label();
            mv.visitLabel(end);

            for (VariableSymbol var : context.localVariables()) {
                mv.visitLocalVariable(var.getName(), Type.getDescriptor(Object.class), Type.getDescriptor(Object.class), start, end, var.getIndex());
            }

            mv.visitMaxs(0, 0); /* computed automatically */
            mv.visitEnd();


            m = Method.getMethod("void main (String[])");
            mv = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, m, null, null, cv);

            start = new Label();

            mv.visitLabel(start);
            mv.visitTypeInsn(NEW, "java/util/ArrayList");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL,
                    "java/util/ArrayList",
                    "<init>",
                    "()V",
                    false
            );
            mv.visitIntInsn(ASTORE, 1);
            mv.visitIntInsn(ALOAD, 1);
            mv.visitIntInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC,
                    "java/util/Collections",
                    "addAll",
                    "(Ljava/util/Collection;[Ljava/lang/Object;)Z",
                    false
            );
            mv.visitInsn(POP);
            mv.visitIntInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC,
                    "Main",
                    "main",
                    "(Ljava/util/ArrayList;)V",
                    false);
            mv.visitInsn(RETURN);

            end = new Label();
            mv.visitLabel(end);

            mv.visitLocalVariable("args", Type.getDescriptor(Object.class), Type.getDescriptor(Object.class), start, end, 0);
            mv.visitLocalVariable("list", Type.getDescriptor(Object.class), Type.getDescriptor(Object.class), start, end, 1);
            mv.visitMaxs(0, 0);
            mv.visitEnd();

        }
        cv.visitEnd();
    }

    @Override
    public Void visitAddNode(AddNode node) {
        visitIntegerOperands(node.getFirstNode(), node.getSecondNode());
        mv.visitInsn(IADD);

        return null;
    }

    private void visitIntegerOperands(Node first, Node second) {
        first.accept(this);
        unbox(first, Type.INT_TYPE);

        second.accept(this);
        unbox(second, Type.INT_TYPE);
    }

    @Override
    public Void visitAndNode(AndNode node) {
        visitBooleanOperands(node.getFirstNode(), node.getSecondNode());
        mv.visitInsn(IAND);

        return null;
    }

    private void visitBooleanOperands(Node first, Node second) {
        first.accept(this);
        unbox(first, Type.BOOLEAN_TYPE);

        second.accept(this);
        unbox(second, Type.BOOLEAN_TYPE);
    }

    @Override
    public Void visitArgsNode(ArgsNode node) {
        for (Node child : node.childNodes()) {
            child.accept(this);
            box(child);
        }
        return null;
    }

    @Override
    public Void visitAssignmentNode(AssignmentNode node) {
        if (!context.hasVariableName(node.getName())) {
            throw new ScriptException("variable " + node.getName() + " is not defined");
        }

        Node value = node.getValue();

        value.accept(this);
        box(value);

        mv.visitIntInsn(ASTORE, context.getVariable(node.getName()).getIndex());

        return null;
    }

    @Override
    public Void visitBlockNode(BlockNode node) {
        for (Node child : node.childNodes()) {
            child.accept(this);
            if (child instanceof CallNode) {
                // Discard result
                mv.visitInsn(POP);
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
        FunctionSymbol sym = getCalledMethod(node);

        int arity = sym.getType().getArgumentTypes().length;

        if (node.getArgs().childNodes().size() != arity) {
            throw new ScriptException("wrong number of arguments in " + node.getName());
        }

        node.getArgs().accept(this);

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
        if (context.hasVariableName(node.getName())) {
            throw new ScriptException("variable " + node.getName() + " is already defined");
        }

        int index = context.setVariable(node.getName());

        Node value = node.getValue();
        value.accept(this);
        box(value);

        // We will assume it is a string for now.
        mv.visitIntInsn(ASTORE, index);
        return null;
    }

    private FunctionSymbol getCalledMethod(CallNode node) {
        if (!context.hasFunctionName(node.getName())) {
            throw new ScriptException("undefined method " + node.getName());
        }
        return context.getFunction(node.getName());
    }

    @Override
    public Void visitDivideNode(DivideNode node) {
        visitIntegerOperands(node.getFirstNode(), node.getSecondNode());
        mv.visitInsn(IDIV);

        return null;
    }

    @Override
    public Void visitEqualNode(EqualNode node) {
        visitReferenceOperands(node.getFirstNode(), node.getSecondNode());

        mv.visitMethodInsn(INVOKESTATIC,
                "java/util/Objects",
                "equals",
                "(Ljava/lang/Object;Ljava/lang/Object;)Z",
                false);

        return null;
    }

    private void visitReferenceOperands(Node first, Node second) {
        first.accept(this);
        box(first);

        second.accept(this);
        box(second);
    }


    @Override
    public Void visitNotEqualNode(NotEqualNode node) {
        Label l1 = new Label();
        Label l2 = new Label();

        visitReferenceOperands(node.getFirstNode(), node.getSecondNode());

        mv.visitMethodInsn(INVOKESTATIC,
                "java/util/Objects",
                "equals",
                "(Ljava/lang/Object;Ljava/lang/Object;)Z",
                false);
        mv.visitJumpInsn(IFNE, l1);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, l2);
        mv.visitLabel(l1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(l2);

        return null;
    }

    @Override
    public Void visitModulusNode(ModulusNode node) {
        visitIntegerOperands(node.getFirstNode(), node.getSecondNode());
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

        if (context.hasFunctionName(name)) {
            throw new ScriptException("symbol " + name + " already defined");
        }
        context.setFunction(name, type, MAIN_CLASS);

        Method m = new Method(name, type.getDescriptor());
        mv = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, m, null, null, cv);

        Node body = node.getBody();
        Node lastInstruction = body.childNodes().get(body.childNodes().size() - 1);

        context = Context.childOf(context);

        Label start = new Label();
        Label end = new Label();

        mv.visitLabel(start);

        for (String arg : node.getArgs().getList()) {
            context.setVariable(arg);
        }

        body.accept(this);
        mv.visitLabel(end);

        if (!(lastInstruction instanceof ReturnNode)) {
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
        }

        for (VariableSymbol var : context.localVariables()) {
            mv.visitLocalVariable(var.getName(), Type.getDescriptor(Object.class), Type.getDescriptor(Object.class), start, end, var.getIndex());
        }

        mv.visitMaxs(0, 0);

        mv.visitEnd();

        context = context.parentContext();

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

        visitIntegerOperands(node.getFirstNode(), node.getSecondNode());
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

        visitIntegerOperands(node.getFirstNode(), node.getSecondNode());
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

        visitIntegerOperands(node.getFirstNode(), node.getSecondNode());
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

        visitIntegerOperands(node.getFirstNode(), node.getSecondNode());
        mv.visitJumpInsn(IF_ICMPGT, l1);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, l2);
        mv.visitLabel(l1);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(l2);

        return null;
    }

    @Override
    public Void visitIntegerLiteralNode(IntegerLiteralNode node) {
        int value = node.getValue();

        switch (value) {
            case -1:
                mv.visitInsn(ICONST_M1);
                break;
            case 0:
                mv.visitInsn(ICONST_0);
                break;
            case 1:
                mv.visitInsn(ICONST_1);
                break;
            case 2:
                mv.visitInsn(ICONST_2);
                break;
            case 3:
                mv.visitInsn(ICONST_3);
                break;
            case 4:
                mv.visitInsn(ICONST_4);
                break;
            case 5:
                mv.visitInsn(ICONST_5);
                break;
            default:
                if (value <= Byte.MAX_VALUE) {
                    mv.visitIntInsn(BIPUSH, value);
                } else if (value <= Short.MAX_VALUE) {
                    mv.visitIntInsn(SIPUSH, value);
                } else {
                    mv.visitLdcInsn(value);
                }
                break;
        }

        return null;
    }

    @Override
    public Void visitMultiplyNode(MultiplyNode node) {
        visitIntegerOperands(node.getFirstNode(), node.getSecondNode());
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
        visitBooleanOperands(node.getFirstNode(), node.getSecondNode());
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
            box(value);
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
    public Void visitSubtractNode(SubtractNode node) {
        visitIntegerOperands(node.getFirstNode(), node.getSecondNode());
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
        if (!context.hasVariableName(node.getName())) {
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

    private void box(Node node) {
        Type type = node.getNodeType().getType();
        if (!type.equals(Type.getType(Object.class))) {
            mv.valueOf(type);
        }
    }

    private void unbox(Node node, Type type) {
        if (node.getNodeType().getType().equals(Type.getType(Object.class))) {
            mv.unbox(type);
        }
    }
}
