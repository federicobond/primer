package ar.edu.itba.primer;

import ar.edu.itba.primer.compiler.Script;
import ar.edu.itba.primer.compiler.ScriptException;
import com.google.common.io.Resources;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertThat;

public class IntegrationTest {

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
        } catch (ScriptException | IOException e) {
            fail(e.getMessage());
        }

        String output = out.toString().replace(System.lineSeparator(), "\n");
        System.setOut(stdout);

        return output;
    }

    private String runFile(String path) {
        String code = null;
        try {
            code = Resources.toString(getClass().getResource("/" + path), Charset.forName("UTF-8"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return run(code);
    }

    @Test
    public void ifTrue() {
        String output = run("if true { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifFalse() {
        String output = run("if false { println(\"hello\") }");
        assertThat(output, isEmptyString());
    }

    @Test
    public void ifTrueElse() {
        String output = run("if true { println(\"hello\") } else { println(\"world\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifFalseElse() {
        String output = run("if false { println(\"hello\") } else { println(\"world\") }");
        assertThat(output, equalTo("world\n"));
    }

    @Test
    public void whileFalse() {
        String output = run("while false { println(\"hello\") }");
        assertThat(output, isEmptyString());
    }

    @Test
    public void ifAndExpression() {
        String output = run("if true && true { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifOrExpression() {
        String output = run("if true || false { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifLessThanExpression() {
        String output = run("if 1 < 2 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifLessEqualsThanExpression() {
        String output = run("if 2 <= 2 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifGreaterThanExpression() {
        String output = run("if 2 > 1 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifGreaterEqualThanExpression() {
        String output = run("if 2 >= 2 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void modulus() {
        String output = run("if (4 % 2) == 0 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void modulus2() {
        String output = run("if (3 % 2) == 1 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifEqualExpression() {
        String output = run("if 1 == 1 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifNotEqualExpression() {
        String output = run("if 1 != 2 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifAddEqualExpression() {
        String output = run("if 1 + 1 == 2 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifSubstractEqualExpression() {
        String output = run("if 1 - 1 == 0 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifMultiplyEqualExpression() {
        String output = run("if 2 * 5 == 10 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifDivideEqualExpression() {
        String output = run("if 10 / 3 == 3 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void whileBreak() {
        String output = run("while true { println(\"hello\")\n break }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void assignment() {
        String output = run("var foo = \"hello\"\n println(foo)");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void simpleBoxVariable() {
        String output = run("var foo = 42\n println(foo)");
        assertThat(output, equalTo("42\n"));
    }

    @Test
    public void boxVariable() {
        String output = run("var foo = 21 + 21\n println(foo)");
        assertThat(output, equalTo("42\n"));
    }

    @Test
    public void unboxVariable() {
        String output = run("var foo = 21\n var bar = foo + 21\n println(bar)");
        assertThat(output, equalTo("42\n"));
    }

    @Test
    public void ifEqualsIntegerVariable() {
        String output = run("var foo = 42\n if foo == 42 { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void ifEqualsStringVariable() {
        String output = run("var foo = \"foo\"\n if foo == \"foo\" { println(\"hello\") }");
        assertThat(output, equalTo("hello\n"));
    }

    @Test
    public void factorial() {
        String output = runFile("factorial.primer");
        assertThat(output, equalTo("Factorial: 120\n"));
    }

    @Test
    public void fibonacci() {
        String output = runFile("fibonacci.primer");
        assertThat(output, equalTo("Result: 21\n"));
    }
}
