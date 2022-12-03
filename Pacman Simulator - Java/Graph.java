/*
  Author: Alexis Nagle
  Email: anagle2020@my.fit.edu
  Course: CSE 2010
  Section: 03
  Description of this file: Create a Graph using an array list 
*/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
public class Graph {
   private int size = 0;                                                // number of nodes in the graph

   public Graph() {}                                                    // constructs an initially empty graph 
   public ArrayList<Node> list = new ArrayList<>();                     // creates a doublyLinkedList to simulate a graph

   /**
    * adds new node to the list
    * 
    * @param d the data of the new node
    * @param r the row of the new node
    * @param c the col of the new node
    */
   public void addNode(String d, int r, int c) {
      Node newest = new Node(d,r,c);                                    // creates a new node with given parameters
      list.add(newest);                                                 // adds the node to the list 
      size++;                                                           // increases the size of the graph
   }

   /**
    * Locate a specific node based on its data content
    *
    * @param nodeData data to find its corresponding node  
    * @return node corresponding to the data given 
    */
   public Node findNode(String nodeData) {
      for (int i = 0; i < size; i++) {                                  //iterate through all the nodes
         if (list.get(i).data.equalsIgnoreCase(nodeData))
            return list.get(i);                                         //return the proper node
      }
      return null;                                                      // return null if the node does not exist
   }

   /**
    * Locate a specific node based on its row and col content
    *
    * @param r row to find its corresponding node
    * @param c col to find its corresponding node
    * @return node corresponding to the row and col given 
    */
   public Node findNodeRowAndCol(int r, int c) {
      for (int i = 0; i < size; i++) {                                  //iterate through all the nodes
         if (list.get(i).row == r && list.get(i).col == c)
            return list.get(i);                                         //return the proper node
      }
      return null;                                                      // return null if the node does not exist
   }
   
   /**
    * Adds corresponding up, dowm, left, right connecting nodes to the list of the node
    */
   public void addAllChildren() {
      for (int i = 0; i < size; i++) {
         Node parent = list.get(i);                                     // node to which "children" are being added
         int row = parent.row;
         int col = parent.col;
         parent.children.add(findNodeRowAndCol(row, col - 1));          // above
         parent.children.add(findNodeRowAndCol(row, col + 1));          // below
         parent.children.add(findNodeRowAndCol(row - 1, col));          // left
         parent.children.add(findNodeRowAndCol(row + 1, col));          // right
         

      }
   }

   /**
    * Uses BFS to traverse through the graph starting at a specific node
    * references each node to a "parent" node 
    * 
    * @param s is the starting node
    */
   public void BFS(Node s) {
      Queue<Node> q = new LinkedList<>();
      q.add(s);                                                         // enqueue the first node
      while (q.peek() != null) {                                        // while there are nodes in the queue
         Node u = q.remove();                                           // dequeue a node
         if (!u.visited) {
            u.visited = true;                                           // visit the node
            for (int i = 0; i < u.children.size(); i++) {
               Node v = u.children.get(i);
               if (!v.visited && (v.data.equals(" ") || v.data.equals(".") || v.data.equals("P"))) {
                  q.add(v);                                             // enquque each of the node children that are unvisted and != #
                  v.parent = u;                                         // refernce each child to its parent node
               }
            }
         }
      }
   }

   /**
    * Resets parent and visited pointers so BFS can be ran from a different node
    */
   public void resetParentAndVisit() {
      for (int i = 0; i < size; i++) {                                  // for each node
         list.get(i).parent = null;                                     // set parent back to null
         list.get(i).visited = false;                                   // set as unvisited 
      }
   }

   /**
    * Print a grid repersentation of the graph
    */
   public void print(int rows, int cols) {
      System.out.print(" ");
      for (int n = 0; n < cols; n++) {
         System.out.print(n%10);                                        // print column number headers
      }
      System.out.println();

      for (int i = 0; i < rows; i++) {
         System.out.print(i%10);                                        // print row number headers 
         for (int j = 0; j < cols; j++) {
            System.out.print(findNodeRowAndCol(i,j).data);              // print the node at row i and col j
         }
         System.out.println();
      }
   }
}
