package ar.edu.itba.lang;

import ar.edu.itba.lang.compiler.Compiler;
import junit.framework.TestCase;
import org.junit.BeforeClass;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertThat;

public class IntegrationTests extends TestCase {

    @BeforeClass
    public static void configure() {
        Compiler.enableOptimizations = false;
    }

    private String run(String code) {
        PrintStream stdout = System.out;
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        try {
            new Compiler().compile(code).exec();
        } catch (Compiler.CompilerException e) {
            fail(e.getMessage());
        }

        String output = out.toString();
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
}
