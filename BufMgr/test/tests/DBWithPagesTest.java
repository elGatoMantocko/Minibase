package tests;

import bufmgr.BufferPoolExceededException;
import bufmgr.HashEntryNotFoundException;
import bufmgr.PagePinnedException;
import chainexception.ChainException;
import global.Convert;
import global.Minibase;
import global.Page;
import global.PageId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 2/16/16.
 */
public class DBWithPagesTest extends ProvidedTests {

    @Before
    public void fillWithPages() throws ChainException {
        numPages = Minibase.BufferManager.getNumUnpinned() + 1;
        pg = new Page();
        firstPid = new PageId();

        //Create a bunch of pages
        firstPid = Minibase.BufferManager.newPage( pg, numPages );

        //Unpin it, to simplify things.
        Minibase.BufferManager.unpinPage(firstPid, false /*not dirty*/);
    }

    @Test
    public void test1() throws ChainException, IOException {
        //Write something on each page
        pid = new PageId();
        lastPid = new PageId();

        for ( pid.pid = firstPid.pid, lastPid.pid = pid.pid+numPages;
              pid.pid < lastPid.pid;
              pid.pid = pid.pid + 1 ) {

            Minibase.BufferManager.pinPage( pid, pg, /*emptyPage:*/ false);

            // Copy the page number + 99999 onto each page.  It seems
            // unlikely that this bit pattern would show up there by
            // coincidence.
            int data = pid.pid + 99999;

            Convert.setIntValue (data, 0, pg.getpage());

            Minibase.BufferManager.unpinPage( pid, /*dirty:*/ true );
        }


        //Read the numbers from the page.

        for (pid.pid=firstPid.pid; pid.pid<lastPid.pid;
             pid.pid = pid.pid + 1) {

            Minibase.BufferManager.pinPage( pid, pg, /*emptyPage:*/ false );

            int data = 0;

            data = Convert.getIntValue (0, pg.getpage());

            assertEquals(pid.pid + 99999, data);

            Minibase.BufferManager.unpinPage( pid, /*dirty:*/ true );
        }


        for ( pid.pid=firstPid.pid; pid.pid < lastPid.pid;
              pid.pid = pid.pid + 1) {

            Minibase.BufferManager.freePage( pid );
        }

    }

    @After
    public void unpinAndFreePages() throws ChainException {
        for ( pid.pid = firstPid.pid; pid.pid < lastPid.pid;
              pid.pid = pid.pid + 1 ) {
            Minibase.BufferManager.freePage( pid );
        }
    }
}
