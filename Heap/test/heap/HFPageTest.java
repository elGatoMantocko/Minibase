package heap;

import global.Page;
import global.PageId;
import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.RID;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HFPageTest implements GlobalConst {

  private HFPage hf;

  @Before
  public void setUp() throws Exception {
    hf = new HFPage();
    hf.print();

    new Minibase("test.minibase", 1000, 10, 0, "LFU", false);
  }

  @Test
  public void testPrint() {

  }
}
