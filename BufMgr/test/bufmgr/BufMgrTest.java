package bufmgr;

import chainexception.ChainException;
import global.Page;
import global.PageId;
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

    @Test
    public void testUnpinPage() throws Exception {
        final int PID = 123;
        PageId pid = new PageId(PID);
        Frame frame = new Frame(pid, new Page());

        bufMgr.mBuffer.put(pid, frame);

        frame.pin();
        bufMgr.unpinPage(pid, false);
        assertEquals("Pin count should be zero", 0, frame.getPinCount());
    }

    @Test
    public void testUnpinTwicePinnedPage() throws Exception {
        final int PID = 123;
        PageId pid = new PageId(PID);
        Frame frame = new Frame(pid, new Page());
        bufMgr.mBuffer.put(pid, frame);

        frame.pin();
        frame.pin();
        bufMgr.unpinPage(pid, false);
        assertEquals("Pin count should be one", 1, frame.getPinCount());
    }

    @Test(expected=PageUnpinnedException.class)
    public void testUnpinUnpinnedPage() throws Exception {
        final int PID = 123;
        PageId pid = new PageId(PID);
        Frame frame = new Frame(pid, new Page());
        bufMgr.mBuffer.put(pid, frame);

        bufMgr.unpinPage(pid, false);
    }

    @Test
    public void testUnpinPageDirty() throws Exception {
        final int PID = 123;
        PageId pid = new PageId(PID);
        Frame frame = new Frame(pid, new Page());
        bufMgr.mBuffer.put(pid, frame);

        frame.pin();
        bufMgr.unpinPage(pid, true);
        assertEquals("Page should be dirty", true, frame.isDirty());
    }


    @Test
    public void testUnpinClean() throws Exception {
        final int PID = 123;
        PageId pid = new PageId(PID);
        Frame frame = new Frame(pid, new Page());
        bufMgr.mBuffer.put(pid, frame);

        frame.pin();
        bufMgr.unpinPage(pid, false);
        assertEquals("Page should be clean", false, frame.isDirty());
    }
}