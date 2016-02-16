package tests;

import chainexception.ChainException;
import global.Convert;
import global.Minibase;
import global.Page;
import global.PageId;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by david on 2/16/16.
 */
public class WritingPagesTest extends ProvidedTests {

    @Test
    public void test3() throws ChainException, IOException {
        int index;
        int numPages = BUF_SIZE + 10;
        Page pg = new Page();
        PageId pid = new PageId();
        PageId [] pids = new PageId[numPages];

        for ( index=0; index < numPages; ++index ) {
            pid = Minibase.BufferManager.newPage(pg, 1);
            pids[index] = pid;
            int data = pid.pid + 99999;
            Convert.setIntValue (data, 0, pg.getpage());
            Minibase.BufferManager.unpinPage( pid, /*dirty:*/ true );
        }
        for ( index=0; index < numPages; ++index ) {
            pid = pids[index];
            Minibase.BufferManager.pinPage( pid, pg, false);
            int data = 0;
            data = Convert.getIntValue (0, pg.getpage());
            assertEquals("Data read from the page should be what was written", pid.pid+99999, data);
            Minibase.BufferManager.unpinPage( pid, true ); //might not be dirty
        }
    }
}
