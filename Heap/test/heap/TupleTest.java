package heap;

import global.Page;
import global.PageId;
import global.GlobalConst;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2/9/16.
 * Not covering custom trimmed tuple length
 */
public class TupleTest implements GlobalConst {

  private Tuple default_tuple,
                custom_tuple;
  private String str;

  @Before 
  public void setUp() throws Exception {
    str = "Hello world!";
    default_tuple = new Tuple();
    custom_tuple = new Tuple(str.getBytes(), 0, str.length());
  }

  @Test
  public void testDefaultTupleLength() throws Exception {
    assertEquals("The length didn't match the default", default_tuple.getLength(), MAX_TUPSIZE);
  }

  @Test
  public void testCustomMaxLength() {
    assertEquals("The length didn't match the \"Hello world!\" length", custom_tuple.getLength(), str.length());
  }

  @Test
  public void testGetByteArray() {
    assertArrayEquals(custom_tuple.getTupleByteArray(), str.getBytes());
  }

}

