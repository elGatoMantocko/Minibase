package bufmgr;

import diskmgr.InvalidPageNumberException;
import global.GlobalConst;
import global.Minibase;
import global.PageId;
import jdk.nashorn.internal.objects.Global;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by david on 2/12/16.
 */
public class FlushPageTest extends BufMgrTest implements GlobalConst {
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

    @Test(expected=PagePinnedException.class)
    public void flushPinnedPage() throws PagePinnedException, InvalidPageNumberException {
        Map.Entry<PageId, Frame> entry = Minibase.BufferManager.mBuffer.entrySet().iterator().next();
        entry.getValue().pin();
        Minibase.BufferManager.flushPage(entry.getKey());
    }

    /**
     * When called with an invalid page number if should not do
     * anything because it shouldnt be in the buffer.
     *
     * @throws PagePinnedException
     * @throws InvalidPageNumberException
     */
    @Test
    public void flushInvalidPageNumber() throws PagePinnedException, InvalidPageNumberException {
        Minibase.BufferManager.flushPage(new PageId(NUM_PAGES+1));
    }

}
