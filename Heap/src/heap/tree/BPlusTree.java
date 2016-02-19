package heap.tree;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Vector;

class BPlusTree {

    private static Node tree;
    private static int degree;

    private BPlusTree(int x) {
        // a B+ Tree must have an initial degree
        degree = x;

        // The initial type of Node for a B+Tree is a leaf
        tree = new LeafNode(degree);

    }

    private static void insertIntoTree(DataNode dnode) {
        tree = tree.insert(dnode);
    }

    private static void searchTree(int x, BufferedWriter output) throws IOException {

        // search the tree starting from the top
        if( tree.search(new DataNode(x)) ) {
            output.write("FOUND" + System.getProperty("line.separator"));
        }
        else {
            output.write("NOT FOUND" + System.getProperty("line.separator"));
        }
    }

    @SuppressWarnings("unchecked")
    private static void printTree(BufferedWriter output) throws IOException {
        // create a vector to store all the nodes from each level as we
        Vector<Node> nodeList = new Vector();

        // put the root of the tree onto the stack to start the process
        nodeList.add(tree);

        boolean done = false;
        while(! done) {
            // this vector will hold all the children of the nodes in the current level
            Vector<Node> nextLevelList = new Vector();
            String toprint = "";

            // for each node in the list convert it to a string and add any children to the nextlevel stack
            for(int i=0; i < nodeList.size(); i++) {

                // get the node at position i
                Node node = (Node)nodeList.elementAt(i);

                // convert the node into a string
                toprint += node.toString() + " ";

                // if this is a leaf node we need only print the contents
                if(node.isLeafNode()) {
                    done = true;
                }
                // if this is a tree node print the contents and populate
                // the temp vector with nodes that node i points to
                else
                {
                    for(int j=0; j < node.size()+1 ; j++) {
                        nextLevelList.add( ((TreeNode)node).getPointerAt(j) );
                    }
                }
            }

            // print the level
            output.write(toprint + System.getProperty("line.separator"));

            // go to the next level and print it
            nodeList = nextLevelList;
        }
    }
}

