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

    PageId mCurrentPage;

    protected HeapScan(HeapFile hf) {
        mHeapFile = hf;
        mPagesItr = hf.getDirectory().values().iterator();
    }

    public void close() throws ChainException {
        Minibase.BufferManager.unpinPage(mFirstPage, false);
    }

    public Tuple getNext(RID rid) {

        if(hasNext()) {
            if(mCurrentPage != null && mCurrentRecord != null) {
                HFPage page = new HFPage();

                Minibase.BufferManager.pinPage(mCurrentPage, page, false);

                RID toReturn = page.nextRecord(mCurrentRecord);
                rid.copyRID(toReturn);

                int length = page.selectRecord(toReturn).length;
                Tuple t = new Tuple(page.selectRecord(toReturn), 0, length);

                mCurrentRecord = toReturn;
                return t;
            } else {
                mCurrentPage = mPagesItr.next();
                HFPage page = new HFPage();

                Minibase.BufferManager.pinPage(mCurrentPage, page, false);

                mCurrentRecord = page.firstRecord();

                rid.copyRID(mCurrentRecord);

                int length = page.selectRecord(mCurrentRecord).length;
                Tuple t = new Tuple(page.selectRecord(mCurrentRecord), 0, length);

                return t;
            }
            //if current page is valid.
                //page.nextRecord()
                //return
            //else not valid
                //pagesitr.nextPage()
                //pin
                //record = first record
                //getRecord
                //return
        } else
            return null;
    }

    public boolean hasNext() {
        if(mCurrentRecord == null) return true;

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
