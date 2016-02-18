package heap;

import chainexception.ChainException;
import global.GlobalConst;
import global.Minibase;
import global.PageId;
import global.RID;

import java.nio.Buffer;
import java.util.Iterator;

/**
 * Created by david on 2/5/16.
 */
public class HeapScan implements GlobalConst {
    HeapFile mHeapFile;
    Iterator<PageId> mPagesItr;
    RID mCurrentRecord;

    protected HeapScan(HeapFile hf) {
        mHeapFile = hf;
        mPagesItr = hf.getDirectory().values().iterator();

        HFPage page = new HFPage();
        PageId pid = mPagesItr.next();
        Minibase.BufferManager.pinPage(pid, page, false);
        mCurrentRecord = page.firstRecord();
        Minibase.BufferManager.unpinPage(pid, false);
    }

    public void close() throws ChainException {

    }

    public Tuple getNext(RID rid) {

        if(hasNext()) {
            HFPage page = new HFPage();
            Minibase.BufferManager.pinPage(mCurrentRecord.pageno, page, false);
            if(page.hasNext(mCurrentRecord)) {
                RID nextRecord = page.nextRecord(mCurrentRecord);
                rid.copyRID(nextRecord);
                Minibase.BufferManager.pinPage(mCurrentRecord.pageno, page, false);

                return new Tuple(page.selectRecord(nextRecord), 0, nextRecord.getLength());
            } else {
                Minibase.BufferManager.unpinPage(mCurrentRecord.pageno, false);

                PageId pid = mPagesItr.next();
                Minibase.BufferManager.pinPage(pid, page, false);
                mCurrentRecord = page.firstRecord();
                Minibase.BufferManager.unpinPage(pid, false);

                return getNext(rid);
            }
        } else
            return null;
    }

    public boolean hasNext() {
        HFPage page = new HFPage();
        Minibase.BufferManager.pinPage(mCurrentRecord.pageno, page, false);
        if(page.hasNext(mCurrentRecord)) {
            return true;
        } else if(mPagesItr.hasNext()) {
            return true;
        }
        return false;
    }
}
