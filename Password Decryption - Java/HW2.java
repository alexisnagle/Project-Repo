/*
  Author: Alexis Nagle
  Email: anagle2020@my.fit.edu
  Course: CSE 2010
  Section: 03
  Description of this file: Use a recursive method to implement brute force approach to decrypt passwords
*/
import java.util.Scanner;
import java.io.File;
public class HW2
{
   /**
   * Generate different possible strings and test compatability to the encrypted messsage; print the correct password once found
   * @param n is the max length the password can be; the string must not extend beyond this length
   * @param currentPassword is the current string being expaned upon
   * @param encryptedMessage is the encryption of which we are trying to replicate 
   */
   public static void recursiveBruteForce(int n, String currentPassword, String encryptedMessage) {

      // loop through each letter in the alphabet for every possible combination 
      for (char ch = 'a'; ch <= 'z'; ch++) {
         String newPassword = currentPassword + ch; //add the letter to the existing string
       
         //test compatability to the encrypted message
         if (HW2crypto.encrypt(newPassword).equals(encryptedMessage)) {
            System.out.println(newPassword); // if it is the right password, print it to the screen 
            return; // once found and printed, the exit recursion
         }
         // as long as current password is less than max length, keep trying longer passwords
         else if (newPassword.length() < n) {
            recursiveBruteForce(n, newPassword, encryptedMessage); // recursive call to add another character to extend the string
         }
      }
   }
   
   public static void main(final String[] args) throws Exception
   {
      final Scanner in = new Scanner(new File(args[0])); // scanner in scans file inputted from the arguments

      final int maxLength = in.nextInt(); // maxLength stores the maximum length the password can be 

      // continue program until there are no more entries in the file
      while (in.hasNext()) {
         final String encryptedMessage = in.next(); // store the next encrypted message from the file
         recursiveBruteForce(maxLength,"",encryptedMessage); // begin recursive brute force method   
      }
   }
}
