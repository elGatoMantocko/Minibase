package heap;

import chainexception.ChainException;
import com.sun.net.httpserver.Filter;
import global.GlobalConst;
import global.RID;

/**
 * Created by david on 2/5/16.
 */
public class HeapFile implements GlobalConst {
    public HeapFile(String name) {

    }

    public RID insertRecord(byte[] record) throws ChainException {
        return null;
    }

    public Tuple getRecord(RID rid) {
        return null;
    }

    public boolean updateRecord(RID rid, Tuple newRecord) throws ChainException {
        return false;
    }

    public boolean deleteRecord(RID rid) {
        return false;
    }

    //get number of records in the file
    public int getRecCnt() {
        return 0;
    }

    public HeapScan openScan() {
        return null;
    }
}
