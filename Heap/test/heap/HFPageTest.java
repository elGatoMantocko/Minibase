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
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class HFPageTest implements GlobalConst {

  private HFPage hf;

  @Before
  public void setUp() throws Exception {
    hf = new HFPage();

    new Minibase("test.minibase", 1000, 10, 0, "LFU", false);
  }

  @Test
  public void testCurPage() {
    Page p = new Page();
    PageId pid = Minibase.BufferManager.newPage(p, 1);
    hf.setCurPage(pid);
    assertEquals(pid.pid, hf.getCurPage().pid);
  }

  @Test
  public void testNextPage() {
    Page p = new Page();
    PageId pid = Minibase.BufferManager.newPage(p, 1);
    hf.setNextPage(pid);
    assertEquals(pid.pid, hf.getNextPage().pid);
  }

  @Test
  public void testPrevPage() {
    Page p = new Page();
    PageId pid = Minibase.BufferManager.newPage(p, 1);
    hf.setPrevPage(pid);
    assertEquals(pid.pid, hf.getPrevPage().pid);
  }

  @After
  public void tearDown() throws Exception {
    hf.print();
  }
}
