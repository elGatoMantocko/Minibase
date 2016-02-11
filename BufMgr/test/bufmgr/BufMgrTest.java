package bufmgr;

import global.Minibase;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;


/**
 * Created by david on 2/9/16.
 */
public class BufMgrTest {
    static int NUM_BUFFERS = 20;

    BufMgr mBufManager;

    @Before
    public void setUp() throws Exception {
        new Minibase("test_db", 100, NUM_BUFFERS, 0, "", false);
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

}