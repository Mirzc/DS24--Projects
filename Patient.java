import java.util.LinkedList;
import java.util.Scanner;

public class Patient {
    private int patientID;
    private String name;
    private int age;
    private String contactinformation;
    private LinkedList<String> medicalHistory;
    private static LinkedList<Patient> patientList = new LinkedList<>(); // patient list to be used in the program

    public static void addPatient(Patient patient) {
        patientList.add(patient);
    }

    public Patient(int patientID, String name, int age, String contactinformation) { // constructor for patient
        this.patientID = patientID;
        this.name = name;
        this.age = age;
        this.contactinformation = contactinformation;
        this.medicalHistory = new LinkedList<>();

    }

    // getters and setter
    public int getPatientID() {
        return patientID;

    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getContactInformation() {
        return contactinformation;
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

    public void setContactInformation(String contactInformation) {
        this.contactinformation = contactInformation;
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
        // if we ever need to hard code the data, but currently not needed ---------
        // delete afterwards

        // Patient p1 = new Patient(123, "John", 25, "123-456-7890");
        // p1.addMedicalHistory("Fever");
        // p1.addMedicalHistory("Headache");
        // p1.displayMedicalHistory();

        Scanner scanner = new Scanner(System.in); // scanner setup

        while (true) { // loop for options and input
            System.out.println("\nEnter 1 to add a new patient, 2 to manage existing patients, 3 to exit\n");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 3) { // to exit
                System.out.println("Exiting");
                break;
            }
            if (choice == 1) { // new patient
                System.out.println("Enter patient ID: ");
                int patientID = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter name: ");
                String name = scanner.nextLine();
                System.out.println("Enter age: ");
                int age = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter contact information: ");
                String contactinformation = scanner.nextLine();
                Patient patient = new Patient(patientID, name, age, contactinformation);
                addPatient(patient);
                System.out.println("Enter medical history (type 'done' to finish):");
                while (true) {
                    String history = scanner.nextLine();
                    if (history.equalsIgnoreCase("done"))
                        break;
                    patient.addMedicalHistory(history);
                }

                System.out.println("\nPatient added successfully.\n");

            } else if (choice == 2) { // existing patients
                System.out.println("\nManage existing patients\n");
                System.out.println("Enter patient ID to manage:");
                int patientIDToManage = scanner.nextInt();
                scanner.nextLine();

                System.out.println(
                        "Enter 1 to view patient details, 2 to update information, 3 to remove discharged patients");
                int manageChoice = scanner.nextInt();
                scanner.nextLine();

                boolean found = false;

                for (int i = 0; i < patientList.size(); i++) { // loop to update patient menu
                    Patient patient = patientList.get(i);
                    if (patient.getPatientID() == patientIDToManage) {
                        found = true;
                        if (manageChoice == 1) {
                            System.out.println("\nViewing patient " + patientIDToManage + " details:\n");
                            System.out.println("Patient Name: " + patient.getName());
                            System.out.println("Patient Age: " + patient.getAge());
                            System.out.println("Contact Information: " + patient.getContactInformation());
                            System.out.println("Known Medical History:");
                            patient.displayMedicalHistory();
                            System.out.println();

                        } else if (manageChoice == 2) {
                            System.out.println("Update Name: ");
                            String newName = scanner.nextLine();
                            System.out.println("Update Age: ");
                            int newAge = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Update Contact Information: ");
                            String newContactInformation = scanner.nextLine();
                            System.out.println("Enter new history entry (type 'done' to cancel):");
                            String entry = scanner.nextLine();
                            if (!entry.equalsIgnoreCase("done")) {
                                patient.addMedicalHistory(entry);
                                System.out.println("Medical history updated.");
                            }

                            patient.setName(newName);
                            patient.setAge(newAge);
                            patient.setContactInformation(newContactInformation);

                            System.out.println("\nPatient Information Updated Successfully.\n");

                        } else if (manageChoice == 3) {
                            patientList.remove(i);
                            System.out.println("\nRemoving discharged patient " + patientIDToManage + "\n");

                        } else {
                            System.out.println("Invalid choice");
                        }

                        break; // breaking the loop of updating patients

                    }

                }
                if (!found) { // patient not found prompt
                    System.out.println("\nPatient Not Found\n");
                }

            }
        }
        scanner.close(); // makeeeeee suuuuuureee to clooooooooose the scanner
    }
}
import java.util.PriorityQueue;
import java.util.Scanner;

enum Severity {
    LOW,
    MID,
    HIGH
}

class EmergencyPatient implements Comparable<EmergencyPatient> {
    private int patientID;
    private String name;
    private Severity severity;

    public EmergencyPatient(int patientID, String name, Severity severity) {
        this.patientID = patientID;
        this.name = name;
        this.severity = severity;
    }

    public int getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public Severity getSeverity() {
        return severity;
    }

    @Override
    public int compareTo(EmergencyPatient other) {
        return other.severity.compareTo(this.severity);
    }

    @Override
    public String toString() {
        return "Patient ID: " + patientID + ", Name: " + name + ", Severity: " + severity;
    }
}

public class EmergencyQueue {
    private PriorityQueue<EmergencyPatient> triageQueue;

    public EmergencyQueue() {
        triageQueue = new PriorityQueue<>();
    }

    public void enqueue(EmergencyPatient patient) {
        triageQueue.offer(patient);
    }

    public EmergencyPatient dequeue() {
        return triageQueue.poll();
    }

    public EmergencyPatient peek() {
        return triageQueue.peek();
    }

    public boolean isEmpty() {
        return triageQueue.isEmpty();
    }

    public int size() {
        return triageQueue.size();
    }

    public static void main(String[] args) {
        EmergencyQueue emergencyQueue = new EmergencyQueue();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nEmergency Triage System");
            System.out.println("1. Add Patient");
            System.out.println("2. Treat Next");
            System.out.println("3. View Next");
            System.out.println("4. Is Empty");
            System.out.println("5. Size");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Severity (LOW, MID, HIGH): ");
                    String severityStr = scanner.nextLine().toUpperCase();
                    try {
                        Severity severity = Severity.valueOf(severityStr);
                        emergencyQueue.enqueue(new EmergencyPatient(id, name, severity));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid Severity.");
                    }
                    break;
                case 2:
                    System.out.println("Treating: " + emergencyQueue.dequeue());
                    break;
                case 3:
                    System.out.println("Next: " + emergencyQueue.peek());
                    break;
                case 4:
                    System.out.println("Empty: " + emergencyQueue.isEmpty());
                    break;
                case 5:
                    System.out.println("Size: " + emergencyQueue.size());
                    break;
                case 6:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid.");
            }
        }
    }
}