package bufmgr;

import diskmgr.InvalidPageNumberException;
import global.Minibase;
import global.Page;
import global.PageId;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by david on 2/12/16.
 */
public class SelectVictimTest extends BufMgrTest {
    @Before
    public void fillWithData() throws PagePinnedException, InvalidPageNumberException {
        Minibase.BufferManager.mBuffer.clear();
        int[][] data = {
                {5,3},
                {6,1},
                {7,2}
        };
        for(int[] d : data) {
            insertData(d[0], (short) d[1]);
        }

    }

    private void insertData(int pageId, short freqCount) {
        Frame f = new Frame(new PageId(pageId), new Page());
        f.frequencyCount = freqCount;
        Minibase.BufferManager.mBuffer.put(new PageId(pageId), f);
    }

    @Test
    public void testSelectVictim() throws BufferPoolExceededException {
        PageId pageId = Minibase.BufferManager.selectVictim();
        assertEquals("Victim should be number 6", 6, pageId.pid);
    }

    @Test
    public void testCullWhenAllPinned() throws BufferPoolExceededException {
        for(Map.Entry<PageId, Frame> entry : Minibase.BufferManager.mBuffer.entrySet()) {
            entry.getValue().pin();
        }
        PageId pageId = Minibase.BufferManager.selectVictim();
        assertNull("Victim should be null", pageId);
    }

    @Test
    public void testPruneBasic() throws BufferPoolExceededException {
        Minibase.BufferManager.pruneBuffer();
        for(Map.Entry<PageId, Frame> entry : Minibase.BufferManager.mBuffer.entrySet()) {
            assertFalse("Buffer should not contain page 6", 6 == entry.getKey().pid);
        }
    }

    @Test(expected=BufferPoolExceededException.class)
    public void testPruneWhenAllPinned() throws BufferPoolExceededException {
        for(Map.Entry<PageId, Frame> entry : Minibase.BufferManager.mBuffer.entrySet()) {
            entry.getValue().pin();
        }
        Minibase.BufferManager.pruneBuffer();
    }



}