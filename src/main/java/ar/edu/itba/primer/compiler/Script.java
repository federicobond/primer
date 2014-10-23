package ar.edu.itba.primer.compiler;

import ar.edu.itba.primer.Lexer;
import ar.edu.itba.primer.Parser;
import ar.edu.itba.primer.Symbols;
import ar.edu.itba.primer.ast.Node;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Scanner;
import java_cup.runtime.ScannerBuffer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Script {

    public static boolean enableOptimizations = true;

    private final byte[] code;

    private final String fileName;

    private Script(byte[] code, String fileName) {
        this.code = code;
        this.fileName = fileName;
    }

    public static Script fromFile(File file) throws IOException {
        byte[] code = Files.readAllBytes(file.toPath());
        return new Script(code, file.getName());
    }

    public static Script fromString(String code) throws IOException {
        return new Script(code.getBytes("UTF-8"), "unnamed");
    }

    public List<String> tokenList() {
        List<String> list = new ArrayList<String>();

        Lexer lexer = getLexer(new ComplexSymbolFactory());

        try {
            String token;
            do {
                token = Symbols.terminalNames[lexer.next_token().sym];
                list.add(token);
            } while (!token.equals("EOF"));
        } catch (IOException e) {
            throw new ScriptException(e.getMessage());
        }

        return list;
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

    public Node parse() {
        Node root = getRootNode(getParser());

        if (enableOptimizations) {
            root = root.accept(new ConstantFoldingVisitor());
        }

        return root;
    }

    public String trace() {
        Node root = parse();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        new ASMVisitor(new TraceClassVisitor(null, new Textifier(), pw), fileName).start(root);

        return sw.toString();
    }

    public void exec(String[] argv) {
        Class<?> klass = loadClass(compile());

        try {
            Method m = klass.getMethod("main", String[].class);
            m.invoke(null, (Object)argv);
        } catch (NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException ignored) { }
    }

    public byte[] compile() {
        Node root = parse();

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        return new ASMVisitor(cw, fileName).start(root).getByteArray();
    }

    private Class<?> loadClass(byte[] classBytes) {
        URLClassLoader cl = new ByteClassLoader(new URL[0], ClassLoader.getSystemClassLoader(), classBytes);

        Class<?> klass = null;
        try {
            klass = cl.loadClass("Main");
        } catch (ClassNotFoundException e) {
            /* ignore */
        }

        return klass;
    }

    private Parser getParser() {
        ComplexSymbolFactory csf = new ComplexSymbolFactory();
        Lexer lexer = getLexer(csf);
        Scanner scanner = new ScannerBuffer(lexer);
        return new Parser(scanner, csf);
    }

    private Lexer getLexer(ComplexSymbolFactory csf) {
        Lexer lexer = null;
        try {
            Reader reader = new InputStreamReader(
                    new ByteArrayInputStream(code), "UTF-8");
            lexer = new Lexer(reader, csf);
        } catch (UnsupportedEncodingException ignore) { }

        if (fileName != null) {
            lexer.setFilename(fileName);
        }
        return lexer;
    }

    private Node getRootNode(Parser parser) {
        Node root;
        try {
            root = (Node)parser.parse().value;
        } catch (Throwable e) {
            throw new ScriptException(e.getMessage());
        }
        return root;
    }
}
