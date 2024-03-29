package bufmgr;

import chainexception.ChainException;
import diskmgr.FileIOException;
import diskmgr.InvalidPageNumberException;
import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;

import java.io.IOException;
import java.util.Map;

/**
 * The BufferManager for the Minibase.
 */
public class BufMgr implements GlobalConst {
    protected final Map<PageId, Frame> mBuffer;
    private final int mNumberOfBuffers;

    /**
     * Create the BufMgr object.
     * Allocate pages (frames) for the buffer pool in main memory and
     * make the buffer manage aware that the replacement policy is
     * specified by replacerArg (e.g., LH, Clock, LRU, MRU, LFU, etc.).
     *
     * @param numberOfBuffers           number of buffers in the buffer pool
     * @param lookAheadSize:    Please ignore this parameter
     * @param replacementPolicy Name of the replacement policy, that parameter will be set to "LFU" (you
     *                          can safely ignore this parameter as you will implement only one policy)
     */
    public BufMgr(int numberOfBuffers, int lookAheadSize, String replacementPolicy) {
        //Allocate an array of buffers with given size.
        mBuffer = new MyHashTable();
        this.mNumberOfBuffers = numberOfBuffers;
        //save numberOfBuffers.
    }

    /**
     * Pin a page.
     * First check if this page is already in the buffer pool.
     * If it is, increment the pin_count and return a pointer to this
     * page.
     * If the pin_count was 0 before the call, the page was a
     * replacement candidate, but is no longer a candidate.
     * If the page is not in the pool, choose a frame (from the
     * set of replacement candidates) to hold this page, read the
     * page (using the appropriate method from {\em diskmgr} package) and pin it.
     * Also, must write out the old page in chosen frame if it is dirty
     * before reading new page.__ (You can assume that emptyPage==false for
     * this assignment.)
     *
     * @param pageno    page number in the Minibase.
     * @param page      the pointer point to the page.
     * @param emptyPage true (empty page); false (non-empty page)
     */
    public void pinPage(PageId pageno, Page page, boolean emptyPage /*assume false*/)
            throws ChainException {
        //check if already exists in pool.
        if (!mBuffer.containsKey(pageno)) {
            //Is not loaded in memory.

            //Load page into replacement candidate. (while writing old page if dirty).
            try {
                checkBufferSpaceAvailable();
                Page p = new Page();
                Minibase.DiskManager.read_page(pageno, p);
                mBuffer.put(new PageId(pageno.pid), new Frame(new PageId(pageno.pid), p));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FileIOException e) {
                e.printStackTrace();
            }
        }
        mBuffer.get(pageno).pin();
        page.setPage(mBuffer.get(pageno).getPage());
    }

    /**
     * Implementation of the replacement policy.
     * This method requests that 1 item be removed from mBuffer and
     * flushed if dirty. This will fail if all frames are pinned.
     */
    protected void pruneBuffer() throws BufferPoolExceededException {
        if (getNumPinned() == getNumBuffers()) throw new BufferPoolExceededException();

        PageId victim = selectVictim();

        if (victim == null)
            throw new BufferPoolExceededException();

        try {
            flushPage(victim);
            mBuffer.remove(victim);
        } catch (InvalidPageNumberException e) {
            e.printStackTrace();
        } catch (PagePinnedException e) {
            e.printStackTrace();
        }
    }

    protected PageId selectVictim() {
        Map.Entry<PageId, Frame> lowestFreq = null;
        for (Map.Entry<PageId, Frame> entry : mBuffer.entrySet()) {
            Frame frame = entry.getValue();
            if (!frame.isPinned()) {
                if (lowestFreq == null) lowestFreq = entry;
                else if (frame.getFrequencyCount() < lowestFreq.getValue().getFrequencyCount()) {
                    lowestFreq = entry;
                }
            }
        }
        return (lowestFreq == null) ? null : lowestFreq.getKey();
    }

