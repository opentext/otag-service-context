package com.opentext.otag.service.context.components;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * This class "tests" a static class, so not really. We are just checking some
 * basic operations.
 * <p>
 * BEWARE: This AWComponentContext will maintain its state of the duration
 * of the test suite it is running in as it is a static framework type utility.
 */
public class ComponentContextTest {

    @Test
    public void testContextOperations() {
        // ensure null is not added to the context
        try {
            AWComponentContext.add(null);
            fail("Expected NPE");
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("We cannot add null references to the component context");
        }

        assertThat(AWComponentContext.add(new MyComponentOne())).isTrue();
        assertThat(AWComponentContext.getComponent(MyComponentOne.class)).isNotNull();

        assertThat(AWComponentContext.add(new MyComponentTwo())).isTrue();
        assertThat(AWComponentContext.getComponent(MyComponentTwo.class)).isNotNull();

        // ensure we can get types
        List<SomeFrameworkInterface> instancesByType =
                AWComponentContext.getComponents(SomeFrameworkInterface.class);
        // both of our instances are implementation of the interface as they are parent and child
        assertThat(instancesByType.size()).isEqualTo(2);

        // ensure we warn the user re: duplicates, some types implement multiple framework
        // type interfaces so may be picked up in scans, so we ignore them and return false
        assertThat(AWComponentContext.add(new MyComponentOne())).isFalse()
                .overridingErrorMessage("We expected the context to reject the addition as we already " +
                        "had an instance of the supplied type");

        // test getComponents
        AtomicInteger myTypeCount = new AtomicInteger(0);
        // ensure that we can detect instances of the base class
        AWComponentContext.getComponents().forEach((c) -> {
            if (c instanceof MyComponentOne) myTypeCount.incrementAndGet();
        });
        assertThat(myTypeCount.get()).isEqualTo(2);
    }

    // AppWorks component test classes
    interface SomeFrameworkInterface extends AWComponent {
    }

    class MyComponentOne implements SomeFrameworkInterface {
    }

    private class MyComponentTwo extends MyComponentOne {
    }

}
