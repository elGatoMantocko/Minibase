package bufmgr;

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
        mBufManager = new BufMgr(NUM_BUFFERS, 0, "");
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