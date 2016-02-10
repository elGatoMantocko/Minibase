package bufmgr;

import org.junit.Before;


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

}