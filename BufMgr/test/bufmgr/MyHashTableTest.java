package bufmgr;

import global.Page;
import global.PageId;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by david on 2/15/16.
 */
public class MyHashTableTest {

    MyHashTable mTable;
    @Before
    public void setUp() throws Exception {
        mTable = new MyHashTable();
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(0, mTable.size());
        Frame f = new Frame(new PageId(1), new Page());
        mTable.put(f.getPageId(), f);
        assertEquals(1, mTable.size());
        mTable.remove(f.getPageId());
        assertEquals(0, mTable.size());
    }

    @Test
    public void testDoubleInsert() {
        Frame f = new Frame(new PageId(1), new Page());
        mTable.put(f.getPageId(), f);
        mTable.put(f.getPageId(), new Frame(new PageId(), new Page())); //add a second, different, entry.
        assertEquals("Size should be 1", 1, mTable.size());
    }

    @Test
    public void testIsEmpty() throws Exception {
        assertTrue(mTable.isEmpty());
        Frame f = new Frame(new PageId(1), new Page());
        mTable.put(f.getPageId(), f);
        assertFalse(mTable.isEmpty());
        mTable.remove(f.getPageId());
        assertTrue(mTable.isEmpty());
    }

    @Test
    public void testContainsKey() throws Exception {
        Frame f = new Frame(new PageId(1), new Page());
        mTable.put(f.getPageId(), f);
        assertTrue(mTable.containsKey(f.getPageId()));

        mTable.remove(f.getPageId());
        assertFalse(mTable.containsKey(f.getPageId()));
    }

    @Test
    public void testContainsValue() throws Exception {
        Frame f = new Frame(new PageId(1), new Page());
        mTable.put(f.getPageId(), f);
        assertTrue(mTable.containsValue(f));
    }

    @Test
    public void testGet() throws Exception {
        Frame f = new Frame(new PageId(1), new Page());
        assertNull(mTable.get(f.getPageId()));

        mTable.put(f.getPageId(), f);
        assertSame(f, mTable.get(f.getPageId()));

        assertSame(f, mTable.get(new PageId(f.getPageId().pid)));
    }

    @Test
    public void testPut() throws Exception {
        Frame f = new Frame(new PageId(1), new Page());
        assertEquals(0, mTable.size());
        mTable.put(f.getPageId(), f);
        assertEquals(1, mTable.size());
    }

    @Test
    public void testRemove() throws Exception {
        Frame f = new Frame(new PageId(1), new Page());
        assertEquals(0, mTable.size());
        mTable.put(f.getPageId(), f);
        assertEquals(1, mTable.size());
        mTable.remove(f.getPageId());
        assertEquals(0, mTable.size());
        assertNull(mTable.get(f.getPageId()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutAll() throws Exception {
        mTable.putAll(new HashMap());
    }

    @Test
    public void testClear() throws Exception {
        for(int i = 0; i < 10; i++) {
            mTable.put(new PageId(i), new Frame(new PageId(i), new Page()));
        }
        assertEquals(10, mTable.size());
        mTable.clear();
        assertEquals(0, mTable.size());
    }

    @Test
    public void testKeySet() throws Exception {
        for(int i = 0; i < 10; i++) {
            mTable.put(new PageId(i), new Frame(new PageId(i), new Page()));
        }
        Set s = mTable.keySet();
        for(int i = 0; i < 10; i++) {
            assertTrue(s.contains(new PageId(i)));
        }
    }

    @Test
    public void testValues() throws Exception {
        Page p = new Page();
        ArrayList<Frame> list = new ArrayList<Frame>();
        for(int i = 0; i < 10; i++) {
            Frame f = new Frame(new PageId(i), p);
            mTable.put(f.getPageId(), f);
            list.add(f);
        }
        Collection<Frame> frames = mTable.values();

        for(Frame f : list) {
            assertTrue(frames.contains(f));
        }

    }

    @Test
    public void testEntrySet() throws Exception {
        Page p = new Page();
        ArrayList<Frame> list = new ArrayList<Frame>();
        for(int i = 0; i < 10; i++) {
            Frame f = new Frame(new PageId(i), p);
            mTable.put(f.getPageId(), f);
            list.add(f);
        }
        Set<Map.Entry<PageId, Frame>> entrySet = mTable.entrySet();

        for(Frame f : list) {
            assertTrue(entrySet.contains(new MyHashTable.MyEntry(
                    f.getPageId(),
                    f
            )));
        }
    }
}