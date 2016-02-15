package bufmgr;

import global.Minibase;
import global.Page;
import global.PageId;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;


/**
 * Created by david on 2/9/16.
 */
public class BufMgrTest {
    static int NUM_BUFFERS = 10;
    static int NUM_PAGES = 100;
    BufMgr mBufManager;

    @Before
    public void createMinibase() throws Exception {
        new Minibase("test_db.minibase", NUM_PAGES, NUM_BUFFERS, 0, "", false);
        mBufManager = Minibase.BufferManager;
    }

    @Test
    public void testBufferInitialized() {
        assertNotNull(mBufManager.mBuffer);
    }

    @Test
    public void testGetNumBuffers() throws Exception {
        assertEquals("", NUM_BUFFERS, mBufManager.getNumBuffers());
    }

    @After
    public void destroyMinibase() throws IOException {
        Minibase.DiskManager.closeDB();
        Minibase.DiskManager.DBDestroy();
    }

    @Test
    public void testGetNumUnpinned() throws Exception {
        Minibase.BufferManager.mBuffer.clear();
        Minibase.BufferManager.pinPage(new PageId(NUM_PAGES/2), new Page(), false);
        assertEquals(Minibase.BufferManager.getNumBuffers() - 1, Minibase.BufferManager.getNumUnpinned());
    }

    @Test
    public void testGetNumPinned() throws Exception {
        Minibase.BufferManager.mBuffer.clear();
        Minibase.BufferManager.pinPage(new PageId(NUM_PAGES/2), new Page(), false);
        assertEquals(1, Minibase.BufferManager.getNumPinned());
    }
}