package ar.edu.itba.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Kernel {

    public static final String NIL_STRING = "nil";

    public static void print(Object o) {
        if (o == null) {
            o = NIL_STRING;
        }
        System.out.print(o);
    }

    public static void println(Object o) {
        if (o == null) {
            o = NIL_STRING;
        }
        System.out.println(o);
    }

    public static Object getenv(Object o) {
        return System.getenv((String)o);
    }

    public static Object readln() {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            return r.readLine();
        } catch (IOException ignore) {}
        return null;
    }

}
