package bufmgr;

import diskmgr.InvalidPageNumberException;
import global.Minibase;
import global.PageId;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2/12/16.
 */
public class FlushPageTest extends BufMgrTest {
    @Test
    public void testFlushPageFromConstructor() throws PagePinnedException, InvalidPageNumberException {
        Minibase.BufferManager.flushPage(new PageId(2));
        assertFalse(Minibase.BufferManager.mBuffer.containsKey(new PageId(2)));
    }

    @Test
    public void flushAllTest() throws PagePinnedException, InvalidPageNumberException {
        Minibase.BufferManager.flushAllPages();
        assertEquals(0, Minibase.BufferManager.mBuffer.size());
    }

}
