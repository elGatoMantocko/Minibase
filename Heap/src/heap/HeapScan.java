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

    PageId mFirstPage;

    protected HeapScan(HeapFile hf) {
        mHeapFile = hf;
        mPagesItr = hf.getDirectory().values().iterator();

        HFPage page = new HFPage();
        mFirstPage = mPagesItr.next();
        Minibase.BufferManager.pinPage(mFirstPage, page, false);
        mCurrentRecord = page.firstRecord();
    }

    public void close() throws ChainException {
        Minibase.BufferManager.unpinPage(mFirstPage, false);
    }

    public Tuple getNext(RID rid) {

        if(hasNext()) {
            HFPage page = new HFPage();
            Minibase.BufferManager.pinPage(mCurrentRecord.pageno, page, false);
            if(page.hasNext(mCurrentRecord)) {

                rid.copyRID(mCurrentRecord);
                int length = page.selectRecord(mCurrentRecord).length;
                Tuple t = new Tuple(page.selectRecord(mCurrentRecord), 0, length);

                mCurrentRecord = page.nextRecord(mCurrentRecord);
                return t;
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
