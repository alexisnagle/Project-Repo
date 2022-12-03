/*
  Author: Alexis Nagle
  Email: anagle2020@my.fit.edu
  Course: CSE 2010
  Section: 03
  Description of this file: Interface of a priority queue 
*/

public interface PriorityQueue<K,V> {
   int size();                                                          // size of a priority queue
   boolean isEmpty();                                                   // returns true if size == 0
   Entry<K,V> insert(K key,V value) throws IllegalArgumentException;    // insert a new entry
   Entry<K,V> min();                                                    // returns smallest entry
   Entry<K,V> removeMin();                                              // removes and returns smallest entry
}
