/*
 *  DS242 Project - Hospital Management System 
 *  Group Members: Mohammed, Abdullah, Hadi, Hussain
 * 
 *  Make sure to keep the patients as a singly linked list not double 
 *  We wanted to enter the patient information as an input instead of hardcoding it. -
 *  - In case we wanted to improve on it in the future and as a chance for us to explore more with this project
 */

import java.util.HashMap;
import java.util.LinkedList; // dont use this for patients only for medical history
import java.util.Scanner;
import java.util.PriorityQueue;

enum Severity { // Setup severity for future triage
    LOW,
    MID,
    HIGH
}

public class Patient implements Comparable<Patient> {

    private int patientID;
    private String name;
    private int age;
    private String contactInfo;
    private LinkedList<String> medicalHistory; // double linked list for medical history
    private Severity severity;
    private static PatientRecords patientList = new PatientRecords(); // patient singly linked list to be used in the
                                                                      // program -- required in by the project

    public static void addPatient(Patient patient) {
        patientList.add(patient);
    }

    public Patient(int patientID, String name, int age, String contactInfo) { // constructor for patient
        this.patientID = patientID;
        this.name = name;
        this.age = age;
        this.contactInfo = contactInfo;
        this.medicalHistory = new LinkedList<>();

    }

    @Override
    public int compareTo(Patient other) {
        return other.severity.compareTo(this.severity); // High severity first
    }

    @Override
    public String toString() {
        return "ID: " + patientID + ", Name: " + name + ", Severity: " + severity;
    }

    // getters and setter

    public int getPatientID() {
        return patientID;

    }

