package heap;

import global.Minibase;
import global.PageId;
import global.Page;
import global.GlobalConst;
import global.RID;

import chainexception.ChainException;

/**
 * Create a new HeapFile opject
 **/
public class HeapFile implements GlobalConst {
  private PageId firstid;

  private String filename;

  private MultipleValueTreeMap directory;

  private int reccnt;

  public HeapFile(String name) throws Exception {
    // this is a map of pages not records
    // <available_space, pageno>
    directory = new MultipleValueTreeMap();
    this.filename = name;
    boolean exists = true;

    if (name != null) {
      Page p = new Page();

      try {
        firstid = Minibase.DiskManager.get_file_entry(name);
        if (firstid == null) {
          firstid = Minibase.BufferManager.newPage(p, 1);
          Minibase.DiskManager.add_file_entry(filename, firstid);

          HFPage hfp = new HFPage(p);
          hfp.setCurPage(firstid);

          PageId badPID = new PageId(INVALID_PAGEID);
          hfp.setNextPage(badPID);
          hfp.setPrevPage(badPID);

          Minibase.BufferManager.unpinPage(firstid, true);
        }
        else {
          Minibase.BufferManager.unpinPage(firstid, true);
        }
      } catch(Exception e){
        e.printStackTrace();
        throw new InvalidUpdateException();
      }
    }
  }

  public RID insertRecord(byte[] record) throws ChainException {

    if (record.length > PAGE_SIZE) {
      throw new ChainException();
    }

    // find the smallest key in directory that 
    //  is larger than record.length
    Short rlength = (short)record.length;
    HFPage curDataPage = new HFPage();
    Short index = directory.getTreeMap().higherKey(rlength);

    RID newRecord;

    // we couldn't find a suitable page!
    if (index == null) {
      // so now we need to make one and insert it into the db
      //  make sure not to forget to add the page into directory
      HFPage newPage = new HFPage();
      PageId newPageId = Minibase.BufferManager.newPage(newPage, 1);
      newPage.setCurPage(newPageId);
      newPage.setPrevPage(new PageId(INVALID_PAGEID));
      newPage.setNextPage(new PageId(INVALID_PAGEID));
      newRecord = newPage.insertRecord(record);

      // debug print
      //System.out.println("pageno: " + newRecord.pageno.pid + "\tslotno: " + newRecord.slotno);

      // update the directory
      directory.put(newPage.getFreeSpace(), newPageId);

      Minibase.BufferManager.unpinPage(newPageId, true);
    }
    else {
      // we first select a page that definetly has more space than 
      HFPage currentPage = new HFPage();
      PageId closestGuess = directory.get(index);
      Minibase.BufferManager.pinPage(closestGuess, currentPage, false);

      // update the directory
      directory.remove(currentPage.getFreeSpace(), closestGuess);

      newRecord = currentPage.insertRecord(record);

      // debug print
      // System.out.println("pageno: " + newRecord.pageno.pid + "\tslotno: " + newRecord.slotno);

      // there wasn't enough free space in this page
      //  for some stupid reason
      if (newRecord == null) {
        throw new ChainException();
      }

      // System.out.println(directory.get(currentPage.getFreeSpace()));
      directory.put(currentPage.getFreeSpace(), closestGuess);

      Minibase.BufferManager.unpinPage(closestGuess, true);
    }

    reccnt += 1;
    return newRecord;
  }

  public Tuple getRecord(RID rid) throws ChainException {
    PageId pid = rid.pageno;
    HFPage page = new HFPage();

    try {
      Minibase.BufferManager.pinPage(pid, page, false);
      byte[] rec = page.selectRecord(rid);
      Tuple t = new Tuple(rec, 0, rec.length);
      Minibase.BufferManager.unpinPage(pid, false);
      return t;
    } catch(Exception e){
      e.printStackTrace();
      throw new InvalidUpdateException();
    }
  }

  public boolean updateRecord(RID rid, Tuple newRecord) throws ChainException {
    HFPage currentPage = new HFPage();
    try {
      Minibase.BufferManager.pinPage(rid.pageno, currentPage, false);

      // update the key value pair in the directory
      directory.remove(currentPage.getFreeSpace(), rid.pageno);

      if (newRecord.getLength() > currentPage.getFreeSpace() + getRecord(rid).getLength()) {
        deleteRecord(rid);
        insertRecord(newRecord.getTupleByteArray());
      }
      else {
        deleteRecord(rid);
        currentPage.insertRecord(newRecord.getTupleByteArray());
      }

      directory.put(currentPage.getFreeSpace(), rid.pageno);

    } catch(Exception e){
      e.printStackTrace();
      throw new InvalidUpdateException();
    } finally {
      Minibase.BufferManager.unpinPage(rid.pageno, true);
    }

    return true;
  }

  public boolean deleteRecord(RID rid) throws ChainException {
    HFPage currentPage = new HFPage();
    try {
      Minibase.BufferManager.pinPage(rid.pageno, currentPage, false);

      // update the key value pair in the directory
      directory.remove(currentPage.getFreeSpace(), rid.pageno);

      currentPage.deleteRecord(rid);

      directory.put(currentPage.getFreeSpace(), rid.pageno);

    } catch(Exception e){
      e.printStackTrace();
      throw new InvalidUpdateException();
    } finally {
      Minibase.BufferManager.unpinPage(rid.pageno, true);
    }
  
    reccnt -= 1;
    return true;
  }

  //get number of records in the file
  public int getRecCnt() {
    return reccnt;
  }

  public HeapScan openScan() {
    return new HeapScan(this);
  }

  public MultipleValueTreeMap getDirectory() {
    return directory;
  }
}

