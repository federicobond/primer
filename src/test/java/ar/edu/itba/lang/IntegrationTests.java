package ar.edu.itba.lang;

import ar.edu.itba.lang.compiler.Compiler;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertThat;

public class IntegrationTests extends TestCase {

    private OutputStream out;

    public void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    public void testIfTrue() {
        new Compiler().compile("if true { println(\"hello\") }").exec();
        assertThat(out.toString(), equalTo("hello\n"));
    }

    public void testIfFalse() {
        new Compiler().compile("if false { println(\"hello\") }").exec();
        assertThat(out.toString(), isEmptyString());
    }

    public void testIfTrueElse() {
        new Compiler().compile("if true { println(\"hello\") } else { println(\"world\") }").exec();
        assertThat(out.toString(), equalTo("hello\n"));
    }

    public void testIfFalseElse() {
        new Compiler().compile("if false { println(\"hello\") } else { println(\"world\") }").exec();
        assertThat(out.toString(), equalTo("world\n"));
    }

    public void testWhileFalse() {
        new Compiler().compile("while false { println(\"hello\") }").exec();
        assertThat(out.toString(), isEmptyString());
    }
}