    public static Patient getPatientByID(int id) {
        return patientList.searchByID(id);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getcontactInfo() {
        return contactInfo;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setcontactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public void addMedicalHistory(String history) {
        medicalHistory.add(history);
    }

    public void displayMedicalHistory() {
        for (String history : medicalHistory) {
            System.out.println(history);
        }

    }

    // main class
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); // scanner setup
        EmergencyQueue emergencyQueue = new EmergencyQueue(); // emergency queue setup
        DoctorAssignments doctorAssignments = new DoctorAssignments();

        while (true) {
            System.out.println("\n Welcome to DS242 Hospital Management System.\n");
            System.out.println("Please select an option from the following menu:");
            System.out.println("1. Manage Patients Records");
            System.out.println("2. Manage Emergency Queue");
            System.out.println("3. Treatment History");
            System.out.println("4. Doctor Assignments");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) { // manage patients records

                while (true) { // loop for options and input
                    System.out.println(
                            "\nEnter 1 to add a new patient, 2 to manage existing patients, 3 to view all patients, 4 to go back\n");
                    int menuChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (menuChoice == 4) { // to exit
                        System.out.println("Exiting");
                        break;
                    }
                    if (menuChoice == 1) { // new patient
                        System.out.println("Enter patient ID: ");
                        int patientID = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.println("Enter age: ");
                        int age = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter contact information: ");
                        String contactInfo = scanner.nextLine();
                        Patient patient = new Patient(patientID, name, age, contactInfo);
                        addPatient(patient);
                        System.out.println("Enter medical history (type 'done' to finish):");
                        while (true) {
                            String history = scanner.nextLine();
                            if (history.equalsIgnoreCase("done"))
                                break;
                            patient.addMedicalHistory(history);
                        }

                        System.out.println("\nPatient added successfully.\n");

                    } else if (menuChoice == 2) { // existing patients
                        System.out.println("\nManage existing patients\n");
                        System.out.println("Enter patient ID to manage:");
                        int patientIDToManage = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println(
                                "Enter 1 to view patient details, 2 to update information, 3 to discharge patient: ");
                        int manageChoice = scanner.nextInt();
                        scanner.nextLine();

                        Patient patient = patientList.searchByID(patientIDToManage);
                        if (patient == null) {
                            System.out.println("\nPatient Is Not Registered\n");
                        } else {
                            switch (manageChoice) {
                                case 1:
                                    System.out.println("\nViewing patient " + patientIDToManage + " details:\n");
                                    System.out.println("Patient Name: " + patient.getName());
                                    System.out.println("Patient Age: " + patient.getAge());
                                    System.out.println("Contact Information: " + patient.getcontactInfo());
                                    System.out.println("Known Medical History:");
                                    patient.displayMedicalHistory();
                                    System.out.println();
                                    break;

                                case 2:
                                    System.out.println("Update Name: ");
                                    String newName = scanner.nextLine();
                                    System.out.println("Update Age: ");
                                    int newAge = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.println("Update Contact Information: ");
                                    String newcontactInfo = scanner.nextLine();
                                    System.out.println("Enter new history entry (type 'done' to cancel):");
                                    String entry = scanner.nextLine();
                                    if (!entry.equalsIgnoreCase("done")) {
                                        patient.addMedicalHistory(entry);
                                        System.out.println("Medical history updated.");
                                    }

                                    patient.setName(newName);
                                    patient.setAge(newAge);
                                    patient.setcontactInfo(newcontactInfo);

                                    System.out.println("\nPatient Information Updated Successfully.\n");
                                    break;

                                case 3:
                                    if (patientList.remove(patientIDToManage)) {
                                        System.out.println("\nRemoving discharged patient " + patientIDToManage + "\n");
                                    } else {
                                        System.out.println("\nFailed to remove patient.\n");
                                    }
                                    break; // breaking the loop of updating patients

                                default:
                                    System.out.println("Invalid choice");

                            }

                        }

                    } else if (menuChoice == 3) {// display all patients -- moved it to this menu instead of later in
                                                 // the options
                        System.out.println("\nDisplaying All patients:\n");
                        patientList.showAll();
                        continue; // show the menu again
                    }
                }

            }

            else if (choice == 2) { // coded by Hadi - talk to him for errors
                System.out.println("\nEmergency Queue\n");
                boolean inMain = true;
                while (inMain) {
                    System.out.println("\nEmergency Queue Menu:\n");
                    System.out.println("1. Add Patient To The Queue");
                    System.out.println("2. Treat Next Patient");
                    System.out.println("3. View Next Patient");
                    System.out.println("4. Is The Queue Empty?");
                    System.out.println("5. How Many Patients In The Queue");
                    System.out.println("6. Back");
                    int emergencyChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (emergencyChoice) {

                        case 1:
                            System.out.print("Enter Patient ID: ");
                            int id = scanner.nextInt();
                            scanner.nextLine();
                            Patient patient = Patient.getPatientByID(id);
                            if (patient == null) {
                                System.out.println("Patient not found. You may need to register the patient first.");
                                break;
                            }

                            System.out.print("Set Severity (LOW, MID!!, HIGH!!!): ");
                            String severityStr = scanner.nextLine().toUpperCase();

                            try {
                                Severity severity = Severity.valueOf(severityStr);
                                patient.setSeverity(severity);
                                emergencyQueue.enqueue(patient);
                                System.out.println("Patient added to the emergency queue.");
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid severity. Try again.");
                            }
                            break;
                        case 2:
                            System.out.println("Treating: " + emergencyQueue.dequeue());
                            break;
                        case 3:
                            if (emergencyQueue.isEmpty()) {
                                System.out.println("No patients in queue.");
                            } else {
                                System.out.println("Next: " + emergencyQueue.peek());

                            }
                            break;
                        case 4:
                            if (emergencyQueue.isEmpty()) {
                                System.out.println("The emergency queue is now empty.");
                            } else {
                                System.out.println("There are patients in the emergency queue.");
                            }
                            break;
                        case 5:
                            System.out.println(
                                    "There are " + emergencyQueue.size() + " patients still in the emergency queue.");
                            break;
                        case 6:
                            inMain = false;
                            System.out.println("Returning to main menu...");
                            break;
                        default:
                            System.out.println("Invalid.");

                    }
                }
            } else if (choice == 3) { // Hussain
                System.out.println("\nTreatment History: ");

            } else if (choice == 4) {

                System.out.println("\nDoctor Assignment: ");
                boolean inDoctormenu = true;
                while (inDoctormenu) {
                    System.out.println("\nDoctor Menu:");
                    System.out.println("1. Add new Doctor");
                    System.out.println("2. Retrive Doctor by ID");
                    System.out.println("3. List all Doctors");
                    System.out.println("4. Back");
                    int doctorChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (doctorChoice) {
                        case 1:
                            System.out.println("Enter Doctor ID:");
                            String docID = scanner.nextLine();
                            System.out.println("Enter Doctor Name:");
                            String docName = scanner.nextLine();
                            System.out.println("Enter Department:");
                            String department = scanner.nextLine();
                            System.out.println("Enter Schedule:");
                            String schedule = scanner.nextLine();
                            Doctor newDoctor = new Doctor(docID, docName, department, schedule);
                            doctorAssignments.addDoctor(newDoctor);
                            System.out.println("Doctor Added.");

                            break;

                        case 2:
                            System.out.println("Enter Docotor ID to search:");
                            String idToFind = scanner.nextLine();
                            Doctor found = doctorAssignments.getDoctor(idToFind);
                            if (found != null) {
                                System.out.println("Found.");
                            } else {
                                System.out.println("Doctor not found.");
                            }
                            break;
                        case 3:
                            doctorAssignments.listAlldoctors();
                            break;
                        case 4:
                            inDoctormenu = false;
                            break;
                        default:
                            System.out.println("Error.");
                    }
                }
            } else if (choice == 5) {

                System.out.println("Exiting"); // setup for exit
                break;

            }

        }
        scanner.close(); // makeeeeee suuuuuureee to clooooooooose the scanner
    }
}

