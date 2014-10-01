package ar.edu.itba.lang.compiler;

import ar.edu.itba.lang.Lexer;
import ar.edu.itba.lang.Parser;
import ar.edu.itba.lang.ast.Node;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Scanner;
import java_cup.runtime.ScannerBuffer;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Compiler {

    private Class<Script> script;

    static class CompilerException extends RuntimeException {

        public CompilerException(String message) {
            super(message);
        }
    }

    public static class ByteClassLoader extends URLClassLoader {
        private final byte[] classBytes;

        public ByteClassLoader(URL[] urls, ClassLoader parent, byte[] bytes) {
            super(urls, parent);
            this.classBytes = bytes;
        }

        @Override
        protected Class<?> findClass(final String name) throws ClassNotFoundException {
            return defineClass(name, classBytes, 0, classBytes.length);
        }
    }

    public Compiler() {
    }

    public Node parse(String code, String fileName) {
        Parser parser = getParser(code, fileName);
        return getRootNode(parser);
    }

    public Compiler compile(String code, String fileName) {
        Node root = parse(code, fileName);

        byte[] classBytes = new ASMVisitor(root).getByteArray();
        URLClassLoader cl = new ByteClassLoader(new URL[0], ClassLoader.getSystemClassLoader(), classBytes);
        try {
            script = (Class<Script>)cl.loadClass("Main");
        } catch (ClassNotFoundException e) {
            /* ignore */
        }

        return this;
    }

    public Compiler exec() {
        return exec(new String[0]);
    }

    public Compiler exec(String[] argv) {
        Method m = null;
        try {
            m = script.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) { }

        try {
            m.invoke(null, (Object)new String[0]);
        } catch (IllegalAccessException e) {
            /* ignore */
        } catch (InvocationTargetException e) {
            /* ignore */
        }

        return this;
    }

    public Compiler compile(String code) {
        return compile(code, null);
    }

    private Parser getParser(String code, String fileName) {
        ComplexSymbolFactory csf = new ComplexSymbolFactory();
        Lexer lexer = new Lexer(new StringReader(code), csf);

        if (fileName != null) {
            lexer.setFilename(fileName);
        }

        Scanner scanner = new ScannerBuffer(lexer);
        return new Parser(scanner, csf);
    }

    private Node getRootNode(Parser parser) {
        Node root = null;
        try {
            root = (Node)parser.parse().value;
        } catch (Throwable e) {
            throw new CompilerException(e.getMessage());
        }
        return root;
    }
}
