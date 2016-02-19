package heap;

import java.util.Arrays;
import global.GlobalConst;

/**
 * Created by david on 2/5/16.
 */
public class Tuple implements GlobalConst {
  private int length;
  protected byte[] data;

  public Tuple(byte[] byteArray, int from, int to) {
    data = Arrays.copyOfRange(byteArray, from, to);
  }

  public Tuple() {
    data = new byte[MAX_TUPSIZE];
  }

  public int getLength() {
    return data.length;
  }

  public byte[] getTupleByteArray() {
    return data;
  }
}
