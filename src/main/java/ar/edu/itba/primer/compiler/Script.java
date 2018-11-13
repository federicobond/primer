package ar.edu.itba.primer.compiler;

import ar.edu.itba.primer.PrimerLexer;
import ar.edu.itba.primer.PrimerParser;
import ar.edu.itba.primer.ast.Node;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        CharStream cs = CharStreams.fromString(new String(code, StandardCharsets.UTF_8), fileName);

        PrimerLexer lexer = new PrimerLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        tokens.fill();

        return tokens.getTokens()
                .stream()
                .map(Token::getText)
                .collect(Collectors.toList());
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
        CharStream cs = CharStreams.fromString(new String(code, StandardCharsets.UTF_8), fileName);

        PrimerLexer lexer = new PrimerLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PrimerParser parser = new PrimerParser(tokens);

        Node root = ASTTransformer.transform(parser.statementList());

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

        ArrayList args = new ArrayList();
        Collections.addAll(args, argv);

        try {
            Method m = klass.getMethod("main", ArrayList.class);
            m.invoke(null, args);
        } catch (NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException e) {

            if (e instanceof InvocationTargetException) {
                Throwable cause = ((InvocationTargetException) e).getCause();
                if (cause.getMessage() == null) {
                    throw new ScriptException(cause.toString());
                }
                throw new ScriptException(cause.getMessage().replace("java.lang.", ""));
            } else {
                throw new ScriptException(e);
            }
        }
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
}
