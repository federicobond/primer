package ar.edu.itba.primer.compiler;

public class VariableSymbol extends Symbol {

    private final int index;

    public VariableSymbol(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
