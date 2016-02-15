package bufmgr;

import chainexception.ChainException;
import diskmgr.InvalidPageNumberException;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by david on 2/12/16.
 */
public class LifecycleTest extends BufMgrTest implements GlobalConst{

    @Test(expected=BufferPoolExceededException.class)
    public void testMockPinningTooManyPages() throws ChainException {
        Minibase.BufferManager.flushAllPages();

        for(int i = 0; i < NUM_BUFFERS; i++) { //Run ten times .
            PageId id = new PageId(i);
            Page p = new Page();
            Frame f = new Frame();
            f.setPage(p);
            f.pin();
            Minibase.BufferManager.mBuffer.put(id, f);
        }

        Minibase.BufferManager.pinPage(new PageId(NUM_BUFFERS), new Page(), false);
    }


    @Test(expected=BufferPoolExceededException.class)
    public void testPinningTooManyPages() throws ChainException {
        Minibase.BufferManager.flushAllPages();

        for(int i = 0; i < NUM_BUFFERS; i++) { //Run ten times .
            PageId id = new PageId(i);
            Page p = new Page();
            Minibase.BufferManager.pinPage(id, p, false);
        }

        Minibase.BufferManager.pinPage(new PageId(NUM_BUFFERS), new Page(), false);
    }

    @Test(expected=BufferPoolExceededException.class) @Ignore
    public void testTooManyNewPages() throws ChainException, IOException {
        Minibase.BufferManager.flushAllPages();

        for(int i = 0; i < NUM_BUFFERS; i++) { //Run ten times .
            Minibase.BufferManager.newPage(new Page(), 1);
        }

        Minibase.BufferManager.pinPage(new PageId(NUM_BUFFERS), new Page(), false);
    }
}
