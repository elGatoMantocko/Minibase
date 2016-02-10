package tests;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by david on 2/3/16.
 */
public class BMDriverTest {
    BMDriver mDriver;
    @Before
    public void setUp() throws Exception {
        mDriver = new BMDriver();
    }

    @Test @Ignore
    public void testTest1() throws Exception {
        assertTrue("Test 1 had an error", mDriver.test1());
    }

    @Test @Ignore
    public void testTest2() throws Exception {
        assertTrue("Test 2 had an error", mDriver.test2());
    }

    @Test @Ignore
    public void testTest3() throws Exception {
        assertTrue("Test 3 had an error", mDriver.test3());
    }

    @Test @Ignore
    public void testTest4() throws Exception {
        assertTrue("Test 4 had an error", mDriver.test4());
    }

    @Test @Ignore
    public void testTest5() throws Exception {
        assertTrue("Test 5 had an error", mDriver.test5());
    }

    @Test @Ignore
    public void testTest6() throws Exception {
        assertTrue("Test 6 had an error", mDriver.test6());
    }
}