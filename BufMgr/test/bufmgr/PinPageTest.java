package bufmgr;

import chainexception.ChainException;
import global.Minibase;
import org.junit.Before;

import java.io.IOException;

/**
 * Created by david on 2/10/16.
 */
public class PinPageTest extends BufMgrTest {
    @Before
    public void setUp() throws IOException, ChainException {
        Minibase.DiskManager.allocate_page(2);
    }
}
