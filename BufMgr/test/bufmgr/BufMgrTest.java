package bufmgr;

import global.Minibase;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;


/**
 * Created by david on 2/9/16.
 */
public class BufMgrTest {
    static int NUM_BUFFERS = 10;

    BufMgr mBufManager;

    @Before
    public void createMinibase() throws Exception {
        new Minibase("test_db.minibase", 100, NUM_BUFFERS, 0, "", false);
        mBufManager = Minibase.BufferManager;
    }

    @Test
    public void testCleanCreate() {
        assertEquals(0, Minibase.BufferManager.getNumPinned());
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

}