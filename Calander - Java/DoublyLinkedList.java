/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
//package net.datastructures;

/**
 * A basic doubly linked list implementation.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class DoublyLinkedList<E> {

  //---------------- nested Node class ----------------
  /**
   * Node of a doubly linked list, which stores a reference to its
   * element and to the previous, next, above, and below nodes in the list.
   */
  public static class Node {

    /** The key stored at this node */
    private int key;                                                          // reference to the key stored at this node

    /** The value stored at this node */
    private String value;                                                     // reference to the value stored at this node

    /** A reference to the preceding node in the list */
    private Node prev;                                                        // reference to the previous node in the list

    /** A reference to the subsequent node in the list */
    private Node next;                                                        // reference to the subsequent node in the list

    /** A reference to the above node in the list */
    private Node above;                                                       // reference to the above node in the list

    /** A reference to the below node in the list */
    private Node below;                                                       // reference to the below node in the list

    /**
     * Creates a node with the given element and next node.
     *
     * @param k  the key to be stored
     * @param v  the value to be stored
     * @param p  reference to a node that should precede the new node
     * @param n  reference to a node that should follow the new node
     * @param a  reference to a node that should be above the new node
     * @param b  reference to a node that should be below the new node
     */
    public Node(int k, String v, Node p, Node n, Node a, Node b) {
      key = k;
      value = v;
      prev = p;
      next = n;
      above = a;
      below = b;
    }

    // public accessor methods
    /**
     * Returns the key stored at the node.
     * @return the key stored at the node
     */
    public int getKey() { return key; }

    /**
     * Returns the value stored at the node.
     * @return the value stored at the node
     */
    public String getValue() { return value; }

    /**
     * Returns the node that precedes this one (or null if no such node).
     * @return the preceding node
     */
    public Node getPrev() { return prev; }

    /**
     * Returns the node that follows this one (or null if no such node).
     * @return the following node
     */
    public Node getNext() { return next; }

    /**
     * Returns the node above this one (or null if no such node).
     * @return the above node
     */
    public Node getAbove() { return above; }

    /**
     * Returns the node below this one (or null if no such node).
     * @return the below node
     */
    public Node getBelow() { return below; }

    // Update methods
    /**
     * Sets the node's previous reference to point to Node n.
     * @param p    the node that should precede this one
     */
    public void setPrev(Node p) { prev = p; }

    /**
     * Sets the node's next reference to point to Node n.
     * @param n    the node that should follow this one
     */
    public void setNext(Node n) { next = n; }

    /**
     * Sets the node's above reference to point to Node n.
     * @param n    the node that should be above this one
     */
    public void setAbove(Node n) { above = n; }

    /**
     * Sets the node's below reference to point to Node n.
     * @param n    the node that should be below this one
     */
    public void setBelow(Node n) { below = n; }
  } //----------- end of nested Node class -----------

  // instance variables of the DoublyLinkedList
  /** Sentinel node at the beginning of the list */
  private Node header;                                                         // header sentinel

  /** Sentinel node at the end of the list */
  private Node trailer;                                                        // trailer sentinel

  /** Number of elements in the list (not including sentinels) */
  private int size = 0;                                                        // number of elements in the list
  private int height = 0;                                                      // number of levels 

  /** Constructs a new empty list. */
  public DoublyLinkedList() {
    header = new Node(Integer.MIN_VALUE,"h", null, null, null, null);          // create header
    trailer = new Node(Integer.MAX_VALUE,"t", header, null, null, null);       // trailer is preceded by header
    header.setNext(trailer);                                                   // header is followed by trailer
  }

  // public accessor methods
  /**
   * Returns the number of elements in the linked list.
   * @return number of elements in the linked list
   */
  public int size() { return size; }

  /**
   * Returns the number of levels in the linked list.
   * @return number of levels in the linked list
   */
  public int height() { return height; }

  /**
   * Tests whether the linked list is empty.
   * @return true if the linked list is empty, false otherwise
   */
  public boolean isEmpty() { return size == 0; }

  /**
   * Adds an element to the linked list in between the given nodes.
   * The given predecessor and successor should be neighboring each
   * other prior to the call.
   *
   * @param key           key of the new node
   * @param value         value of the new node
   * @param predecessor   node just before the location where the new element is inserted
   * @param successor     node just after the location where the new element is inserted
   * @param above         node just above the location where the new element is inserted
   * 
   * @return              returns the node that was added
   */
  private Node addBetween(int k, String v, Node predecessor, Node successor, Node above) {
    Node newest = new Node(k, v, predecessor, successor, above, null);        // create new node, with prev, next, and abv
    predecessor.setNext(newest);                                              // set previous node to point at new node
    successor.setPrev(newest);                                                // set the successing node to point at the new node
    if (above != null)
      above.setBelow(newest);                                                 // if there is an upper node, set it to point to the new node
    size++;                                                                   // increase the size to account for the added node
    return newest;                                                            // return the added node
  }

  /**
   * Finds the proper location at which a node shall be added and calls the 
   * addBetween method to add the node. The method repeats this for all levels 
   * below the height at which the node is added
   *
   * @param k             key that will be added to the list
   * @param v             value to be added, corresponds to the key in the list
   * @param h             height at which the new key and value should be added
   */
  public void add(int k, String v, int h) {
    Node current = header;                                                    // start at the header
    for (int i = height; i > h; i--) {
      current = current.getBelow();                                           // drop down to correct height to add the node
    }
    Node previous = null;                                                     // declare a previous node, this will be the upper node
    for (int i = h; i >= 0; i--) {                                            // for each level less than or equal to the height 
      while (current.getNext().getKey() < k) {
        current = current.getNext();                                          // skip forward if the next node is less than the key
      }
      previous = addBetween(k, v, current, current.getNext(), previous);      // create a new node between current and the next node, with previous as the upper node
      if (current.getBelow() != null)                                         // if there are lower levels, 
        current = current.getBelow();                                         // drop down to next level
    }
  }

  /**
   * Adds a new level to the list above the previous upper level
   */
  public void addLevel() {
    Node newHead = new Node(Integer.MIN_VALUE,"h", null, null, null, header);  
    Node newTail = new Node(Integer.MAX_VALUE,"t", newHead, null, null, trailer);
    newHead.setNext(newTail);                                                 // declare the new head to point to the nex tail
    header.setAbove(newHead);                                                 // have the heder point up to the new head
    trailer.setAbove(newTail);                                                // have the trailer point to the new tail
    header = newHead;                                                         // set the header to the new head
    trailer = newTail;                                                        // set the trailer to the new tail
    height++;                                                                 // increase the height to account for the added level
  }

  /**
   * Finds the upper most node with key k
   *
   * @param k             desired key to find in the list
   * 
   * @return              returns the uppermost occurance of the desired node
   */
  public Node find(int k) {
    Node current = header;                                                    // start iterating though at the header
    while (current != null) {                                                 // continue while the current node is not null
      while(current.getNext() != null && current.getNext().getKey() <= k) {
        current = current.getNext();                                          // skip forward as long as the next ode exists and is <= k
      }
      if (current.getKey() == k) {
        return current;                                                       // if the current key equals k, return the current node
      }
      current = current.getBelow();                                           // else, drop down to the next level 
    }
    return current;                                                           // return the current value, returns null if not found
  }

  /**
   * Removes the given node from the list, and its corresponding lower nodes and returns its element.
   * @param node        the node to be removed (must not be a sentinel)
   */
  public void remove(Node node) {
    Node predecessor = node.getPrev();                                        // store the previous node 
    Node successor = node.getNext();                                          // store the next node
    Node above = node.getAbove();                                             // store the above node
    Node below = node.getBelow();                                             // store the below node
    predecessor.setNext(successor);                                           // point the predecessor to the succesor
    successor.setPrev(predecessor);                                           // point the succesor back to the predecessor
    if (above != null) {
      above.setBelow(below);                                                  // as long as the above node is not null, point it to the below node
    }
    if (below != null) {
      below.setAbove(above);                                                  // as long as the below node is not null, point it to the above node
      remove(below);                                                          // remove the below node
    }
    if (predecessor.getKey() == Integer.MIN_VALUE && successor.getKey() == Integer.MAX_VALUE) {
                                                                              // if the row is now empty, except for the min and max
      header.below = predecessor.getBelow();                                  // have the header point to the min of the next row
      trailer.below = successor.getBelow();                                   // have the trailer point to the max of the next row 
      height--;                                                               // decrease the height to account for the removed row
    }
    size--;                                                                   // decrease the size to account for the removed node
  }

  /**
   * Prints a visual repersentation of the current list 
   */
  public void print() {
    Node current = header;                                                    // begin at the header
    int level = height;                                                       // declare the level as the height
    while (current != null) {                                                 // continue while the current is not null
      System.out.print("(s" + level + ") ");                                  // print the number of the current level
      if (level == height)                                                    
        System.out.print("empty");                                            // if at the uppermost level, print empty
      Node nodeInLevel = current;                                             // set nodeInLevel to current
      nodeInLevel = nodeInLevel.getNext();                                    // go to next node, skipping neg infinity
 
      while (nodeInLevel.getNext() != null) {                                 // continue while there are more nodes in that level
        System.out.printf("%06d%s%s%s",nodeInLevel.getKey(), ":", nodeInLevel.getValue(), " ");
        nodeInLevel = nodeInLevel.getNext();                                  // print the node and continue to the next node
      }
 
      current = current.getBelow();                                           // once there are no nodes left in the level, drop down 
      System.out.println();
      level--;                                                                // decrease level to account for dropping down to the next level
    }
  }
} //----------- end of DoublyLinkedList class -----------
