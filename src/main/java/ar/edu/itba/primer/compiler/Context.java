package ar.edu.itba.primer.compiler;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import java.util.*;

public class Context {

    private final Context parentContext;

    private final Map<String, FunctionSymbol> functions = new HashMap<>();
    private final Map<String, VariableSymbol> variables = new HashMap<>();

    private final Stack<Label> breakLabels = new Stack<>();
    private final Stack<Label> continueLabels = new Stack<>();

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

    public void setFunction(String name, Type type, String className) {
        functions.put(name, new FunctionSymbol(type, className));
    }

    public void setVariable(String name, int index){
        variables.put(name, new VariableSymbol(name, index));
    }

    public FunctionSymbol getFunction(String name) {
        if (parentContext == null || functions.containsKey(name)) {
            return functions.get(name);
        }
        return parentContext.getFunction(name);
    }

    public VariableSymbol getVariable(String name) {
        return variables.get(name);
    }

    public boolean hasFunctionName(String name) {
        if (parentContext == null) {
            return functions.containsKey(name);
        }
        if (functions.containsKey(name)) {
            return true;
        }
        return parentContext.hasFunctionName(name);
    }

    public boolean hasVariableName(String name) {
        return variables.containsKey(name);
    }

    public List<VariableSymbol> localVariables() {
        List<VariableSymbol> list = new ArrayList<>();

        for (Map.Entry<String, VariableSymbol> e : variables.entrySet()) {
            list.add(e.getValue());
        }

        Collections.sort(list, new Comparator<VariableSymbol>() {
            @Override
            public int compare(VariableSymbol o1, VariableSymbol o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });

        return list;
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
