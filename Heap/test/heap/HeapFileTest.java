package heap;

import global.Page;
import global.PageId;
import global.Convert;
import global.GlobalConst;
import global.Minibase;
import global.RID;
import heap.HeapFile;
import heap.HeapScan;
import heap.Tuple;

import java.io.IOException;
import java.lang.IllegalArgumentException;

import chainexception.ChainException;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by david on 2/9/16.
 */
public class HeapFileTest {

  private final static boolean OK = true;
  private final static boolean FAIL = false;

  private final static int REC_LEN = 32;

  private HeapFile f;

  @Before
  public void createDatabase() throws Exception {
    new Minibase("test.minibase", 1000, 10, 0, "LFU", false);
  }

  @Test
  public void testNewHeapFile() {
    try {
      f = new HeapFile("file_1");
    }
    catch (Exception e) {
      fail("Could not create heap file");
    }
  }

  @Test
  public void testAllPagesUnpinned() {
    assertEquals("The heap file has left pages pinned", 
        Minibase.BufferManager.getNumUnpinned(), 
        Minibase.BufferManager.getNumBuffers());
  }

  // I think this should be a strong test?
  @Test
  public void testByteArrayOneHundo() {

    try {
      f = new HeapFile("file_1");
    } catch(Exception e){
      e.printStackTrace();
    }

    for (int i = 0; i < 100; i++) {
      DummyRecord rec = new DummyRecord(REC_LEN);
      rec.ival = i;
      rec.fval = (float) (i * 2.5);
      rec.name = "record" + i;

      try {
        RID rid = f.insertRecord(rec.toByteArray());
        assertArrayEquals(rec.toByteArray(), f.getRecord(rid).getTupleByteArray());
      } catch(Exception e){
        e.printStackTrace();
        fail();
      }
    }
  }

  @Test
  public void testTwoThousandRecords() {

    try {
      f = new HeapFile("file_1");
    } catch(Exception e){
      e.printStackTrace();
    }

    for (int i = 0; i < 2000; i++) {
      DummyRecord rec = new DummyRecord(REC_LEN);
      rec.ival = i;
      rec.fval = (float) (i * 2.5);
      rec.name = "record" + i;

      try {
        RID rid = f.insertRecord(rec.toByteArray());
        assertArrayEquals(rec.toByteArray(), f.getRecord(rid).getTupleByteArray());
      } catch(Exception e){
        e.printStackTrace();
        fail();
      }
    }
  }

  @Test
  public void testWrongNumInsertedRecords() {
    try {
      f = new HeapFile("file_1");
    } catch(Exception e){
      e.printStackTrace();
    }

    DummyRecord rec = new DummyRecord(REC_LEN);
    rec.ival = 0;
    rec.fval = (float) (0 * 2.5);
    rec.name = "record" + 0;

    try {
      f.insertRecord(rec.toByteArray());
    } catch(Exception e){
      e.printStackTrace();
    }

    assertEquals("Wrong amount of records inserted", f.getRecCnt(), 1);
  }

  @Test
  public void testUnpinnedAfterInsert() {
    try {
      f = new HeapFile("file_1");
    } catch(Exception e){
      e.printStackTrace();
    }

    DummyRecord rec = new DummyRecord(REC_LEN);
    rec.ival = 0;
    rec.fval = (float) (0 * 2.5);
    rec.name = "record" + 0;

    try {
      f.insertRecord(rec.toByteArray());
    } catch(Exception e){
      e.printStackTrace();
    }

    assertEquals("The heap file has left pages pinned", 
        Minibase.BufferManager.getNumUnpinned(), 
        Minibase.BufferManager.getNumBuffers());
  }

  @Test
  public void testUpdateRecord() {
    try {
      f = new HeapFile("file_1");
    } catch(Exception e){
      e.printStackTrace();
    }

    DummyRecord rec = new DummyRecord(REC_LEN);
    rec.ival = 0;
    rec.fval = (float) (0 * 2.5);
    rec.name = "record" + 0;

    try {
      f.insertRecord(rec.toByteArray());
    } catch(Exception e){
      e.printStackTrace();
    }

    assertEquals("Wrong amount of records inserted", f.getRecCnt(), 1);
  }

  @Test
  public void testDeleteRecord() {
    try {
      f = new HeapFile("file_1");
    } catch(Exception e){
      e.printStackTrace();
    }

    DummyRecord rec = new DummyRecord(REC_LEN);
    rec.ival = 0;
    rec.fval = (float) (0 * 2.5);
    rec.name = "record" + 0;

    try {
      RID newrec = f.insertRecord(rec.toByteArray());
      f.deleteRecord(newrec);

    } catch(Exception e){
      e.printStackTrace();
    }

    assertEquals("Wrong amount of records inserted", 0, f.getRecCnt());
    
  }

  @Test(expected=IllegalArgumentException.class)
  public void testNoRecordFound() throws IllegalArgumentException, ChainException, IOException {
    try {
      f = new HeapFile("file_1");
    } catch(Exception e){
      e.printStackTrace();
    }

    DummyRecord rec = new DummyRecord(REC_LEN);
    rec.ival = 0;
    rec.fval = (float) (0 * 2.5);
    rec.name = "record" + 0;

    RID newrec = f.insertRecord(rec.toByteArray());
    //System.out.println("pageno: " + newrec.pageno.pid + "\tslotno: " + newrec.slotno);
    HFPage curPage = new HFPage();
    Minibase.BufferManager.pinPage(newrec.pageno, curPage, false);

    f.deleteRecord(newrec);

    assertArrayEquals("The tuple returned should be empty", new byte[0], curPage.selectRecord(newrec));
  }

