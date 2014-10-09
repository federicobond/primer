package ar.edu.itba.lang;

import ar.edu.itba.lang.compiler.Script;
import junit.framework.TestCase;
import org.junit.BeforeClass;

import java.io.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertThat;

public class IntegrationTests extends TestCase {

    @BeforeClass
    public static void configure() {
        Script.enableOptimizations = false;
    }

    private String run(String code) {
        PrintStream stdout = System.out;
        OutputStream out = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(out, true, "UTF-8"));
        } catch (UnsupportedEncodingException ignore) { }

        try {
            Script.fromString(code).exec(new String[0]);
        } catch (Script.ScriptException | IOException e) {
            fail(e.getMessage());
        }

        String output = out.toString().replace(System.lineSeparator(), "\n");
        System.setOut(stdout);

        return output;
    }

    public void testIfTrue() {
        String output = run("if true { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfFalse() {
        String output = run("if false { println(\"hello\") }");
        assertThat(output, isEmptyString());
    }

    public void testIfTrueElse() {
        String output = run("if true { println(\"hello\") } else { println(\"world\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfFalseElse() {
        String output = run("if false { println(\"hello\") } else { println(\"world\") }");
        assertThat(output, equalTo("world\n"));
    }

    public void testWhileFalse() {
        String output = run("while false { println(\"hello\") }");
        assertThat(output, isEmptyString());
    }

    public void testIfAndExpression() {
        String output = run("if true && true { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfOrExpression() {
        String output = run("if true || false { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfLessThanExpression() {
        String output = run("if 1 < 2 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfLessEqualsThanExpression() {
        String output = run("if 2 <= 2 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfGreaterThanExpression() {
        String output = run("if 2 > 1 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfGreaterEqualThanExpression() {
        String output = run("if 2 >= 2 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testModulus() {
        String output = run("if (4 % 2) == 0 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testModulus2() {
        String output = run("if (3 % 2) == 1 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfEqualExpression() {
        String output = run("if 1 == 1 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfNotEqualExpression() {
        String output = run("if 1 != 2 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfAddEqualExpression() {
        String output = run("if 1 + 1 == 2 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfSubstractEqualExpression() {
        String output = run("if 1 - 1 == 0 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfMultiplyEqualExpression() {
        String output = run("if 2 * 5 == 10 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    public void testIfDivideEqualExpression() {
        String output = run("if 10 / 3 == 3 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }
}
