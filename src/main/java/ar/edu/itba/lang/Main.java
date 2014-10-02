package ar.edu.itba.lang;

import ar.edu.itba.lang.ast.Node;
import ar.edu.itba.lang.compiler.Compiler;

import java.io.File;
import java.nio.file.Files;

public class Main {

    public static void printUsage() {
        System.err.println("usage: lang [run|ast] filename.lang");
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        String subCommand = args[0];
        File file = new File(args[1]);
        String fileName = file.getName();

        if (!file.isFile()) {
            System.err.println("error: file does not exist");
            System.exit(1);
        }

        String code = new String(Files.readAllBytes(file.toPath()));

        switch (subCommand) {
            case "run":
                new Compiler().compile(code, fileName).exec();
                break;
            case "ast":
                Node root = new Compiler().parse(code, fileName);
                System.out.println(root);
                break;
            case "bytecode":
                String byteCode = new Compiler().trace(code, fileName);
                System.out.println(byteCode);
                break;
            default:
                System.err.println("error: invalid command " + subCommand);
        }
    }
}
