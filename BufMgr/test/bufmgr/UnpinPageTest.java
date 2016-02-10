package bufmgr;

import global.Page;
import global.PageId;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2/10/16.
 */
public class UnpinPageTest extends BufMgrTest {
    final int PID = 123;
    Frame mFrame;
    PageId mPageId; 
    
    @Before
    public void setup() {
        mPageId = new PageId(PID);
        mFrame = new Frame(mPageId, new Page());
        mBufManager.mBuffer.put(mPageId, mFrame);
    }
    
    @Test
    public void testGetNumBuffers() throws Exception {
        assertEquals("", NUM_BUFFERS, mBufManager.getNumBuffers());
    }

    @Test
    public void testUnpinPage() throws Exception {
        mFrame.pin();
        mBufManager.unpinPage(mPageId, false);
        assertEquals("Pin count should be zero", 0, mFrame.getPinCount());
    }

    @Test
    public void testUnpinTwicePinnedPage() throws Exception {
        mFrame.pin();
        mFrame.pin();
        mBufManager.unpinPage(mPageId, false);
        assertEquals("Pin count should be one", 1, mFrame.getPinCount());
    }

    @Test(expected=PageUnpinnedException.class)
    public void testUnpinUnpinnedPage() throws Exception {
        mBufManager.unpinPage(mPageId, false);
    }

    @Test
    public void testUnpinPageDirty() throws Exception {
        mFrame.pin();
        mBufManager.unpinPage(mPageId, true);
        assertEquals("Page should be dirty", true, mFrame.isDirty());
    }


    @Test
    public void testUnpinClean() throws Exception {
        mFrame.pin();
        mBufManager.unpinPage(mPageId, false);
        assertEquals("Page should be clean", false, mFrame.isDirty());
    }
}
