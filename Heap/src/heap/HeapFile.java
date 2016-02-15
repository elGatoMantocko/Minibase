package heap;

import global.Minibase;
import global.PageId;
import global.Page;
import global.GlobalConst;
import global.RID;

import chainexception.ChainException;
import com.sun.net.httpserver.Filter;

/**
 * Created by david on 2/5/16.
 */

/**
 * Create a new HeapFile opject
 *  
 * @exception IOException 
 **/
public class HeapFile implements GlobalConst {
  private HFPage hfpage;
  private PageId firstid;
  String filename;

  public HeapFile(String name) throws Exception {
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
      }
    }
  }

  public RID insertRecord(byte[] record) throws ChainException {
    boolean found;

    HFPage curDirPage = new HFPage();
    PageId curDirPid = new PageId(firstid.pid);

    HFPage curDataPage = new HFPage();
    Minibase.BufferManager.pinPage(curDirPid, curDirPage, false);

    found = false;
    byte[] data;

    // find space for the record
    // while we haven't guaranteed a slot for the record
    while (found == false) {
      // loop over the records in the current data page
      for (RID curDataPageRID = curDirPage.firstRecord(); 
          curDataPageRID != null; 
          curDataPageRID = curDirPage.nextRecord(curDataPageRID)) {
        data = curDirPage.selectRecord(curDataPageRID);
      }
    }

    RID rid;
    rid = curDataPage.insertRecord(record);

    Minibase.BufferManager.unpinPage(curDirPid, true);

    return rid;
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
