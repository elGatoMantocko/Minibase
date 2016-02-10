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
public class HeapFile implements GlobalConst {
  private HFPage hfpage;
  String fileName;

  public HeapFile(String name) {

    this.fileName = name;

    boolean exists = true;
    PageId firstid;
    if (name != null) {
      if (Minibase.DiskManager.get_file_entry(name) == null) {
        Page p = new Page();
      }
    }
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
