package heap.tree;

/**
 * Created by david on 2/18/16.
 */
class DataNode {
    // I chose Integer because it allows a null value, unlike int
    private Integer data;

    DataNode() {
        data = null;
    }
    public String toString() {
        return data.toString();
    }
    public DataNode(int x) {
        data = x;
    }
    public int getData() {
        return data.intValue();
    }
    public boolean inOrder(DataNode dnode) {
        return (dnode.getData() <= this.data.intValue());
    }
}
