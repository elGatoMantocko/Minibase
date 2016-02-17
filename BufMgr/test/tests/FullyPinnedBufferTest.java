package tests;

import bufmgr.BufferPoolExceededException;
import bufmgr.HashEntryNotFoundException;
import bufmgr.PagePinnedException;
import chainexception.ChainException;
import global.Minibase;
import global.Page;
import global.PageId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 2/16/16.
 */
public class FullyPinnedBufferTest extends ProvidedTests {
    @Before
    public void pinAllPages() throws ChainException {
        // We choose this number to ensure that pinning this number of buffers
        // should fail.
        numPages = Minibase.BufferManager.getNumUnpinned() + 1;
        pg = new Page ();
        firstPid = new PageId();

        firstPid = Minibase.BufferManager.newPage( pg, numPages );

        pid = new PageId();
        lastPid = new PageId();

        // First pin enough pages that there is no more room.
        for ( pid.pid=firstPid.pid+1, lastPid.pid=firstPid.pid+numPages-1;
              pid.pid < lastPid.pid;
              pid.pid = pid.pid + 1 ) {
            Minibase.BufferManager.pinPage( pid, pg, /*emptyPage:*/ false );
        }
    }

    @Test
    public void testAllPinned() {
        assertEquals(0, Minibase.BufferManager.getNumUnpinned());
    }

    @Test(expected= BufferPoolExceededException.class)
    public void test2() throws ChainException {
        Minibase.BufferManager.pinPage( lastPid, pg, /*emptyPage:*/ false );
    }

    @Test(expected = PagePinnedException.class)
    public void test2_2() throws ChainException {
        //Pin the first page a second time.
        Minibase.BufferManager.pinPage( firstPid, pg, /*emptyPage:*/ false );

        Minibase.BufferManager.freePage( firstPid );
    }

    @Test(expected = HashEntryNotFoundException.class)
    public void test2_3() throws ChainException {
        //Pin the first page a second time.
        Minibase.BufferManager.pinPage( firstPid, pg, /*emptyPage:*/ false );
        Minibase.BufferManager.unpinPage( firstPid, false );

        //Try to unpin a page not in the pool.
        Minibase.BufferManager.unpinPage( lastPid, false );
    }

    @Test
    public void test2_4() throws ChainException {
        //Pin the first page a second time.
        Minibase.BufferManager.pinPage( firstPid, pg, /*emptyPage:*/ false );
        Minibase.BufferManager.unpinPage( firstPid, false );
    }

    @After
    public void unpinAndFreePages() throws ChainException {
        for ( pid.pid = firstPid.pid; pid.pid < lastPid.pid;
              pid.pid = pid.pid + 1 ) {
            Minibase.BufferManager.unpinPage(pid, false);
        }
    }
}
