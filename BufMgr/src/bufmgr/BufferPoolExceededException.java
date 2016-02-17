package bufmgr;

import chainexception.ChainException;

/**
 * Thrown when the buffer pool is both full and cannot be purged (ie all frames are pinned).
 */
public class BufferPoolExceededException extends ChainException {
    public BufferPoolExceededException() {
    }
}