    /**
     * Unpin a page specified by a pageId.
     * This method should be called with dirty==true if the client has
     * modified the page.
     * If so, this call should set the dirty bit
     * for this frame.
     * Further, if pin_count>0, this method should
     * decrement it.
     * If pin_count=0 before this call, throw an exception
     * to report error.
     * (For testing purposes, we ask you to throw
     * an exception named PageUnpinnedException in case of error.)
     *
     * @param pageno page number in the Minibase.
     * @param dirty  the dirty bit of the frame
     */
    public void unpinPage(PageId pageno, boolean dirty) throws ChainException /*, PageUnpinnedException*/ {
        Frame frame = mBuffer.get(pageno);
        if (frame == null) throw new HashEntryNotFoundException();
        frame.unpin(); //throws PageUnpinnedException if page is not pinned.
        if (dirty) {
            frame.markDirty();
        }
    }

    /**
     * Allocate new pages.
     * Call DB object to allocate a run of new pages and
     * find a frame in the buffer pool for the first page
     * and pin it. (This call allows a client of the Buffer Manager
     * to allocate pages on disk.) If buffer is full, i.e., you
     * can't find a frame for the first page, ask DB to deallocate
     * all these pages, and return null.
     *
     * @param firstpage the address of the first page.
     * @param howmany   total number of allocated new pages.
     * @return the first page id of the new pages.__ null, if error.
     */
    public PageId newPage(Page firstpage, int howmany) {
        PageId pageId = null;
        //allocate page in diskManager.
        try {
            //Load first page into memory
            //pin it.
            pageId = Minibase.DiskManager.allocate_page(howmany);
            pinPage(pageId, firstpage, false);
            return pageId;
        } catch (Exception e) {
            if (pageId != null) {
                try {
                    Minibase.DiskManager.deallocate_page(pageId, howmany);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                mBuffer.remove(pageId);
            }
        }
        return null;
    }

    protected void checkBufferSpaceAvailable() throws BufferPoolExceededException {
        if (mBuffer.size() == getNumBuffers()) {
            pruneBuffer(); //throws IllegalStateException if all pinned.
        }
    }

    /**
     * This method should be called to delete a page that is on disk.
     * This routine must call the method in diskmgr package to
     * deallocate the page.
     *
     * @param globalPageId the page number in the data base.
     */
    public void freePage(PageId globalPageId) throws ChainException {
        //QUESTION: Should this check/clear the buffer? Should it respect pinned status?
        //run diskmgr.deallocate*
        if (mBuffer.containsKey(globalPageId)) {
            if (mBuffer.get(globalPageId).isPinned()) {
                throw new PagePinnedException();
            }
            mBuffer.remove(globalPageId);
        }
        Minibase.DiskManager.deallocate_page(globalPageId);
    }

    /**
     * Used to flush a particular page of the buffer pool to disk.
     * This method calls the write_page method of the diskmgr package.
     *
     * @param pageid the page number in the database.
     */
    public void flushPage(PageId pageid) throws PagePinnedException, InvalidPageNumberException {
        //call write_page to write page to disk
        if (mBuffer.containsKey(pageid)) {
            Frame frame = mBuffer.get(pageid);
            if (frame.isPinned()) {
                throw new PagePinnedException(); // Cannot flush a pinned page!
            }
            if (frame.isDirty()) {
                writeToDisk(pageid, frame.getPage());
                frame.setClean();
            }
        }
    }

    protected void writeToDisk(PageId pid, Page page) throws InvalidPageNumberException {
        try {
            Minibase.DiskManager.write_page(pid, page);
        } catch (FileIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to flush all dirty pages in the buffer pool to disk
     */
    public void flushAllPages() throws PagePinnedException, InvalidPageNumberException {
        //loop app pages in buffer and write dirty to disk
        for (Map.Entry<PageId, Frame> entry : mBuffer.entrySet()) {
            flushPage(entry.getKey());
        }
    }

    /**
     * Returns the total number of buffer frames.
     */
    public int getNumBuffers() {
        //return numBufs
        return mNumberOfBuffers;
    }

    /**
     * Returns the total number of unpinned buffer frames.
     */
    public int getNumUnpinned() {
//        int numUnPinned = 0;
//        for(Map.Entry<PageId, Frame> entry : mBuffer.entrySet()) {
//            if (!entry.getValue().isPinned()) {
//                numUnPinned++;
//            }
//        }
        return mNumberOfBuffers - getNumPinned();
    }

    public int getNumPinned() {
        int numPinned = 0;
        for (Map.Entry<PageId, Frame> entry : mBuffer.entrySet()) {
            if (entry.getValue().isPinned()) {
                numPinned++;
            }
        }
        return numPinned;
    }
}
