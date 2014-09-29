package ar.edu.itba.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Kernel {

    public void println(String str) {
        System.out.println(str);
    }

    public String readln(String str) {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        try {
            return r.readLine();
        } catch (IOException e) {}
        return null;
    }

}
