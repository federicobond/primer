package ar.edu.itba.primer.compiler;

import junit.framework.TestCase;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.isA;

public class ContextTest extends TestCase {

    public void testRootContext() {
        assertThat(Context.rootContext(), isA(Context.class));
    }

    public void testChildOfContext() {
        assertThat(Context.childOf(Context.rootContext()), isA(Context.class));
    }

    public void testIsRoot() {
        assertTrue(Context.rootContext().isRoot());
        assertFalse(Context.childOf(Context.rootContext()).isRoot());
    }

    public void testParentContext() {
        Context parent = Context.childOf(Context.rootContext());
        Context child = Context.childOf(parent);

        assertThat(child.parentContext(), equalTo(parent));
    }

    public void testSetGet() {
        Context context = Context.rootContext();

        context.set("foo", Type.getType(Object.class), "Test");
        assertTrue(context.isDefined("foo"));
        assertThat(context.get("foo"), isA(Symbol.class));
    }

    public void testPushPopLoop() {
        Context context = Context.rootContext();

        assertFalse(context.isLoop());
        context.pushLoop(new Label(), new Label());
        assertTrue(context.isLoop());
        context.popLoop();
        assertFalse(context.isLoop());
    }

    public void testBreakContinueLabels() {
        Context context = Context.rootContext();

        Label start = new Label();
        Label end = new Label();

        context.pushLoop(start, end);

        assertThat(context.continueLabel(), equalTo(start));
        assertThat(context.breakLabel(), equalTo(end));
    }
}
