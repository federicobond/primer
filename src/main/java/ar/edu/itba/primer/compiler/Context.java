package ar.edu.itba.primer.compiler;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Context {

    private final Context parentContext;

    private Map<String, Symbol> symbols = new HashMap<String, Symbol>();

    private Stack<Label> breakLabels = new Stack<Label>();
    private Stack<Label> continueLabels = new Stack<Label>();

    static class ContextException extends RuntimeException {
        ContextException(String msg) {
            super(msg);
        }
    }

    private Context(Context parentContext) {
        this.parentContext = parentContext;
    }

    public static Context rootContext() {
        return new Context(null);
    }

    public static Context childOf(Context parent) {
        return new Context(parent);
    }

    public boolean isRoot() {
        return parentContext == null;
    }

    public Context parentContext() {
        if (isRoot()) {
            throw new ContextException("root context has no parent");
        }
        return parentContext;
    }

    public void set(String name, Type type, String className) {
        symbols.put(name, new Symbol(type, className));
    }

    public Symbol get(String name) {
        if (parentContext == null || symbols.containsKey(name)) {
            return symbols.get(name);
        }
        return parentContext.get(name);
    }

    public boolean isDefined(String name) {
        if (parentContext == null) {
            return symbols.containsKey(name);
        }
        if (symbols.containsKey(name)) {
            return true;
        }
        return parentContext.isDefined(name);
    }

    public boolean isLoop() {
        return !breakLabels.isEmpty();
    }

    public void pushLoop(Label start, Label end) {
        breakLabels.push(end);
        continueLabels.push(start);
    }

    public void popLoop() {
        breakLabels.pop();
        continueLabels.pop();
    }

    public Label breakLabel() {
        return breakLabels.peek();
    }

    public Label continueLabel() {
        return continueLabels.peek();
    }
}
