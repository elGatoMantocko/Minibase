package bufmgr;

import chainexception.ChainException;
import global.Minibase;
import global.Page;
import global.PageId;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
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
    public void testNewPage() throws ChainException, IOException {
        mPageId = mBufManager.newPage(mPage, 1);
        assertNotNull(mPageId);
    }

    @Test
    public void testShouldReturnPageId() throws ChainException, IOException {
        mPageId = mBufManager.newPage(mPage, 1);
        assertNotNull(mPageId);
    }

    @Test
    public void testShouldBufferPage() throws ChainException, IOException {
        mPageId = mBufManager.newPage(mPage, 1);
        assertTrue(mBufManager.mBuffer.containsKey(mPageId));
    }

    @Test
    public void testShouldPinPage() throws ChainException, IOException {
        mPageId = mBufManager.newPage(mPage, 1);
        assertTrue(mBufManager.mBuffer.get(mPageId).isPinned());
    }

    @Test
    public void testNewPageOnFullBuffer() throws ChainException {
        Minibase.BufferManager.mBuffer.clear();
        assertNotNull(Minibase.BufferManager.newPage(new Page(), NUM_BUFFERS*2));

        for(int i = 0; i < NUM_BUFFERS; i++) {
            Minibase.BufferManager.pinPage(new PageId(i), new Page(), false);
        }

        assertNull(Minibase.BufferManager.newPage(new Page(), 1));
    }
}
