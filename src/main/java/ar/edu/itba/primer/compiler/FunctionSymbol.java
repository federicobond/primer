package ar.edu.itba.primer.compiler;

import org.objectweb.asm.Type;

public class FunctionSymbol extends Symbol {

    private final Type type;
    private final String container;

    public FunctionSymbol(Type type, String container) {
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