class EmergencyQueue { // need this after the main class we dont want it before
    private PriorityQueue<Patient> triageQueue;

    public EmergencyQueue() {
        triageQueue = new PriorityQueue<>();
    }

    public void enqueue(Patient patient) {
        triageQueue.offer(patient);
    }

    public Patient dequeue() {
        return triageQueue.poll();
    }

    public Patient peek() {
        return triageQueue.peek();
    }

    public boolean isEmpty() {
        return triageQueue.isEmpty();
    }

    public int size() {
        return triageQueue.size();
    }
}

class PatientRecords { // this is for the singly linked list

    private PatientNode head;
    private PatientNode tail;
    private int size;

    class PatientNode {
        Patient data;
        PatientNode next;

        public PatientNode(Patient patient) {
            this.data = patient;
            this.next = null;
        }

        public Patient getPatient() {
            return data;
        }

    }

    public PatientRecords() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void add(Patient patient) {
        PatientNode newNode = new PatientNode(patient);
        if (head == null) {

            head = newNode;
            tail = newNode;
        } else {

            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public boolean remove(int patientID) {
        if (head == null)
            return false;

        if (head.data.getPatientID() == patientID) {
            head = head.next;
            if (head == null)
                tail = null;

            size--;
            return true;
        }
        PatientNode current = head;
        while (current.next != null && current.next.data.getPatientID() != patientID) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            if (current.next == null)
                tail = current;
            size--;
            return true;
        }

        return false; // the patient is not found
    }

    public Patient searchByID(int patientID) {
        PatientNode current = head;
        while (current != null) {
            if (current.data.getPatientID() == patientID) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public void showAll() {
        PatientNode current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
}

// Abdullah Alqahtani
class Doctor {
    private String doctorID, name, department, schedule;

    public Doctor(String doctorID, String name, String department, String schedule) {
        this.doctorID = doctorID;
        this.name = name;
        this.department = department;
        this.schedule = schedule;

    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return "Doctor ID: " + doctorID + ", Name: " + name + ",Department: " + department + ",Schedule: " + schedule;
    }
}

class DoctorAssignments {
    private HashMap<String, Doctor> doctorTable = new HashMap<>();

    public void addDoctor(Doctor doctor) {
        doctorTable.put(doctor.getDoctorID(), doctor);
    }

    public Doctor getDoctor(String doctorID) {
        return doctorTable.get(doctorID);
    }

    public void listAlldoctors() {
        if (doctorTable.isEmpty()) {
            System.out.println("No Doctors assigned.");
            return;

        }
        for (Doctor doc : doctorTable.values()) {
            System.out.println(doc);
        }
    }
}
