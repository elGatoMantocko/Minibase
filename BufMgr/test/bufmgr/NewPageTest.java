package bufmgr;

import global.Page;
import global.PageId;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2/10/16.
 */
public class NewPageTest extends BufMgrTest {
    final int PID = 123;
    Frame mFrame;
    PageId mPageId;

    @Before
    public void setup() {

    }

    @Test
    public void testNewPage() {
        Page page = new Page();
        PageId pid;
        pid = mBufManager.newPage(page, 1);
    }

    @Test
    public void testShouldReturnPageId() {
        Page page = new Page();
        PageId pid;
        pid = mBufManager.newPage(page, 1);
        assertNotNull(pid);
    }

    @Test
    public void testShouldBufferPage() {
        Page page = new Page();
        PageId pid;
        pid = mBufManager.newPage(page, 1);
        assertTrue(mBufManager.mBuffer.containsKey(pid));
        assertEquals(page, mBufManager.mBuffer.get(pid).getPage());
    }

    @Test
    public void testShouldPinPage() {
        Page page = new Page();
        PageId pid;
        pid = mBufManager.newPage(page, 1);
        assertTrue(mBufManager.mBuffer.get(pid).isPinned());
    }
}
