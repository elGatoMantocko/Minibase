package tests;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2/5/16.
 */
public class HFDriverTest {

  HFDriver mDriver;
  @Before
  public void setUp() throws Exception {
      mDriver = new HFDriver();
  }

  /**
   * Redundant test because it seems to write to come outfiles.
   * @throws Exception
   */
//  @Test
//  public void testrunTests() throws Exception {
//      mDriver.runTests();
//  }

  @Test
  public void testTest1() throws Exception {
      assertTrue("Test 1 had an error", mDriver.test1());
  }

  @Test @Ignore
  public void testTest2() throws Exception {
      assertTrue("Test 2 had an error", mDriver.test2());
  }

  @Test(timeout = 1000L)  @Ignore //This tests contains a while loop that can be infinite.
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
