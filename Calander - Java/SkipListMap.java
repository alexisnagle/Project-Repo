/*
  Author: Alexis Nagle
  Email: anagle2020@my.fit.edu
  Course: CSE 2010
  Section: 03
  Description of this file: Create a Skip List Map using a doubly linked list
*/
import java.util.ArrayList;
public class SkipListMap {
   private int size = 0;                                                // number of nodes in the skip list
   private int height = 0;                                              // height of the skip list

   public SkipListMap() {}                                              // constructs an initially empty skip list 
   public DoublyLinkedList list = new DoublyLinkedList();               // creates a doublyLinkedList to simulate a skip list
   public FakeRandHeight randHeight = new FakeRandHeight();             // fand rand height simulates a coin flip to determine height

   /**
    * Add a node to the skip list
    * 
    * @param time at which the event should be added
    * @param name of the event that should be added
    * @return event, returns event that was added, returns ExistingEventError:existingEvent if an event exists at that time
    */
   public String put(int time, String name) { 
      if (list.find(time) != null) {                                    // if an event already exists at the desired time, return error
         return String.format("%06d%s%s%s%s",time," ",name," ExistingEventError:",list.find(time).getValue());
      }
      int h = randHeight.get();                                         // determine the height h to add the event at
      while (list.height() < h + 1) {                                   // ensure there are h+1 levels in the skip list 
         list.addLevel();                                               // if not, add new level(s) to the skip list
      }
      list.add(time, name, h);                                          // add the event to the list
      return String.format("%06d%s%s",time, " ", name);                 // return the added event
   }

   /**
    * Remove a node from the skip list
    * 
    * @param time at which an event should be removed
    * @return event, returns the event that was removed, returns NoEventError if not found
    */
   public String remove(int time) { 
      String event = get(time);                                         // get the name of the event at time 
      if ( event.contains("none")) {                                    // if the event does not exist
         return String.format("%06d%s",time," NoEventError");           // return NoEventError
      }
      list.remove(list.find(time));                                     // find and remove the node that correspons to time 
      return event;                                                     // return the event that was removed
   }

   /**
    * Get value of a node in the skip list
    * 
    * @param time at which the event name is desired
    * @return event, returns event at desired time, returns none if it does not exist
    */
   public String get(int time) { 
      if (list.isEmpty() || list.find(time) == null) {
         return String.format("%06d%s",time," none");                   // if empty or the event does not exist return none
      }
      String name = list.find(time).getValue();
      return String.format("%06d%s%s",time, " ",name);                  // return the name of the event
   }

   /**
    * Get the value of all nodes between the start and end time in the skip list
    * 
    * @param startTime beginning limit time for the submap
    * @param endTime end limit time for the submap
    * @return submapEvents, a list of events between the desired times
    */
   public ArrayList<String> submap(int startTime, int endTime) { 
      ArrayList<String> submapEvents = new ArrayList<String>();
      for (int i = startTime; i <=endTime; i++) {                       // for all times between the start and end (inclusive)

         if (list.find(i) != null) {                                    // if an event exists at time i
            String event = String.format("%06d%s%s",i, ":",list.find(i).getValue()); 
            submapEvents.add(event);                                    // add event to list as time:eventName
         }
      }
      return submapEvents;                                              // return an array list of all the events
   }

   /**
    * Print a visual repersentation of the Skip List
    */
   public void print() {
      list.print();                                                     // print the list
   }
}
