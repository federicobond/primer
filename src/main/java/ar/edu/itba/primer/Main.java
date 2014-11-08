package ar.edu.itba.primer;

import ar.edu.itba.primer.ast.Node;
import ar.edu.itba.primer.compiler.Script;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Main {

    private static final String VERSION = "1.0";
    private static final Map<String, String> commandHelp = new TreeMap<>();
    static {
        commandHelp.put("run", "executes the file");
        commandHelp.put("compile", "compiles the file to a JVM .class");
        commandHelp.put("ast", "prints the AST for the file");
        commandHelp.put("bytecode", "prints a human-readable version of the JVM bytecode for the file");
        commandHelp.put("tokens", "prints the list of tokens for the file");
    }

    public static void printUsage() {
        System.err.println("usage: primer COMMAND programfile");
    }

    private static void printHelp() {
        System.out.printf("primer, version %s\n", VERSION);
        printUsage();

        System.out.println();
        for (Map.Entry<String, String> e : commandHelp.entrySet()) {
            System.out.printf("  %-15s %s\n", e.getKey(), e.getValue());
        }
    }

    public static void main(String[] args) {
        if (Arrays.binarySearch(args, "--help") != -1
            || Arrays.binarySearch(args, "-h") != -1) {
            printHelp();
            return;
        }
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        String subCommand = args[0];
        File file = new File(args[1]);

        if (!file.isFile()) {
            System.err.println("error: file does not exist");
            System.exit(1);
        }

        try {
            switch (subCommand) {
                case "run":
                    String[] argv = Arrays.copyOfRange(args, 2, args.length);
                    Script.fromFile(file).exec(argv);
                    break;
                case "ast":
                    Node root = Script.fromFile(file).parse();
                    System.out.println(root);
                    break;
                case "bytecode":
                    String byteCode = Script.fromFile(file).trace();
                    System.out.println(byteCode);
                    break;
                case "compile":
                    byte[] classBytes = Script.fromFile(file).compile();
                    Files.write(Paths.get("Main.class"), classBytes, StandardOpenOption.CREATE);
                    break;
                case "tokens":
                    List<String> tokens = Script.fromFile(file).tokenList();
                    System.out.println(tokens);
                    break;
                default:
                    System.err.println("error: invalid command " + subCommand);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
