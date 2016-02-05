package heap;

import global.PageId;
import global.RID;
import sun.jvm.hotspot.debugger.Page;

/**
 * Created by david on 2/5/16.
 */
public class HFPage extends Page {


    public HFPage(long baseAddress, long unmappedPageLength) {
        super(baseAddress, unmappedPageLength);
    }

    //TODO: Determine how to make the constructor
//    public heap.HFPage(Page page) {
//
//    }

    //Deletes a record from the page, compacting the records space.
    void deleteRecord(RID rid) {

    }

    //    Gets the RID of the first record on the page, or null if none.
    public RID firstRecord() {
        return null;
    }

    //    Gets the current page's id.
    public PageId getCurPage() {
        return null;
    }

    //    Sets the current page's id.
    public void setCurPage(PageId pageno) {

    }

    //    Gets the amount of free space (in bytes).
    public short getFreeSpace() {
        return 0;
    }

    //    Gets the next page's id.
    public PageId getNextPage() {
        return null;
    }

    //    Sets the next page's id.
    public void setNextPage(PageId pageno) {

    }

    //    Gets the previous page's id.
    public PageId getPrevPage() {
        return null;
    }

    //    Sets the previous page's id.
    public void setPrevPage(PageId pageno) {

    }

    //    Gets the number of slots on the page.
    public short getSlotCount() {
        return 0;
    }

    //    Gets the length of the record referenced by the given slot.
    public short getSlotLength(int slotno) {
        return 0;
    }

    //    Gets the offset of the record referenced by the given slot.
    public short getSlotOffset(int slotno) {
        return 0;
    }

    //    Returns true if the iteration has more elements.
    public boolean hasNext(RID curRid) {
        return true;
    }

    //    Inserts a new record into the page.
    public RID insertRecord(byte[] record) {
        return null;
    }

    //    Gets the next RID after the given one, or null if no more.
    public RID nextRecord(RID curRid) {
        return null;
    }

    //    Prints the contents of a heap file page.
    public void print() {

    }

    //    Selects a record from the page.
    public Tuple selectRecord(RID rid) {
        return null;
    }

    //    Updates a record on the page.
    public void updateRecord(RID rid, byte[] record) {

    }
}
