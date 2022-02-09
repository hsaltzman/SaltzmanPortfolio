import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

import components.sortingmachine.SortingMachine;

/**
 * JUnit test fixture for {@code SortingMachine<String>}'s constructor and
 * kernel methods.
 *
 * @author Put your name here
 *
 */
public abstract class SortingMachineTest {

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * implementation under test and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorTest = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorTest(
            Comparator<String> order);

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * reference implementation and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorRef = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorRef(
            Comparator<String> order);

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the
     * implementation under test type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsTest = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsTest(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorTest(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the reference
     * implementation type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsRef = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsRef(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorRef(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     * Comparator<String> implementation to be used in all test cases. Compare
     * {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }

    }

    /**
     * Comparator instance to be used in all test cases.
     */
    private static final StringLT ORDER = new StringLT();

    /*
     * Sample test cases.
     */

    @Test
    public final void testConstructor() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.constructorRef(ORDER);
        assertEquals(mExpected, m);
    }

    @Test
    public final void testAddEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green");
        m.add("green");
        assertEquals(mExpected, m);
    }

    // TODO - add test cases for add, changeToExtractionMode, removeFirst,
    // isInInsertionMode, order, and size

    @Test
    public final void testAddNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "red");
        m.add("red");
        assertEquals(mExpected, m);
    }

    @Test
    public final void testChangeToExtractionModeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    @Test
    public final void testChangeToExtractionModeNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "red", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "red", "blue");
        m.changeToExtractionMode();
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveFirstOneElement() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        String removedElement = m.removeFirst();
        assertEquals(mExpected, m);
        assertEquals("green", removedElement);
    }

    @Test
    public final void testRemoveFirstMultiElement() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green", "red", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "red");
        String removedElement = m.removeFirst();

        assertEquals("blue", removedElement);
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveFirstLargeHeap() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "1",
                "2", "3", "4", "5", "6", "7", "8", "9");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "2", "3", "4", "5", "6", "7", "8", "9");
        String removedElement = m.removeFirst();

        assertEquals("1", removedElement);
        assertEquals(mExpected, m);
    }

    @Test
    public final void testIsInInsertionModeEmptyTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);

        assertEquals(mExpected.isInInsertionMode(), m.isInInsertionMode());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testIsInInsertionModeEmptyFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);

        assertEquals(mExpected.isInInsertionMode(), m.isInInsertionMode());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testIsInInsertionModeMultiElementFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "6",
                "7", "8", "9");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "6", "7", "8", "9");

        assertEquals(mExpected.isInInsertionMode(), m.isInInsertionMode());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testIsInInsertionModeMultiElementTrue() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "6",
                "7", "8", "9");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "6", "7", "8", "9");

        assertEquals(mExpected.isInInsertionMode(), m.isInInsertionMode());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testOrderFull() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "6",
                "7", "8", "9");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "6", "7", "8", "9");

        assertEquals(mExpected.order(), m.order());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testOrderFullFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "6",
                "7", "8", "9");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "6", "7", "8", "9");

        assertEquals(mExpected.order(), m.order());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testOrderEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);

        assertEquals(mExpected.order(), m.order());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testOrderEmptyFalse() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);

        assertEquals(mExpected.order(), m.order());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testSize0() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);

        assertEquals(mExpected.size(), m.size());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testSizeFew() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "red", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "red", "blue");

        assertEquals(mExpected.size(), m.size());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testSizeMany() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "1",
                "2", "3", "4", "5", "6", "7", "8", "9");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "1", "2", "3", "4", "5", "6", "7", "8", "9");

        assertEquals(mExpected.size(), m.size());
        assertEquals(mExpected, m);
    }

}
