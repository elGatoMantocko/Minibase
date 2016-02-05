package tests;

import org.junit.Before;
import org.junit.Test;
import tests.BMDriver;

import static org.junit.Assert.*;

/**
 * Created by david on 2/3/16.
 */
public class BMDriverTest {
    BMDriver mDriver;
    @Before
    public void setUp() throws Exception {
        mDriver = new BMDriver();
    }

    @Test
    public void testTest1() throws Exception {
        assertTrue("Test 1 had an error", mDriver.test1());
    }

    @Test
    public void testTest2() throws Exception {
        assertTrue("Test 2 had an error", mDriver.test2());
    }

    @Test
    public void testTest3() throws Exception {
        assertTrue("Test 3 had an error", mDriver.test3());
    }

    @Test
    public void testTest4() throws Exception {
        assertTrue("Test 4 had an error", mDriver.test4());
    }

    @Test
    public void testTest5() throws Exception {
        assertTrue("Test 5 had an error", mDriver.test5());
    }

    @Test
    public void testTest6() throws Exception {
        assertTrue("Test 6 had an error", mDriver.test6());
    }
}