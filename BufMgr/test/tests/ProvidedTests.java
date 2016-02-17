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
public class ProvidedTests {
    /** Default database size (in pages). */
    protected int DB_SIZE = 10000;

    /** Default buffer pool size (in pages) */
    protected int BUF_SIZE = 100;

    /** Default number of pages to be looked ahead */
    protected int LAH_SIZE = 10;

    protected String dbpath = "minibase.minibase";

    int numPages;
    Page pg;
    PageId pid;
    PageId lastPid;
    PageId firstPid;

    @Before
    public void create_minibase() {
        new Minibase(dbpath, DB_SIZE, BUF_SIZE, LAH_SIZE, "LFU", false);
    }

    @After
    public void destroyMinibase() throws IOException {
        Minibase.DiskManager.closeDB();
        Minibase.DiskManager.DBDestroy();
    }
}
