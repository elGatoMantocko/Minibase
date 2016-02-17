package bufmgr;

import chainexception.ChainException;

/**
 * Thrown when trying to free or otherwise remove a page from the buffer that is pinned.
 */
public class PagePinnedException extends ChainException {
    public PagePinnedException() {
    }
}
