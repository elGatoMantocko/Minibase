package bufmgr;

import global.Page;

/**
 * Created by david on 2/9/16.
 */
public class Frame {
    protected boolean isDirty;
    protected Page page;
    protected int pinCount;

    public Frame() {
        this.isDirty = false;
        this.pinCount = 0;
    }

    public Frame(Page page) {
        this();
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
    }

    public void unpin() {
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
}
