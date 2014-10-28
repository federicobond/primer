package ar.edu.itba.primer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Kernel {

    private static final String NIL_STRING = "nil";

    public static Object print(Object o) {
        if (o == null) {
            o = NIL_STRING;
        }
        System.out.print(o);
        return null;
    }

    public static Object println(Object o) {
        if (o == null) {
            o = NIL_STRING;
        }
        System.out.println(o);
        return null;
    }

    public static Object getenv(Object o) {
        return System.getenv((String)o);
    }

    public static Object readln() {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            return r.readLine();
        } catch (IOException ignore) {}
        return null;
    }

    public static Object random(Object n) {
        Random r = new Random();
        return Integer.valueOf(r.nextInt((Integer)n));
    }

    public static Object abs(Object n) {
        return Math.abs((Integer)n);
    }

}
