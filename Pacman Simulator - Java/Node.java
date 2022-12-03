/*
  Author: Alexis Nagle
  Email: anagle2020@my.fit.edu
  Course: CSE 2010
  Section: 03
  Description of this file: Node for graph
*/
import java.util.ArrayList;
public class Node {

      public String data;                                               // reference to the data stored in this node
      public int row;                                                   // reference to the row
      public int col;                                                   // reference to the col
      public ArrayList<Node> children;                                  // reference to a list of children nodes
      public boolean visited;                                           // reference to if the node has been visited
      public Node parent;                                               // reference to the node's "parent"

      /**
       * Creates a node with the given data, an empty children list
       *
       * @param d  the data to be stored
       * @param r  the row to be stored
       * @param c  the col to be stored
       */
      public Node(String d, int r, int c) {
         data = d;
         row = r;
         col = c;
         children = new ArrayList<Node>();
         visited = false;
         parent = null;
      }
   } 
