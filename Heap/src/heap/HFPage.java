package heap;

import global.Page;
import global.GlobalConst;
import global.PageId;
import global.RID;

/**
 * Created by david on 2/5/16.
 */
public class HFPage extends Page implements GlobalConst {

  // there should be a collection of <PID, SlotNum> RIDs here

  public HFPage() {
    // slot count
    setShortValue((short)0, 0);

    // last used pointer to a record
    setShortValue((short)PAGE_SIZE, 2);

    // free space
    setShortValue((short)1004, 4);

    // prev page
    setIntValue(-1, 8);
    
    // next page
    setIntValue(-1, 12);
    
    // curr page
    setIntValue(-1, 16);
  }

  public HFPage(Page page) {
    super(page.getData());
  }

  //    Gets the current page's id.
  public PageId getCurPage() {
    return new PageId(getIntValue(16));
  }

  //    Sets the current page's id.
  public void setCurPage(PageId pageno) {
    setIntValue(pageno.pid, 16);
  }

  //    Gets the next page's id.
  public PageId getNextPage() {
    return new PageId(getIntValue(12));
  }

  //    Sets the next page's id.
  public void setNextPage(PageId pageno) {
    setIntValue(pageno.pid, 12);
  }

  //    Gets the previous page's id.
  public PageId getPrevPage() {
    return new PageId(getIntValue(16));
  }

  //    Sets the previous page's id.
  public void setPrevPage(PageId pageno) {
    setIntValue(pageno.pid, 16);
  }

  //Deletes a record from the page, compacting the records space.
  void deleteRecord(RID rid) {

  }

  //    Gets the RID of the first record on the page, or null if none.
  public RID firstRecord() {
    short slotcount = getShortValue(0);
    short slotlength;

    int i;
    for (i = 0; i < slotcount; i++) {
      slotlength = getSlotLength(i);
      // find the first slot that has a record
      if (slotlength != -1) {
        break;
      }
    }

    // if slotcount is 0 then there is no record on the page
    if (i == slotcount) { 
      return null;
    }
    // return the rid of the current page id with the found slot number
    else {
      return new RID(new PageId(getIntValue(16)), i);
    }
  }

  //    Gets the amount of free space (in bytes).
  public short getFreeSpace() {
    return getShortValue(4);
  }

  //    Gets the number of slots on the page.
  public short getSlotCount() {
    return getShortValue(0);
  }

  //    Gets the length of the record referenced by the given slot.
  public short getSlotLength(int slotno) {
    return getShortValue(20 + 4 * slotno);
  }

  //    Gets the offset of the record referenced by the given slot.
  public short getSlotOffset(int slotno) {
    return 0;
  }

  //  Returns true if the iteration has more elements.
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

