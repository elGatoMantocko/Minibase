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

    /**
     * The driver needs to init things in runTests, so it needs to be
     * run like this, unfortunately.
     */
    @Test
    public void testRunTests() {
        assertTrue(mDriver.runTests());
    }

}