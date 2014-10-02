package ar.edu.itba.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Kernel {

    public void println(String str) {
        System.out.println(str);
    }

    public String readln(String str) {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            return r.readLine();
        } catch (IOException ignore) {}
        return null;
    }

}
