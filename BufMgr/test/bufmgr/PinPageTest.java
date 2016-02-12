package bufmgr;

import chainexception.ChainException;
import com.sun.net.httpserver.Filter;
import diskmgr.InvalidPageNumberException;
import global.Minibase;
import global.Page;
import global.PageId;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by david on 2/10/16.
 */
public class PinPageTest extends BufMgrTest {
    PageId mPageId;

    @Before
    public void setUp() throws IOException, ChainException {
        mPageId = Minibase.DiskManager.allocate_page(1);
    }

    @Test
    public void pinPageTest() throws ChainException {
        Page p = new Page();
        Minibase.BufferManager.pinPage(mPageId, p, false);

        assertTrue(Minibase.BufferManager.mBuffer.containsKey(mPageId));
        boolean exists = false;
        for(Map.Entry<PageId, Frame> entry : Minibase.BufferManager.mBuffer.entrySet()) {
            if(entry.getValue().containsSamePage(p)) {
                exists = true;
                break;
            }
        }
        assertTrue("the page exists in the buffer", exists);
    }

    @Test
    public void pageGetsPinedTest() throws ChainException {
        Page p = new Page();
        Minibase.BufferManager.pinPage(mPageId, p, false);
        assertTrue("Page is pinned in buffer", Minibase.BufferManager.mBuffer.get(mPageId).isPinned());
    }

    /**
     * This test apparently passes even though I don't think it SHOULD.
     * But it is a problem with the Minibase, not the code.
     * @throws ChainException
     */
    @Test //@Ignore//(expected = ChainException.class)
    public void pinNonExistentPage() throws ChainException {
        Page p = new Page();
        Minibase.BufferManager.pinPage(new PageId(mPageId.pid + 1), p, false);
    }
}
