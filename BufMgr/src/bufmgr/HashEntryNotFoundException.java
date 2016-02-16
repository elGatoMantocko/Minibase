package bufmgr;

import chainexception.ChainException;

/**
 * Thrown when unpinning a pageid that is not in the buffer.
 */
public class HashEntryNotFoundException extends ChainException {
    public HashEntryNotFoundException() {
    }
}
