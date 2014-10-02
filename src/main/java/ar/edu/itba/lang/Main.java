package ar.edu.itba.lang;

import ar.edu.itba.lang.ast.Node;
import ar.edu.itba.lang.compiler.Script;

import java.io.File;
import java.util.Arrays;

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

        if (!file.isFile()) {
            System.err.println("error: file does not exist");
            System.exit(1);
        }

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
            default:
                System.err.println("error: invalid command " + subCommand);
        }
    }
}
