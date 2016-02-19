package heap;

import global.Minibase;
import global.PageId;
import global.Page;
import global.GlobalConst;
import global.RID;
import global.Convert;

import java.util.*;
import java.util.Map.Entry;

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

  private PageId firstid;

  private String filename;

  private MultipleValueTreeMap directory;

  private int reccnt;

  public HeapFile(String name) throws Exception {
    // this is a map of pages not records
    // <available_space, pageno>
    directory = new MultipleValueTreeMap();
    this.filename = name;

    if (filename != null) {
      HFPage page = new HFPage();

      try {
        // see if there is already a heapfile with the name
        firstid = Minibase.DiskManager.get_file_entry(filename);

        // if not
        if (firstid == null) {
          // create the first directory page
          firstid = Minibase.BufferManager.newPage(page, 1);

          // hook up the first page
          page.setCurPage(firstid);
          PageId badPID = new PageId(INVALID_PAGEID);
          page.setNextPage(badPID);
          page.setPrevPage(badPID);

          Minibase.BufferManager.unpinPage(firstid, true);

          // add the file to the database
          Minibase.DiskManager.add_file_entry(filename, firstid);
        }
        else {
          // pin the first page
          Minibase.BufferManager.pinPage(firstid, page, false);

          PageId currentPid = firstid;
          RID keyrec = page.firstRecord();
          RID valuerec = page.nextRecord(keyrec);
          while (keyrec != null) {
            if (valuerec != null) {
              directory.put(Convert.getShortValue(0, page.selectRecord(keyrec)), new PageId(Convert.getIntValue(0, page.selectRecord(valuerec))));
            }
            if (page.hasNext(valuerec)) {
              keyrec = page.nextRecord(valuerec);
              valuerec = page.nextRecord(keyrec);
            }
            else if (page.getNextPage() != null) {
              // we found another page
              currentPid = page.getNextPage();
              Minibase.BufferManager.unpinPage(page.getCurPage(), false);;
              Minibase.BufferManager.pinPage(currentPid, page, false);
              keyrec = page.firstRecord();
              valuerec = page.nextRecord(keyrec);
            }
          }
          
          Minibase.BufferManager.unpinPage(page.getCurPage(), false);;

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
    writeHeader();
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
      writeHeader();
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

    writeHeader();
    return true;
  }

  public boolean deleteRecord(RID rid) throws ChainException {
    HFPage currentPage = new HFPage();
    try {
      Minibase.BufferManager.pinPage(rid.pageno, currentPage, false);

      // update the key value pair in the directory
      directory.remove(currentPage.getFreeSpace(), rid.pageno);

      currentPage.deleteRecord(rid);

      if (currentPage.getSlotCount() < 1) {
        Minibase.DiskManager.deallocate_page(rid.pageno);
      }

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

  private void writeHeader() {
    HFPage page = new HFPage();
    HFPage header = new HFPage();
    Minibase.BufferManager.pinPage(firstid, header, false);

    RID keyrec = header.firstRecord();
    RID valuerec = header.nextRecord(keyrec);

    for ( Map.Entry<Short, PageId> entry : directory.entrySet()) {
      byte[] keydata = new byte[2];
      byte[] valuedata = new byte[4];
      Convert.setShortValue(entry.getKey(), 0, keydata);
      Convert.setIntValue(entry.getValue().pid, 0, valuedata);
      Tuple keytup = new Tuple(keydata, 0 , keydata.length);
      Tuple valuetup = new Tuple(valuedata, 0 , valuedata.length);

      if (header.selectRecord(keyrec) != null && header.selectRecord(valuerec) != null) {
        header.updateRecord(keyrec, keytup);
        header.updateRecord(valuerec, valuetup);
      }
      else if (header.getNextPage() != null) {
        Minibase.BufferManager.pinPage(header.getNextPage(), header, false);
        Minibase.BufferManager.unpinPage(header.getPrevPage(), true);

        if (header.selectRecord(keyrec) != null) {
          header.updateRecord(keyrec, keytup);
          header.updateRecord(valuerec, valuetup);
        }
      }
      
      keyrec = header.nextRecord(valuerec);
      valuerec = header.nextRecord(keyrec);
    }

  }
}

