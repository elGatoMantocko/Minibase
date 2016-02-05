package heap;

import global.GlobalConst;
import global.RID;

/**
 * Created by david on 2/5/16.
 */
public class HeapScan implements GlobalConst {
    protected HeapScan(HeapFile hf) {

    }

    public void close() {

    }

    public Tuple getNext(RID rid) {
        return null;
    }

    public boolean hasNext() {
        return false;
    }
}
