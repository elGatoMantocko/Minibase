package heap;

import global.Minibase;
import global.PageId;
import global.Page;
import global.GlobalConst;
import global.RID;
import global.Convert;

import java.util.TreeMap;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Iterator;

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

  TreeMap<Short, LinkedList<RID>> directory;

  long reccnt;

  public HeapFile(String name) throws Exception {
    // this is a map of pages not records
    // <available_space, pageno>
    directory = new TreeMap<Short, LinkedList<RID>>();
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
          Minibase.BufferManager.flushPage(firstid);
        }
        else {
          Minibase.BufferManager.unpinPage(firstid, false);
        }
      } catch(Exception e){
        e.printStackTrace();
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
    Short index = directory.higherKey(rlength);

    LinkedList<RID> recordlist;

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
      recordlist = directory.get(newPage.getFreeSpace());

      // initialize the recordlist if it hasn't been yet
      if (recordlist == null) {
        recordlist = new LinkedList<RID>();
      }
      
      // add the record to the list and update the directory
      recordlist.offer(newRecord);
      directory.put(newPage.getFreeSpace(), recordlist);

      Minibase.BufferManager.unpinPage(newPageId, true);
      Minibase.BufferManager.flushPage(newPageId);
    }
    else {
      // we first select a page that definetly has more space than 
      HFPage currentPage = new HFPage();
      recordlist = directory.get(index);

      // Instead of iterating over the list of records we just peek
      // onto the first element
      //     if for some reason it fails and there isn't an availabe slot then we can go to the next one
      //     else we just make a new page

      RID rec = recordlist.peek();

      Minibase.BufferManager.pinPage(rec.pageno, currentPage, false);
      newRecord = currentPage.insertRecord(record);

      // debug print
      // System.out.println("pageno: " + newRecord.pageno.pid + "\tslotno: " + newRecord.slotno);

      // there wasn't enough free space in this page
      //  for some stupid reason
      if (newRecord == null) {
        // this is bad so we have to add the record back to the list
        throw new ChainException();
      }

      directory.remove(index);
      directory.put(currentPage.getFreeSpace(), recordlist);

      Minibase.BufferManager.unpinPage(rec.pageno, true);
      Minibase.BufferManager.flushPage(rec.pageno);
    }

    return newRecord;
  }

  public Tuple getRecord(RID rid) {
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
    }
    
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
    //return new HeapScan(firstid, this);
    return null;
  }
}

