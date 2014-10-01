package ar.edu.itba.lang;

import ar.edu.itba.lang.compiler.Compiler;

import java.io.*;
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

        String subcommand = args[0];
        File file = new File(args[1]);

        if (!file.isFile()) {
            System.err.println("error: file does not exist");
            System.exit(1);
        }

        String code = new String(Files.readAllBytes(file.toPath()));

        if (subcommand.equals("run")) {
            new Compiler().compile(code, file.getName()).exec();
        } else if (subcommand.equals("ast")) {
            System.out.println(new Compiler().parse(code, file.getName()));
        } else {
            System.err.println("error: invalid command " + subcommand);
        }
    }
}
