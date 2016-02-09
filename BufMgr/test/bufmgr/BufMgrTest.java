package bufmgr;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2/9/16.
 */
public class BufMgrTest {
    static int NUM_BUFFERS = 20;

    BufMgr bufMgr;

    @Before
    public void setUp() throws Exception {
        bufMgr = new BufMgr(NUM_BUFFERS, 0, "");
    }

    @Test
    public void testGetNumBuffers() throws Exception {
        assertEquals("", NUM_BUFFERS, bufMgr.getNumBuffers());
    }
}