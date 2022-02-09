import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber1L;

/**
 * JUnit test fixture for {@code NaturalNumber}'s constructors and kernel
 * methods.
 *
 * @author Put your name here
 *
 */
public abstract class NaturalNumberTest {

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new number
     * @ensures constructorTest = 0
     */
    protected abstract NaturalNumber constructorTest();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorTest = i
     */
    protected abstract NaturalNumber constructorTest(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorTest)
     */
    protected abstract NaturalNumber constructorTest(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorTest = n
     */
    protected abstract NaturalNumber constructorTest(NaturalNumber n);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @return the new number
     * @ensures constructorRef = 0
     */
    protected abstract NaturalNumber constructorRef();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorRef = i
     */
    protected abstract NaturalNumber constructorRef(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorRef)
     */
    protected abstract NaturalNumber constructorRef(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorRef = n
     */
    protected abstract NaturalNumber constructorRef(NaturalNumber n);

    // TODO - add test cases for four constructors, multiplyBy10, divideBy10, isZero

    @Test
    public final void testNoArgumentConstructor() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest();
        NaturalNumber numExpected = this.constructorRef();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testIntArgumentConstructor1() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(467);
        NaturalNumber numExpected = this.constructorRef(467);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testIntArgumentConstructor2() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(0);
        NaturalNumber numExpected = this.constructorRef(0);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testIntArgumentConstructor3() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(3);
        NaturalNumber numExpected = this.constructorRef(3);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testStringArgumentConstructor1() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest("0");
        NaturalNumber numExpected = this.constructorRef("0");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testStringArgumentConstructor2() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest("58218");
        NaturalNumber numExpected = this.constructorRef("58218");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testStringArgumentConstructor3() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest("5");
        NaturalNumber numExpected = this.constructorRef("5");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testNNArgumentConstructor1() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(new NaturalNumber1L(0));
        NaturalNumber numExpected = this.constructorRef(new NaturalNumber1L(0));
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testNNArgumentConstructor2() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(new NaturalNumber1L(123));
        NaturalNumber numExpected = this
                .constructorRef(new NaturalNumber1L(123));
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testNNArgumentConstructor3() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(new NaturalNumber1L(5));
        NaturalNumber numExpected = this.constructorRef(new NaturalNumber1L(5));
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testMultiplyBy10() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(123);
        num.multiplyBy10(0);
        NaturalNumber numExpected = this.constructorRef(1230);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testMultiplyBy101() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(123);
        num.multiplyBy10(5);
        NaturalNumber numExpected = this.constructorRef(1235);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testMultiplyBy102() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(0);
        num.multiplyBy10(9);
        NaturalNumber numExpected = this.constructorRef(9);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testMultiplyBy103() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(0);
        num.multiplyBy10(0);
        NaturalNumber numExpected = this.constructorRef(0);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
    }

    @Test
    public final void testDivideBy10() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(123);
        int remainder = num.divideBy10();
        NaturalNumber numExpected = this.constructorRef(12);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
        assertEquals(remainder, 3);
    }

    @Test
    public final void testDivideBy101() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(5);
        int remainder = num.divideBy10();
        NaturalNumber numExpected = this.constructorRef(0);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
        assertEquals(remainder, 5);

    }

    @Test
    public final void testDivideBy102() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(0);
        int remainder = num.divideBy10();
        NaturalNumber numExpected = this.constructorRef(0);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(numExpected, num);
        assertEquals(remainder, 0);

    }

    @Test
    public final void testisZero1() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(123);

        boolean isZeroTest = num.isZero();

        NaturalNumber numExpected = this.constructorRef(123);

        boolean isZeroRef = num.isZero();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(isZeroRef, isZeroTest);
    }

    @Test
    public final void testisZero2() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(0);

        boolean isZeroTest = num.isZero();

        NaturalNumber numExpected = this.constructorRef(0);

        boolean isZeroRef = num.isZero();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(isZeroRef, isZeroTest);
    }

    @Test
    public final void testisZero3() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber num = this.constructorTest(1);

        boolean isZeroTest = num.isZero();

        NaturalNumber numExpected = this.constructorRef(1);

        boolean isZeroRef = num.isZero();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(isZeroRef, isZeroTest);
    }

}
