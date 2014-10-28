package ar.edu.itba.primer.compiler;

public class ScriptException extends RuntimeException {

    public ScriptException(String message) {
        super(message);
    }

    public ScriptException(Throwable e) {
        super(e);
    }
}
