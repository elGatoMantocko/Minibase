package bufmgr;

import chainexception.ChainException;
import global.Minibase;
import global.Page;
import global.PageId;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by david on 2/12/16.
 */
public class FreePageTest extends BufMgrTest {
    Page mPage = new Page();
    PageId mPageId;

    @Before
    public void allocatePage() throws ChainException, IOException {
        mPageId = Minibase.BufferManager.newPage(mPage, 1);
    }

    @Test(expected = PagePinnedException.class)
    public void testFreePage() throws Exception {
        Minibase.BufferManager.freePage(mPageId);
    }

    @Test
    public void testFreeUnpinnedPage() throws Exception {
        Minibase.BufferManager.unpinPage(mPageId, false);
        Minibase.BufferManager.freePage(mPageId);
    }

    @Test
    public void testFreePageClearsBuffer() throws Exception {
        Minibase.BufferManager.unpinPage(mPageId, false);
        Minibase.BufferManager.freePage(mPageId);
        assertNull(Minibase.BufferManager.mBuffer.get(mPageId));
    }
}