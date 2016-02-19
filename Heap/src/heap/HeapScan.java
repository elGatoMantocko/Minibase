package heap;

import chainexception.ChainException;
import global.*;

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

    Page mFirstPagePage = new Page();

    PageId mCurrentPage;

    protected HeapScan(HeapFile hf) {
        mHeapFile = hf;
        mPagesItr = hf.getDirectory().values().iterator();

        mFirstPage = hf.getDirectory().entrySet().iterator().next().getValue();
        Minibase.BufferManager.pinPage(mFirstPage, mFirstPagePage, false);
    }

    public void close() throws ChainException {
        Minibase.BufferManager.unpinPage(mFirstPage, false);
        Minibase.BufferManager.unpinPage(mCurrentPage, false);
    }

    public Tuple getNext(RID rid) {

        PageId pinnedCurrentPage = null;
        if(hasNext()) {

            HFPage foopage = null;
            if(mCurrentPage != null) {
                foopage = new HFPage();
                pinnedCurrentPage = mCurrentPage;
                Minibase.BufferManager.pinPage(pinnedCurrentPage, foopage, false);
            }

            if(mCurrentPage != null && mCurrentRecord != null && foopage.hasNext(mCurrentRecord)) {
                HFPage page = new HFPage();

                Minibase.BufferManager.pinPage(mCurrentPage, page, false);

                RID toReturn = page.nextRecord(mCurrentRecord);
                rid.copyRID(toReturn);

                int length = page.selectRecord(toReturn).length;
                Tuple t = new Tuple(page.selectRecord(toReturn), 0, length);

                mCurrentRecord = toReturn;

                Minibase.BufferManager.unpinPage(pinnedCurrentPage, false);
                Minibase.BufferManager.unpinPage(mCurrentPage, false);

                if(hasNext() == false) {
                    try {
                        close();
                    } catch (ChainException e) {
                        e.printStackTrace();
                    }
                }
                return t;
            } else {
                if(foopage != null && !foopage.hasNext(mCurrentRecord)) {
                    Minibase.BufferManager.unpinPage(pinnedCurrentPage, false);
                }
                mCurrentPage = mPagesItr.next();
                HFPage page = new HFPage();

                Minibase.BufferManager.pinPage(mCurrentPage, page, false);

                mCurrentRecord = page.firstRecord();

                rid.copyRID(mCurrentRecord);

                int length = page.selectRecord(mCurrentRecord).length;
                Tuple t = new Tuple(page.selectRecord(mCurrentRecord), 0, length);

                if(pinnedCurrentPage != null)
                    Minibase.BufferManager.unpinPage(pinnedCurrentPage, false);
                Minibase.BufferManager.unpinPage(mCurrentPage, false);

                if(hasNext() == false) {
                    try {
                        close();
                    } catch (ChainException e) {
                        e.printStackTrace();
                    }
                }
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
        Minibase.BufferManager.pinPage(mCurrentPage, page, false);
        boolean toReturn = page.hasNext(mCurrentRecord) || mPagesItr.hasNext();
        Minibase.BufferManager.unpinPage(mCurrentPage, false);
        return toReturn;
    }
}
