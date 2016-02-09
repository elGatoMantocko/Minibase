package bufmgr;

import global.Page;
import global.PageId;

/**
 * Created by david on 2/9/16.
 */
public class Frame {
    protected boolean isDirty;
    protected PageId pageid;
    protected Page page;
    protected int pinCount;

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
    }

    public void unpin() {
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
}

