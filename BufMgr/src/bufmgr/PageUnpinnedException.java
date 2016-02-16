package bufmgr;

import chainexception.ChainException;

/**
 * This is called when unpinning a page that is not pinned.
 */
public class PageUnpinnedException extends ChainException {
    public PageUnpinnedException() {

    }
}
