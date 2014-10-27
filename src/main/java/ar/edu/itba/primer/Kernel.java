package ar.edu.itba.primer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Kernel {

    public static final String NIL_STRING = "nil";

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

    public static Object list() {
        return new ArrayList();
    }

    public static Object list_append(Object list, Object elem) {
        ((ArrayList)list).add(elem);
        return null;
    }

    public static Object list_get(Object list, Object index) {
        return ((ArrayList)list).get((int)index);
    }

    public static Object list_remove(Object list, Object index) {
        return ((ArrayList)list).remove(index);
    }

    public static Object list_empty(Object list) {
        return ((ArrayList)list).isEmpty();
    }

    public static Object list_size(Object list) {
        return ((ArrayList)list).size();
    }

    public static Object upper(Object str) {
        return ((String)str).toUpperCase();
    }

    public static Object lower(Object str) {
        return ((String)str).toLowerCase();
    }

    public static Object isupper(Object str) {
        return str != null && ((String)str).toUpperCase().equals(str);
    }

    public static Object islower(Object str) {
        return str != null && ((String)str).toLowerCase().equals(str);
    }

    public static Object parse_int(Object str) {
        if (str == null) {
            return null;
        }
        return Integer.valueOf((String)str);
    }
}
