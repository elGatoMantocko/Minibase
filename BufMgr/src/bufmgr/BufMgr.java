package bufmgr;

import chainexception.ChainException;
import diskmgr.FileIOException;
import diskmgr.InvalidPageNumberException;
import global.Minibase;
import global.Page;
import global.PageId;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.*;

/**
 * Created by david on 2/3/16.
 */
public class BufMgr {
    private final int mNumBufs;
    Map<PageId, Frame> mBuffer;

    /**
     * Create the BufMgr object.
     * Allocate pages (frames) for the buffer pool in main memory and
     * make the buffer manage aware that the replacement policy is
     * specified by replacerArg (e.g., LH, Clock, LRU, MRU, LFU, etc.).
     *
     * @param numbufs           number of buffers in the buffer pool
     * @param lookAheadSize:    Please ignore this parameter
     * @param replacementPolicy Name of the replacement policy, that parameter will be set to "LFU" (you
     *                          can safely ignore this parameter as you will implement only one policy)
     */
    public BufMgr(int numbufs, int lookAheadSize, String replacementPolicy) {
        //Allocate an array of buffers with given size.
        mBuffer = new HashMap<PageId, Frame>();
        this.mNumBufs = numbufs;
        //save numbufs.


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
            throws ChainException, InvalidPageNumberException {
        //check if already exists in pool.
        if(!mBuffer.containsKey(pageno)) {
            //Is not loaded in memory.

            //Load page into replacement candidate. (while writing old page if dirty).
            try {
                checkBufferSpaceAvailable();
                Minibase.DiskManager.read_page(pageno, page);
                mBuffer.put(pageno, new Frame(pageno, page)); //TODO: Need to limit the frames.
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FileIOException e) {
                e.printStackTrace();
            }
        }
        mBuffer.get(pageno).pin();
    }

    /**
     * Implementation of the replacement policy.
     * This method requests that i items be removed from mBuffer and
     * flushed if dirty. This will fail if all frames are pinned.
     * @param i
     */
    private void clearFrames(int i) throws BufferPoolExceededException {
        if(getNumPinned() == getNumBuffers()) throw new BufferPoolExceededException();

        Map.Entry<PageId, Frame> lowestFreq = null;
        for(Map.Entry<PageId, Frame> entry: mBuffer.entrySet()) {
            Frame frame = entry.getValue();
            if(!frame.isPinned()) {
                if(lowestFreq == null) lowestFreq = entry;
                else if(frame.getFrequencyCount() < lowestFreq.getValue().getFrequencyCount()) {
                    lowestFreq = entry;
                }
            }
        }
        if(lowestFreq == null)
            throw new BufferPoolExceededException();

        try {
            flushPage(lowestFreq.getKey());
        } catch (InvalidPageNumberException e) {
            e.printStackTrace();
        } catch (PagePinnedException e) {
            e.printStackTrace();
        }
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
        if(frame == null) throw new HashEntryNotFoundException();
        frame.unpin(); //throws PageUnpinnedException if page is not pinned.
        if(dirty) {
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
    public PageId newPage(Page firstpage, int howmany) throws ChainException {
        checkBufferSpaceAvailable();

            //allocate page in diskManager.
        try {
            //Load first page into memory
            //pin it.
            PageId pageId = Minibase.DiskManager.allocate_page(howmany);
            pinPage(pageId, firstpage, false);
            return pageId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkBufferSpaceAvailable() throws BufferPoolExceededException {
        if(mBuffer.size() == getNumBuffers()) {
            clearFrames(1); //throws IllegalStateException if all pinned.
        }
        if(mBuffer.size() == getNumBuffers()) {
            throw new BufferPoolExceededException();
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
        if(mBuffer.containsKey(globalPageId)) {
            if(mBuffer.get(globalPageId).isPinned()) {
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
        if(mBuffer.containsKey(pageid)) {
            Frame frame = mBuffer.get(pageid);
            if(frame.isPinned()) {
                throw new PagePinnedException(); // Cannot flush a pinned page!
            }
            if(frame.isDirty()) {
                writeToDisk(pageid, frame.getPage());
                mBuffer.remove(pageid);
            }
        }
    }

    private void writeToDisk(PageId pid, Page page) throws InvalidPageNumberException {
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
        //QUESTION: Do we remove them from the buffer or just set to no longer dirty?
        ArrayList<PageId> pageIds = new ArrayList<PageId>();

        for(Map.Entry<PageId, Frame> entry : mBuffer.entrySet()) {
            pageIds.add(entry.getKey());
        }
        for(PageId pid : pageIds) flushPage(pid);
        mBuffer.clear();
    }

    /**
     * Returns the total number of buffer frames.
     */
    public int getNumBuffers() {
        //return numBufs
        return mNumBufs;
    }

    /**
     * Returns the total number of unpinned buffer frames.
     */
    public int getNumUnpinned() {
        return getNumBuffers() - getNumPinned();
    }

    public int getNumPinned() {
        int numPinned = 0;
        for(Map.Entry<PageId, Frame> entry : mBuffer.entrySet()) {
            if (entry.getValue().isPinned()) {
                numPinned++;
            }
        }
        return numPinned;
    }
}
