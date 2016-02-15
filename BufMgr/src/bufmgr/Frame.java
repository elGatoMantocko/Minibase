package bufmgr;

import global.Page;
import global.PageId;

import java.util.Arrays;

/**
 * Created by david on 2/9/16.
 */
public class Frame {
    protected boolean isDirty;
    protected PageId pageid;
    protected Page page;
    protected int pinCount;
    protected short frequencyCount;

    public Frame() {
        this.isDirty = false;
        this.pinCount = 0;
    }

    public Frame(PageId id, Page page) {
        this();
        this.pageid = id;
        this.page = page;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public int getPinCount() {
        return pinCount;
    }

    public void pin() {
        pinCount++;
        bumpFrequencyCount();
    }

    public void unpin() throws PageUnpinnedException {
        if(getPinCount() == 0) {
            throw new PageUnpinnedException();
        }
        pinCount--;
    }

    public void markDirty() {
        isDirty = true;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public PageId getPageId() {
        return pageid;
    }

    public boolean isPinned() {
        return pinCount > 0;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(!(object instanceof Frame)) return false;
        Frame frame = (Frame) object;
        return this.isDirty == frame.isDirty() &&
                this.pageid == frame.getPageId() &&
                this.pinCount == frame.getPinCount() &&
                this.hasSamePage(frame);
    }

    /**
     * Page has no equals method, and since it is a library class we need
     * to implement this check here.
     * @param that The other frame we are comparing against.
     * @return true/false: the equality of this.page and that.page.
     */
    public boolean hasSamePage(Frame that) {
        return this.containsSamePage(that.getPage());
    }

    /**
     * Similar to the above, this checks the equality of the page in
     * the frame to the given page.
     * @param p the page to compare
     * @return The equality of the page.
     */
    public boolean containsSamePage(Page p) {
        if(this.page == p) return true;
        return Arrays.equals(this.page.getData(), p.getData());
    }

    private void bumpFrequencyCount() {
        frequencyCount++;
    }

    public short getFrequencyCount() {
        return frequencyCount;
    }

    public void setClean() {
        this.isDirty = false;
    }
}

