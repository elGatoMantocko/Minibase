package heap;

import global.Page;
import global.GlobalConst;
import global.PageId;
import global.RID;

/**
 * Created by david on 2/5/16.
 */
public class HFPage extends Page implements GlobalConst {

  protected static final int SLOT_COUNT = 0;
  protected static final int USED_POINTER = 2;
  protected static final int FREE_SPACE = 4;

  protected static final int PREV_PAGE = 8;
  protected static final int NEXT_PAGE = 12;
  protected static final int CUR_PAGE = 16;

  protected static final int HEADER_SIZE = 20;
  protected static final int SLOT_SIZE = 4;

  // there should be a collection of <PID, SlotNum> RIDs here

  public HFPage() {
    setShortValue((short)0, SLOT_COUNT);
    setShortValue((short)PAGE_SIZE, USED_POINTER);
    setShortValue((short)(PAGE_SIZE - HEADER_SIZE), FREE_SPACE);

    setIntValue(-1, PREV_PAGE);
    setIntValue(-1, NEXT_PAGE);
    setIntValue(-1, CUR_PAGE);
  }

  public HFPage(Page page) {
    super(page.getData());
  }

  //    Gets the current page's id.
  public PageId getCurPage() {
    return new PageId(getIntValue(CUR_PAGE));
  }

  //    Sets the current page's id.
  public void setCurPage(PageId pageno) {
    setIntValue(pageno.pid, CUR_PAGE);
  }

  //    Gets the next page's id.
  public PageId getNextPage() {
    return new PageId(getIntValue(NEXT_PAGE));
  }

  //    Sets the next page's id.
  public void setNextPage(PageId pageno) {
    setIntValue(pageno.pid, NEXT_PAGE);
  }

  //    Gets the previous page's id.
  public PageId getPrevPage() {
    return new PageId(getIntValue(PREV_PAGE));
  }

  //    Sets the previous page's id.
  public void setPrevPage(PageId pageno) {
    setIntValue(pageno.pid, PREV_PAGE);
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
      return new RID(new PageId(getIntValue(CUR_PAGE)), i);
    }
  }

  //    Gets the amount of free space (in bytes).
  public short getFreeSpace() {
    return getShortValue(FREE_SPACE);
  }

  //    Gets the number of slots on the page.
  public short getSlotCount() {
    return getShortValue(0);
  }

  //    Gets the length of the record referenced by the given slot.
  public short getSlotLength(int slotno) {
    return getShortValue(20 + SLOT_SIZE * slotno);
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
    // make sure the record is not empty
    if (getSlotLength(rid.slotno) == EMPTY_SLOT) {
      throw new IllegalArgumentException("The record is empty");
    }

    short l = rid.getLength();
    byte rec[] = new byte[l];
    System.arraycopy(data, getSlotOffset(rid.slotno), rec, 0, l);
    return new Tuple(rec, 0, l);
  }

  //    Updates a record on the page.
  public void updateRecord(RID rid, byte[] record) {

  }
}

