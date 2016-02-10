package heap;

import java.util.Arrays;
import global.GlobalConst;

/**
 * Created by david on 2/5/16.
 */
public class Tuple implements GlobalConst {
  private int length;
  private byte[] tupleByteArray;

  public Tuple(byte[] byteArray, int from, int to) {
    tupleByteArray = Arrays.copyOfRange(byteArray, from, to);
  }

  public Tuple() {
    tupleByteArray = new byte[MAX_TUPSIZE];
  }

  public int getLength() {
    return tupleByteArray.length;
  }

  public byte[] getTupleByteArray() {
    return tupleByteArray;
  }
}
