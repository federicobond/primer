package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.Kernel;
import ar.edu.itba.lang.ast.*;
import org.objectweb.asm.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ASMVisitor implements NodeVisitor<Void>, Opcodes {

    private static final String MAIN_CLASS = "Main";

    private ClassVisitor cw;
    private MethodVisitor mv;
    private Map<String, Symbol> functions = new HashMap<String, Symbol>();
    private Map<String, Integer> variables = new HashMap<String, Integer>();
    private int variableMaxIndex = 0;

    private Stack<Label> breakLabels = new Stack<Label>();
    private Stack<Label> continueLabels = new Stack<Label>();

    public ASMVisitor(Node root, ClassVisitor cw) {
        this.cw = cw;

        initializeSymbols();

        cw.visit(49,
                ACC_PUBLIC + ACC_SUPER,
                MAIN_CLASS,
                null,
                "java/lang/Object",
                null);

        cw.visitSource(MAIN_CLASS + ".java", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
                    "main",
                    "([Ljava/lang/String;)V",
                    null,
                    null);
            root.accept(this);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0); /* computed automatically */
            mv.visitEnd();
        }
        cw.visitEnd();
    }

    private void initializeSymbols() {
        Method[] methods = Kernel.class.getMethods();
        for (Method m : methods) {
            functions.put(m.getName(), new Symbol(Type.getType(m), Type.getInternalName(Kernel.class)));
        }
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
    public Void visitAssignmentNode(AssignmentNode node) {
        if (!variables.containsKey(node.getName())) {
            throw new Script.ScriptException("variable " + node.getName() + " is not defined");
        }

        node.getValue().accept(this);

        // We will assume it is a string for now.
        mv.visitIntInsn(ASTORE, variables.get(node.getName()));

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
        if (breakLabels.isEmpty()) {
            throw new Script.ScriptException("nowhere to break to");
        }
        mv.visitJumpInsn(GOTO, breakLabels.peek());

        return null;
    }

    @Override
    public Void visitCallNode(CallNode node) {
        node.getArgs().accept(this);

        Symbol sym = getCalledMethod(node);

        mv.visitMethodInsn(INVOKESTATIC,
                sym.getContainer(),
                node.getName(),
                sym.getType().getDescriptor(),
                false);

        return null;
    }

    @Override
    public Void visitContinueNode(ContinueNode node) {
        if (continueLabels.isEmpty()) {
            throw new Script.ScriptException("nowhere to continue to");
        }
        mv.visitJumpInsn(GOTO, continueLabels.peek());
        return null;
    }

    @Override
    public Void visitDeclarationNode(DeclarationNode node) {
        if (variables.containsKey(node.getName())) {
            throw new Script.ScriptException("variable " + node.getName() + " is already defined");
        }

        variables.put(node.getName(), variableMaxIndex);

        node.getValue().accept(this);

        // We will assume it is a string for now.
        mv.visitIntInsn(ASTORE, variableMaxIndex);

        variableMaxIndex++;
        return null;
    }

    private Symbol getCalledMethod(CallNode node) {
        if (!functions.containsKey(node.getName())) {
            throw new Script.ScriptException("undefined method " + node.getName());
        }
        return functions.get(node.getName());
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
    public Void visitFunctionNode(FunctionNode node) {
        MethodVisitor old = mv;

        Type[] args = new Type[node.getArgs().getList().size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = Type.getType(Object.class);
        }

        String name = node.getName();
        Type type = Type.getMethodType(Type.getType(Object.class), args);

        if (functions.containsKey(name)) {
            throw new Script.ScriptException("symbol " + name + " already defined");
        }
        functions.put(name, new Symbol(type, MAIN_CLASS));

        mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, name, type.getDescriptor(), null, null);

        Node body = node.getBody();
        Node lastInstruction = body.childNodes().get(body.childNodes().size() - 1);

        body.accept(this);

        if (!(lastInstruction instanceof ReturnNode)) {
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
        }

        mv.visitMaxs(2, 1);

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
        if (!variables.containsKey(node.getName())) {
            throw new Script.ScriptException("variable " + node.getName() + " is not defined");
        }

        mv.visitIntInsn(ALOAD, variables.get(node.getName()));

        return null;
    }

    @Override
    public Void visitWhileNode(WhileNode node) {
        Label conditionLabel = new Label();
        Label endLabel = new Label();

        continueLabels.push(conditionLabel);
        breakLabels.push(endLabel);

        mv.visitLabel(conditionLabel);
        node.getConditionNode().accept(this);
        mv.visitJumpInsn(IFEQ, endLabel);
        node.getBodyNode().accept(this);
        mv.visitJumpInsn(GOTO, conditionLabel);
        mv.visitLabel(endLabel);

        continueLabels.pop();
        breakLabels.pop();

        return null;
    }

    private static class Symbol {

        private final Type type;
        private final String container;

        Symbol(Type type, String container) {
            this.type = type;
            this.container = container;
        }

        public Type getType() {
            return type;
        }

        public String getContainer() {
            return container;
        }
    }
}
