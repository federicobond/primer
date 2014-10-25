package ar.edu.itba.primer.compiler;

import com.google.common.base.MoreObjects;

public class VariableSymbol extends Symbol {

    private final String name;

    private final int index;

    public VariableSymbol(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String toStringHelper() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("index", index)
                .toString();
    }
}