  @Test @Ignore
  public void sometest() throws Exception {

    /*
    // In general, a sequential scan won't be in the same order as the
    // insertions.  However, we're inserting fixed-length records here, and
    // in this case the scan must return the insertion order.

    HeapScan scan = null;

    if ( status == OK ) {	
      System.out.println ("  - Scan the records just inserted\n");

      try {
        scan = f.openScan();
      }
      catch (Exception e) {
        status = FAIL;
        System.err.println ("*** Error opening scan\n");
        e.printStackTrace();
      }

      if ( status == OK &&  Minibase.BufferManager.getNumUnpinned() 
              == Minibase.BufferManager.getNumBuffers() ) {
          System.err.println ("*** The heap-file scan has not pinned the first page\n");
          status = FAIL;
      }
    }	

    if ( status == OK ) {
      int len, i = 0;
      DummyRecord rec = null;
      Tuple tuple = new Tuple();

      boolean done = false;
      while (!done) { 
        try {
          tuple = scan.getNext(rid);
          if (tuple == null) {
            done = true;
            break;
          }
        }
        catch (Exception e) {
          status = FAIL;
          e.printStackTrace();
        }

        if (status == OK && !done) {
          try {
            rec = new DummyRecord(tuple);
          }
          catch (Exception e) {
            System.err.println (""+e);
            e.printStackTrace();
          }

          len = tuple.getLength();
          if ( len != reclen ) {
            System.err.println ("*** Record " + i + " had unexpected length " 
                    + len + "\n");
            status = FAIL;
            break;
          }
          else if ( Minibase.BufferManager.getNumUnpinned()
                  == Minibase.BufferManager.getNumBuffers() ) {
            System.err.println ("On record " + i + ":\n");
            System.err.println ("*** The heap-file scan has not left its " +
            "page pinned\n");
            status = FAIL;
            break;
          }
          String name = ("record" + i );

          if( (rec.ival != i)
                  || (rec.fval != (float)i*2.5)
                  || (!name.equals(rec.name)) ) {
            System.err.println ("*** Record " + i
                    + " differs from what we inserted\n");
            System.err.println ("rec.ival: "+ rec.ival
                    + " should be " + i + "\n");
            System.err.println ("rec.fval: "+ rec.fval
                    + " should be " + (i*2.5) + "\n");
            System.err.println ("rec.name: " + rec.name
                    + " should be " + name + "\n");
            status = FAIL;
            break;
          }
        }	
        ++i;
      }

      //If it gets here, then the scan should be completed
      if (status == OK) {
        if ( Minibase.BufferManager.getNumUnpinned() 
                != Minibase.BufferManager.getNumBuffers() ) {
          System.err.println ("*** The heap-file scan has not unpinned " + 
          "its page after finishing\n");
          status = FAIL;
        }
        else if ( i != (choice) ) {
          status = FAIL;

          System.err.println ("*** Scanned " + i + " records instead of "
                  + choice + "\n");
        }
      }	
    }

    if ( status == OK )
        System.out.println ("  Test 1 completed successfully.\n");

    assertEquals("  Test 1 completed successfully.\n", status, OK);

    */
  }

}

// This is added to substitute the struct construct in C++
class DummyRecord  {

	//content of the record
	public int    ival; 
	public float  fval;      
	public String name;  

	//length under control
	private int reclen;

	private byte[]  data;

	/** Default constructor
	 */
	public DummyRecord() {}

	/** another constructor
	 */
	public DummyRecord (int _reclen) {
		setRecLen (_reclen);
		data = new byte[_reclen];
	}

	/** constructor: convert a byte array to DummyRecord object.
	 * @param arecord a byte array which represents the DummyRecord object
	 */
	public DummyRecord(byte [] arecord) 
	throws java.io.IOException {
		setIntRec (arecord);
		setFloRec (arecord);
		setStrRec (arecord);
		data = arecord; 
		setRecLen(name.length());
	}

	/** constructor: translate a tuple to a DummyRecord object
	 *  it will make a copy of the data in the tuple
	 * @param _atuple: the input tuple
	 */
	public DummyRecord(Tuple _atuple) 
	throws java.io.IOException{   
		data = new byte[_atuple.getLength()];
		data = _atuple.getTupleByteArray();
		setRecLen(_atuple.getLength());

		setIntRec (data);
		setFloRec (data);
		setStrRec (data);

	}

	/** convert this class objcet to a byte array
	 *  this is used when you want to write this object to a byte array
	 */
	public byte [] toByteArray() 
	throws java.io.IOException {
		//    data = new byte[reclen];
		Convert.setIntValue (ival, 0, data);
		Convert.setFloatValue (fval, 4, data);
		Convert.setStringValue (name, 8, data);
		return data;
	}

	/** get the integer value out of the byte array and set it to
	 *  the int value of the DummyRecord object
	 */
	public void setIntRec (byte[] _data) 
	throws java.io.IOException {
		ival = Convert.getIntValue (0, _data);
	}

	/** get the float value out of the byte array and set it to
	 *  the float value of the DummyRecord object
	 */
	public void setFloRec (byte[] _data) 
	throws java.io.IOException {
		fval = Convert.getFloatValue (4, _data);
	}

	/** get the String value out of the byte array and set it to
	 *  the float value of the HTDummyRecorHT object
	 */
	public void setStrRec (byte[] _data) 
	throws java.io.IOException {
		// System.out.println("reclne= "+reclen);
		// System.out.println("data size "+_data.size());
		name = Convert.getStringValue (8, _data, reclen-8);
	}

	//Other access methods to the size of the String field and 
	//the size of the record
	public void setRecLen (int size) {
		reclen = size;
	}

	public int getRecLength () {
		return reclen;
	}  
}
