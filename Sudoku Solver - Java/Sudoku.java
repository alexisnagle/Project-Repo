import java.util.Scanner;
import java.util.Stack;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

// Main runtime class
public class Sudoku {
   
   //Globals
   public static Scanner scan = new Scanner(System.in);  // Scanner reads from input stream
   public static int inputSize = scan.nextInt();         // User-defined square grid size N x N
   public static int count = 0;                          // Track's current box position in grid

   public static void main(String[] args) {
 
      // *
      // Simulation Set-up 
      // *
      
      // Stack orders depth and order of grid permutations
      long nano_start = System.nanoTime();
      Stack<Node> orderStack = new Stack<Node>();

      // Impossibility bool
      boolean possible = true;
          
      // Read in initial game board and store in 2d array
      char[][] initialBoard = new char[inputSize][inputSize];
      for (int i = 0; i < inputSize; i++) {
         for (int j = 0; j < inputSize; j++){
            initialBoard[i][j] = scan.next().charAt(0);
         }
      }

      // Find position of the first empty spot
      // Count is global, will be used by Node constructor
      for (int i = 0; i < Math.pow(inputSize, 2) - 1; i++) {
         if (initialBoard[i/inputSize][i%inputSize] == '_') {
            break;
         }
         count++;
      }
      
      // Create current node
      Node currentNode = new Node(initialBoard);
      orderStack.push(currentNode);
      
      // *
      // Main run-time loop
      // *

      // Continue until last position in grid is filled
      while(count != Math.pow(inputSize, 2)) {
         
         // Skip over already set values
         if (currentNode.getGrid()[currentNode.getRow()][currentNode.getCol()] != '_'){
             // Push node to stack; increment count
             orderStack.push(currentNode);
             count++;

             // Make next child node, only if not on the last position
             if (count != Math.pow(inputSize, 2) ){
               currentNode = new Node(currentNode.getGrid());
            }
         }
         else {
            // Check rows and columns, remove values from the list
            for (int i = 0; i < inputSize; i++){
               char rowValue = currentNode.getGrid()[currentNode.getRow()][i];
               char colValue = currentNode.getGrid()[i][currentNode.getCol()]; 
               currentNode.updateValues(rowValue);
               currentNode.updateValues(colValue);
            }

            // Check if list is empty; Pop node, decrement count; return to step 1
            if(currentNode.getValues().isEmpty() && orderStack.size() - 1 != 0) {          // If there are no more valid solutions in this subtree
               currentNode = orderStack.pop();
               count--;
               while(currentNode.isPermanent()) {     // Backtrack & skip over permanent values in the grid
                  currentNode = orderStack.pop();
                  count--;
               }
               currentNode.updateGrid('_');  // Re-assign previous non-permanent position to its original state
            }
            else if (currentNode.getValues().isEmpty() && orderStack.size() - 1 == 0) {    // If there are no valid solutions for the entire grid
               possible = false;
               break;
            }
            else {
               // Insert next avaliable number to currrent; Remove this number from the list
               char insertValue = currentNode.getValues().get(0);
               currentNode.updateGrid(insertValue);
               currentNode.updateValues(insertValue);
               
               // Push node to stack; increment count
               orderStack.push(currentNode);
               count++;
 
               // Make next child node, only if not on the last position
               if (count != Math.pow(inputSize, 2) ){
                  currentNode = new Node(currentNode.getGrid());
               }
            }
         }
      }

      // Print Solution
      if (possible) {
         //Print Board 
         for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++){
               //System.out.print(currentNode.getGrid()[i][j] + " ");
            }
            //System.out.println();
         }
      }
      else {
         //System.out.println("ERR: NO SOLUTION");
      }
      long nano_end = System.nanoTime();
      System.out.println((nano_end - nano_start));
   }

   // Nested Class  
   public static class Node {
      // Attributes & vars
      private char[][] grid; 
      private int row;
      private int col;
      private List<Character> possibleValues;
      private boolean permanent;
   
      // Constructor
      public Node (char[][] grid) {
      
         // Initialize vars
         this.grid = grid;
         this.row = count/inputSize;
         this.col = count%inputSize;
         this.possibleValues = new ArrayList<>();
         this.permanent = false;

         // Sets permanent to true if this value is pre-assigned
         if (grid[this.row][this.col] != '_') {
            this.permanent = true;
         }
        
         // Generate each node's list
         for(int i = 1; i <= inputSize; i++) {
            possibleValues.add(Integer.toString(i).charAt(0));
         }
      }

      // Changes current position's character in the grid
      // Assumes that the row and col numbers are up-to-date
      public void updateGrid(char character) {
         this.grid[this.row][this.col] = character;
      }      
      
      // Getter method for grid
      public char[][] getGrid(){
         return this.grid;
      }   

      // Getter method for row
      public int getRow() {
         return this.row;
      } 
      
      // Getter method for col
      public int getCol(){
         return this.col;
      }

      // Getter method for possibleValues
      public List<Character> getValues(){
         return this.possibleValues;
      }

      // Getter method for permanent
      public boolean isPermanent(){
         return this.permanent;
      }

      // Setter method for possibleValues
      public void updateValues(char num){
         Character charNum = Character.valueOf(num);
         this.possibleValues.remove(charNum);
      }
   }
}

