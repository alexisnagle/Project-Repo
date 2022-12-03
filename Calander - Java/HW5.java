/*
  Author: Alexis Nagle
  Email: anagle2020@my.fit.edu
  Course: CSE 2010
  Section: 03
  Description of this file: Simulate a sort of calendar to add, remove, and return events at specific times using a SkipListMap
*/
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
public class HW5
{
   /* Print the results stored in an ArrayList, print none if there are no events*/
   public static void printResults(ArrayList<String> results) {
      if (results.size() == 0) {
         System.out.println("none");                                          // if the arraylist is empty print none
      }
      else {
         for (int i = 0; i < results.size(); i++) {                           // iterate through the arraylist
            System.out.print(results.get(i) + " ");                           // print out each event listed in the array list
         }
         System.out.println();
      }
   }
   
   public static void main(final String[] args) throws Exception
   { 
      final Scanner in = new Scanner(new File(args[0]));                      // scanner in scans file inputted from the arguments

      SkipListMap schedule = new SkipListMap();                               // create a new skip list

      /*iterate through lines in the input*/
      while(in.hasNext()) {
         String command = in.next();                                          // store the first word as the command
         
         String result;
         ArrayList<String> results;
         switch (command) {                                                   // correspond the command with what is to be done
            case "AddEvent":                                                  // AddEvent 
               result = schedule.put(in.nextInt(), in.next());                // add event with time MMDDHH and event name 
               System.out.println("AddEvent " + result);
               break;
            case "CancelEvent":                                               // CancelEvent
               result = schedule.remove(in.nextInt());                        // remove event at time MMDDHH 
               System.out.println("CancelEvent " + result);
               break;
            case "GetEvent":                                                  // GetEvent
               result = schedule.get(in.nextInt());                           // get event at time MMDDHH 
               System.out.println("GetEvent " + result);
               break;
            case "GetEventsBetweenTimes":                                     // GetEventBetweenTimes
               int startTime = in.nextInt();                                  // startTime
               int endTime = in.nextInt();                                    // endTime
               results = schedule.submap(startTime, endTime);                 // get events between time startTime MMDDHH and endTime MMDDHH
               System.out.printf("%s%06d%s%06d%s","GetEventsBetweenTimes ",startTime," ",endTime," ");
               printResults(results);
               break;
            case "GetEventsForOneDay":                                        // GetEventsForOneDay
               int date = in.nextInt();                                       // date MMDD
               results = schedule.submap(date*100, date*100 + 23);            // get events between time MMDD00 and MMDD23                     
               System.out.printf("%s%04d%s","GetEventsForOneDay ",date," ");
               printResults(results);
               break;
            case "GetEventsForTheRestOfTheDay":                               // GetEventsForTheRestOfTheDay
               int time = in.nextInt();                                       // time MMDDHH
               results = schedule.submap(time, (time/100)*100 + 23);          // get events between time MMDDHH and MMDD23
               System.out.printf("%s%06d%s","GetEventsForTheRestOfTheDay ",time," ");
               printResults(results);
               break;
            case "GetEventsFromEarlierInTheDay":                              // GetEventsFromEarlierInTheDay
               int time2 = in.nextInt();                                      // time MMDDHH
               results = schedule.submap((time2/100)*100, time2);             // get events between time MMDD00 and MMDDHH
               System.out.printf("%s%06d%s","GetEventsFromEarlierInTheDay ",time2," ");
               printResults(results);
               break;
            case "PrintSkipList":                                             // PrintSkipList
               System.out.println("PrintSkipList");                        
               schedule.print();                                              // print the visual repersentation of the list
               break;

         }
      }

   }
}
