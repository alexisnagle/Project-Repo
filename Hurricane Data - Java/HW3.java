/*
  Author: Alexis Nagle
  Email: anagle2020@my.fit.edu
  Course: CSE 2010
  Section: 03
  Description of this file: Take heirachy input to create a linked structure tree, 
  then take in commands about the datastored in the tree and return the correct answers according to what was asked
*/
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
public class HW3
{
   private static Tree hurricaneData = new Tree(); // create empty tree to store hurricane data in 

   /**
    * overloads printAnswers to then call printAnswers but only prints the elements from index min to max
    * 
    * @param answers1 the first list of answers
    * @param min the min index of the substring
    * @param max the max index of the substring
    */
   public static void printAnswers(ArrayList<String> answers, int min, int max) {
      ArrayList<String> newAnswers = new ArrayList<String>();
      for (int i = 0; i < answers.size();i++) {
         newAnswers.add(answers.get(i).substring(min,max)); // substring each element from min to max
      }
      printAnswers(newAnswers); // print substringed answers
   }

   /**
    *  prints answers contained in an array list in alphabetical order
    * 
    * @param answers is the list of answers to be printed
    */
   public static void printAnswers(ArrayList<String> answers) {
      Collections.sort(answers); // sort array list

      //remove duplicates from the list 
      Object[] arr = answers.toArray();
      for (Object s : arr) {
         if (answers.indexOf(s) != answers.lastIndexOf(s)) {
            answers.remove(answers.lastIndexOf(s)); // if the object occurs twice, remove
         }
      }
      // print none if there are no answers
      if (answers.size() == 0)
         System.out.print("none"); 
      //print each element in the list
      for (int i = 0; i < answers.size(); i ++) {
         System.out.print(answers.get(i) + " ");
      }
      System.out.println(); // print new line
   }

   /**
    * retrieves the data of any nodes that have multiple ancestors containing a string
    * 
    * @param str is the string to be obtained in the ancestors
    * @return answers is a list of nodes that have multiple ancestors containing the string 
    */
   public static ArrayList<String> getMultiple(String str) {
      ArrayList<String> answers = new ArrayList<String>();
      ArrayList<String> duplicates = hurricaneData.getDuplicates(); // find duplicated nodes
      for (int i = 0; i < duplicates.size(); i++) {
         // find ancestors of each node containing a specified string
         ArrayList<String> ancestors = hurricaneData.getAncestors(hurricaneData.findAllNodes(duplicates.get(i)),str);
         //remove duplicated ancestors
         Object[] arr = ancestors.toArray();
         for (Object s : arr) {
            if (ancestors.indexOf(s) != ancestors.lastIndexOf(s)) {
               ancestors.remove(ancestors.lastIndexOf(s)); // if the object occurs twice, remove
            }
         }
         // if the node has more than one ancestor
         if (ancestors.size() > 1)
            answers.add(duplicates.get(i)); // add it to the answers list
      }
      return answers; //return final list of answers
   }
   

   public static void main(final String[] args) throws Exception
   {
      final Scanner dataFile = new Scanner(new File(args[0])); // scans file inputted from the arguments with data to form tree
      final Scanner query = new Scanner(new File(args[1])); // scans file inputted from the arguments with query of what to return 

      //-----------Create the Tree Structure-----------
      //iterate through lines in the data file
      while (dataFile.hasNextLine()) {
         String[] line = dataFile.nextLine().split(" "); //read in the next line and split the line so the first element is the parent and the rest are children 
         if (hurricaneData.isEmpty()) { // if there are no nodes in the tree
            hurricaneData.addRoot(line[0]); // add new node as the root
            for (int i = 1; i < line.length; i++) {
               hurricaneData.addChild(hurricaneData.rootNode(), line[i]); // add children to the root
            }
         }
         else { // find the node correspodnding to the current parent of the line of entries and add its children 
            for (int i = 1; i < line.length; i++) {
               hurricaneData.addChild(hurricaneData.findNode(line[0]), line[i]);
            }
         }
      }

      //-----------Call Procedures about the Tree-------------
      // iterate through the lines in the query file
      while (query.hasNextLine()) {
         String[] arr = query.nextLine().split(" "); //read in the next line and split the line so the first element is the parent and the rest are children
         if (arr[0].equals("GetNamesByCategory")) {
            System.out.print("GetNamesByCategory " + arr[1] + " ");
            // find all nodes with the right category, find the leaf decendants of those nodes
            ArrayList<String> answers = hurricaneData.getLeafDecendants(hurricaneData.findAllNodes(arr[1]));
            printAnswers(answers); // print the leaf decendants
         }
         else if (arr[0].equals("GetNamesByState")) {
            System.out.print("GetNamesByState "+ arr[1] + " ");
            // find all nodes with the right category, find the leaf decendants of those nodes
            ArrayList<String> answers = hurricaneData.getLeafDecendants(hurricaneData.findAllNodes(arr[1]));
            printAnswers(answers); // print the leaf decendants
         }
         else if (arr[0].equals("GetNamesByCategoryAndState")) {
            System.out.print("GetNamesByCategoryAndState " + arr[1] + " " + arr[2] + " ");
            //find all nodes in the correct categories, find the leafs that are of both those nodes
            ArrayList<String> answers = hurricaneData.getLeafDecendants(hurricaneData.findAllNodes(arr[1]), hurricaneData.findAllNodes(arr[2]));
            printAnswers(answers); //print leafs contained in both categories
         }
         else if (arr[0].equals("GetNamesWithMultipleStates")) {
            System.out.print("GetNamesWithMultipleStates ");
            //find nodes that are have multiple ancestors of a certain string
            ArrayList<String> answers = getMultiple("_");
            printAnswers(answers); // print names with multiple states
         }
         else if (arr[0].equals("GetNamesWithMultipleCategories")) {
            System.out.print("GetNamesWithMultipleCategories ");
            //find nodes that are have multiple ancestors of a certain string
            ArrayList<String> answers = getMultiple("category");
            printAnswers(answers); // print names with multiple categories
         }
         else if (arr[0].equals("GetCategory")) {
            System.out.print("GetCategory " + arr[1] + " ");
            //find all nodes with correct name, find ancestors of that node that indicate categories
            ArrayList<String> answers = hurricaneData.getAncestors(hurricaneData.findAllNodes(arr[1]), "category");
            printAnswers(answers); // print the categories
         }
         else if (arr[0].equals("GetState")) {
            System.out.print("GetState " + arr[1] + " ");
            //find all nodes with correct name, find ancestors of that node that indicate states
            ArrayList<String> answers = hurricaneData.getAncestors(hurricaneData.findAllNodes(arr[1]),"_");
            printAnswers(answers, 0, 2); //print the first two letters of the states

         }
      }
   }
}
