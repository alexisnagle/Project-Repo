/*
  Author: Alexis Nagle
  Email: anagle2020@my.fit.edu
  Course: CSE 2010
  Section: 03
  Description of this file: Simulate one round of a Pac-Man game
*/
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
public class HW6 {
   private static boolean endgame = false;                                    // boolean for continuation of the game
   private static int points = 0;                                             // points pac-man has earned
   private static Graph board = new Graph();                                  // Create graph
   private static int rows;
   private static int cols;

   public static boolean playerCanMove() {
      Node p = board.findNode("P");                                           // find pacman node
      for (int i = 0; i < p.children.size(); i++) {                           // for each connecting node, check if pacman can move
         if (p.children.get(i).data.equals(" ") || p.children.get(i).data.equals(".")) {
            return true;                                                      // if pacman can move to a node return true
         }
      }
      return false;                                                           // return false if pacman cannot move
   }

   public static void movePlayer() {
      Scanner kb = new Scanner(System.in);
      System.out.print("Please enter your move [u(p), d(own), l(elf), or r(ight)]: ");
      String dir = kb.next();                                                 // take in user input for direction

      Node p = board.findNode("P");                                           // find pacman node
      Node newP = null;                                                       // node pacman will be moved to 
      if (dir.equalsIgnoreCase("u")) {
         newP = board.findNodeRowAndCol(p.row - 1, p.col);                    // if up, set newP to above node
      }
      else if (dir.equalsIgnoreCase("d")) {
         newP = board.findNodeRowAndCol(p.row + 1, p.col);                    // if down, set newP to below node
      }
      else if (dir.equalsIgnoreCase("l")) {
         newP = board.findNodeRowAndCol(p.row, p.col - 1);                    // if left, set newP to left node
      }
      else if (dir.equalsIgnoreCase("r")) {
         newP = board.findNodeRowAndCol(p.row, p.col + 1);                    // if right, set newP to right node
      }

      if (newP != null && (newP.data.equals(" ") || newP.data.equals("."))) {
         if (newP.data.equals(".")) {                                         // if newP is a dot,
            points++;                                                         // increase points
         }
         newP.data = "P";                                                     // set the data of newP to P
         p.data = " ";                                                        // set the data of the previous P to " " 
         board.print(rows, cols);                                             // print the board
         System.out.println("Points: " + points);                             // print number of points
      }
      else {
         movePlayer();                                                        // if invalid input, reprompt
      }
      
      if (board.findNode(".") == null && board.findNode("G").data.equals("G") // if there are no dots left (capital ghost == dot),
                                      && board.findNode("H").data.equals("H") 
                                      && board.findNode("O").data.equals("O")
                                      && board.findNode("S").data.equals("S")) {                                      
         endgame = true;                                                      // game has ended,
         System.out.println("Pac-man is full!");                              // pacman won 
      }
   }

   public static void moveGhost(String ghost) {
      Node g = board.findNode(ghost);                                         // find the ghost node
      Node p = board.findNode("P");                                           // find the pacman node

      board.BFS(g);                                                           // use BFS from the ghost node

      if (p.parent != null) {
         Node current = p;                                        
         Node nextNode = null;                                                // nextNode is where the ghost will move
         String path = "";                                              
         int count = 0;                                                       // count of number of moves in the path
         while (current != g) {                                               // while not at the ghost, 
            path = "(" + current.row + "," + current.col + ") " + path;
            nextNode = current;        
            current = current.parent;                                         // work back from pacman using parent pointers
            count++;                                                          // increase path count
         }
         path = "(" + current.row + "," + current.col + ") " + path;
      
         System.out.print("Ghost " + ghost.toLowerCase() + ": ");
         String dir = "";                                                     // determine the direction the ghost will move based on nextNode
         if (g.row > nextNode.row) {
            dir = "u";                                                        // if row decr, moved up
         }
         if (g.row < nextNode.row) {
            dir = "d";                                                        // if row incr, moved down
         }
         if (g.col > nextNode.col) {
            dir = "l";                                                        // if col decr, moved left
         }
         if (g.col < nextNode.col) {
            dir = "r";                                                        // if col incr, moved right
         }
         System.out.println(dir + " " + count + " " + path);                  // print out direction, number of moves, and path
   
         // move ghost
         if (Character.isUpperCase(g.data.charAt(0))) {                       // if the ghost was originally uppercase,
            g.data = ".";                                                     // replace with a dot
         }
         else {
            g.data = " ";                                                     // else replace with a blank
         }
         if (nextNode.data.equals(".")) {                                     // if the nextNode was a dot, 
            nextNode.data = ghost.toUpperCase();                              // replace with an unpercase ghost
         }
         else {
            nextNode.data = ghost.toLowerCase();                              // else replace with lowercase ghost
         }
         board.print(rows, cols);                                             // print the new board
      }
      else {
         System.out.println("Ghost " + ghost.toLowerCase() + ": cannot move towards Pacman");
         board.print(rows, cols);                                             // print the new board
      }

      
      if (board.findNode("P") == null) {                                      // if pacman nolonger exists,
         endgame = true;                                                      // game is over
         System.out.println("A ghost is not hungry any more!");               // ghosts win 
      }
      board.resetParentAndVisit();                                            // reset parents and visited pointers to run BFS again
   }

   public static void main(final String[] args) throws Exception
   { 
      final Scanner in = new Scanner(new File(args[0]));                      // scanner in scans file inputted from the arguments

      /*Create Board*/
      rows = in.nextInt();                                                    // store number of rows in board
      cols = in.nextInt();                                                    // store number of cols in board
      in.nextLine();

      // read in all spaces and add to the graph
      for (int i = 0; i < rows; i++) {
         String str = in.nextLine();
         for (int j = 0; j < cols; j++) {
            board.addNode(String.valueOf(str.charAt(j)), i, j);               // add the node to the graph
         }
      }
      board.addAllChildren();                                                 // connect nodes together 
      board.print(rows, cols);                                                // print the board

      if (playerCanMove()) {                                                  // check if player can move
         movePlayer();                                                        // move player
      }
      else {
         System.out.println("Player cannot move");                            // player cannot move                       
         endgame = true;                                                      // endgame
      }
      if (board.findNode("G") != null && !endgame) {
      moveGhost("G");                                                         // move ghost G if it exists and game is not over
      }
      if (board.findNode("H") != null && !endgame) {
      moveGhost("H");                                                         // move ghost H if it exists and game is not over
      }
      if (board.findNode("O") != null && !endgame) {
      moveGhost("O");                                                         // move ghost O if it exists and game is not over
      }
      if (board.findNode("S") != null && !endgame) {
      moveGhost("S");                                                         // move ghost S if it exists and game is not over
      }
   }
}
