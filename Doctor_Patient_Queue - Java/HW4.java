/*
  Author: Alexis Nagle
  Email: anagle2020@my.fit.edu
  Course: CSE 2010
  Section: 03
  Description of this file: Using a heap priority queue, intake patients and their severity levels,
  then assign doctors to patients to most efficiently treat all patients 
*/
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
public class HW4
{
   private static HeapPriorityQueue patients = new HeapPriorityQueue<>();     //heap of waiting patients
   private static HeapPriorityQueue treatments = new HeapPriorityQueue<>();   //heap of ongoing treatments
   private static ArrayList<String> avaliableDoctors = new ArrayList<>();     //list of avaliable doctors

   /** Begin treatment of the patient at the top of the patients heap by the first avaliable doctor,
    *  add treatment to the treaments heap 
    *  @param time is the time at which the treatment will begin
    */
   @SuppressWarnings("unchecked")
   public static void startTreatment(int time) {
      String doctor = avaliableDoctors.get(0);                       // assign the first doctor to the treatment
      String patient = (String)patients.min().getValue();            // choose the patient at the root of the heap to treat
      int esi = ((int[])patients.min().getKey())[0];                 // obtain the esi of the patient
      int endTime = time + (int)Math.pow(2, 6-esi);                  // calculate the end time of the treatment
      
      String[] valueArr = {patient,doctor};                          // create the value of the heap (patient, doctor)
      int[] keyArr = {endTime, time};                                // create the key of the heap (endtime, start time)
      treatments.insert(keyArr,valueArr);                            // add to treatment heap
      patients.removeMin();                                          // remove from patient waitlist heap
      avaliableDoctors.remove(0);                                    // remove the doctor from the avaliable doctor list
      System.out.printf("%s %04d %s %s%n","doctorStartsTreatingPatient", time, doctor, patient);
   }

   /** Finsih the treatment at the top of the treatment heap,
    *  add the doctor from that treatment back to list of avaliable doctors,
    *  if there are more patients then begin a new treatment 
    */
   public static void finishTreatingPatient() {
      String patient = ((String[])treatments.min().getValue())[0];   // patient that was treated
      String doctor = ((String[])treatments.min().getValue())[1];    // doctor that did the treatment
      int time = ((int[])treatments.min().getKey())[0];              // end time of the treatment
      if ((time % 100) >= 60)
         time = (time - 60) + 100;
      if (time >= 2400)
         time = time - 2400;
      treatments.removeMin();                                        // remove patient from treatment heap
      avaliableDoctors.add(doctor);                                  // add the doctor to the list of avaliable doctors
      System.out.printf("%s %04d %s %s%n","doctorFinishesTreatmentAndPatientDeparts", time, doctor, patient);

      //since a doctor is now avaliable
      if(!patients.isEmpty()) {                                      // if there are patients that need treatment
         startTreatment(time);                                       // begin treating the patient
      }
   }

   @SuppressWarnings("unchecked")
   public static void main(final String[] args) throws Exception
   { 
      avaliableDoctors.add("Alice");                                 // add pre-existing doctor Alice to the list of avaliable doctors
      avaliableDoctors.add("Bob");                                   // add pre-existing doctor Bob to the list of avaliable doctors

      final Scanner in = new Scanner(new File(args[0]));             // scanner in scans file inputted from the arguments

      /*iterate through lines in the input*/
      while(in.hasNext()) {
         String command = in.next();                                 // store the first word as the command
         int time = in.nextInt();                                    // store the second as the time 

         /*finish all treatments thats endtime occur prior to the next command time*/
         while (!treatments.isEmpty() && time > ((int[])treatments.min().getKey())[0]) {
            finishTreatingPatient();                                 // finish first treatment
         }

         /*case if a new patient arrives*/
         if (command.equals("patientArrives")) {
            String patient = in.next();                              // store name of patient
            int esi = in.nextInt();                                  // store esi of the patient
            System.out.printf("%s %04d %s %s%n", "patientArrives", time, patient, esi);

            int[] keyArr = {esi, time};                              // the key for the patient contains their esi and arrival time
            patients.insert(keyArr,patient);                         // add the patient to the patient heap

            if (!avaliableDoctors.isEmpty()) {                       // if there is a doctor avaliable
               startTreatment(time);                                 // begin a new treatment
            }
         }

         /*case if a new doctor arrives*/
         else if (command.equals("doctorArrives")) {
            String doctor = in.next();                               // store the name of the doctor
            System.out.printf("%s %04d %s%n","doctorArrives", time, doctor);

            avaliableDoctors.add(doctor);                            // add the doctor to the avaliable doctor list

            if(!patients.isEmpty()) {                                // if there are patients waiting
               startTreatment(time);                                 // treat the most urgent one
            }
         }
      }

      /*once done reading in lines, continue to treat patients until both heaps are empty*/
      while (!patients.isEmpty() || !treatments.isEmpty()) {
         finishTreatingPatient();                                    // finish the next treatment in the heap
      }
   }
}
