package tests;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by david on 2/17/16.
 */
public class ProvidedTests {

    @Test
    public void runProvidedTests() {
        HFDriver hd = new HFDriver();
        assertTrue(hd.runTests());
    }
}
