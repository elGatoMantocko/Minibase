package bufmgr;

import diskmgr.InvalidPageNumberException;
import global.Minibase;
import global.Page;
import global.PageId;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2/15/16.
 */
public class WritePageTest extends BufMgrTest{

    @Test(expected=InvalidPageNumberException.class)
    public void testWriteToDisk() throws Exception {
        Minibase.BufferManager.writeToDisk(new PageId(NUM_PAGES+1), new Page());
    }
}