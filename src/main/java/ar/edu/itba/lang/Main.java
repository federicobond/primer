package ar.edu.itba.lang;

import ar.edu.itba.lang.compiler.ASMVisitor;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Scanner;
import java_cup.runtime.ScannerBuffer;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        Path p = Paths.get(".", "Main.class");
        Files.write(p, new ASMVisitor().getByteArray());

        Path path = Paths.get("src/test/resources", "expr.lang");
        String code = new String(Files.readAllBytes(path));

        ComplexSymbolFactory csf = new ComplexSymbolFactory();

        Lexer lexer = new Lexer(new StringReader(code), csf);
        lexer.setFilename(path.getFileName().toString());

        Scanner scanner = new ScannerBuffer(lexer);
        Parser parser = new Parser(scanner, csf);

        Object ret = null;
        try {
            ret = parser.parse().value;
        } catch (Throwable e) {
            System.err.println(e.getMessage());
        }

        if (ret != null) {
            System.out.println(ret);
        }
    }
}
