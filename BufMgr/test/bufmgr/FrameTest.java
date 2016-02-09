package bufmgr;

import global.Page;
import global.PageId;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2/9/16.
 */
public class FrameTest {

    public static final int PID = 123;

    Frame mFrame;
    Page mPage;
    PageId mPageId;
    @Before
    public void setUp() throws Exception {
        mPage = new Page();
        mPageId = new PageId(PID);
        mFrame = new Frame(mPageId, mPage);
     }

    @Test
    public void testIsDirty() throws Exception {
        assertFalse(mFrame.isDirty());
        mFrame.isDirty = true;
        assertTrue(mFrame.isDirty());
    }

    @Test
    public void testGetPinCount() throws Exception {
        assertEquals("Should start at 0", 0, mFrame.getPinCount());
        mFrame.pin();
        assertEquals("Should increment", 1, mFrame.getPinCount());
    }

    @Test
    public void testPin() throws Exception {
        mFrame.pin();
        assertEquals("Pinning should increase the count to one", 1, mFrame.pinCount);
    }

    @Test
    public void testUnpin() throws Exception {
        mFrame.pin();
        mFrame.unpin();
        assertEquals("This page should no longer be pinned", 0, mFrame.pinCount);

        boolean threwException = false;
        try {
            mFrame.unpin();
        } catch(PageUnpinnedException pue) {
            threwException = true;
        }
        assertTrue("Unpinning an already unpinned page should throw an exception", threwException);
    }

    @Test
    public void testMarkDirty() throws Exception {
        assertFalse("Page should start clean", mFrame.isDirty);
        mFrame.markDirty();
        assertTrue("Page should end up dirty", mFrame.isDirty);
    }

    @Test
    public void testGetPage() throws Exception {
        assertSame("The original page should be returned", mPage, mFrame.getPage());
    }

    @Test
    public void testSetPage() throws Exception {
        Page page = new Page();
        mFrame.setPage(page);
        assertSame("New page should be in the frame", page, mFrame.page);
    }

    @Test @Ignore
    public void testGetPageId() throws Exception {

    }
}