package heap;

import global.GlobalConst;
import global.RID;

/**
 * Created by david on 2/5/16.
 */
public class HeapFile implements GlobalConst {
    public HeapFile(String name) {

    }

    public RID insertRecord(byte[] record) {
        return null;
    }

    public Tuple getRecord(RID rid) {
        return null;
    }

    public void updateRecord(RID rid, Tuple newRecord) {

    }

    public void deleteRecord(RID rid) {

    }

    //get number of records in the file
    public int getRecCnt() {
        return 0;
    }

    public HeapScan openScan() {
        return null;
    }
}
