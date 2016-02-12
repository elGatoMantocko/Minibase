package bufmgr;

import chainexception.ChainException;
import global.Page;
import global.PageId;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2/10/16.
 */
public class NewPageTest extends BufMgrTest {
    PageId mPageId;
    Page mPage;

    @Before
    public void setup() {
        mPage = new Page();
    }

    @Test
    public void testNewPage() throws ChainException {
        mPageId = mBufManager.newPage(mPage, 1);
        assertNotNull(mPageId);
    }

    @Test
    public void testShouldReturnPageId() throws ChainException {
        mPageId = mBufManager.newPage(mPage, 1);
        assertNotNull(mPageId);
    }

    @Test
    public void testShouldBufferPage() throws ChainException {
        mPageId = mBufManager.newPage(mPage, 1);
        assertTrue(mBufManager.mBuffer.containsKey(mPageId));
    }

    @Test
    public void testShouldPinPage() throws ChainException {
        mPageId = mBufManager.newPage(mPage, 1);
        assertTrue(mBufManager.mBuffer.get(mPageId).isPinned());
    }
}
