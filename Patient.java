import java.util.LinkedList;
import java.util.Scanner;

public class Patient {
    private int patientID;
    private String name;
    private int age;
    private String contactinformation;
    private LinkedList<String> medicalHistory;
    private static LinkedList<Patient> patientList = new LinkedList<>();

    public static void addPatient(Patient patient) {
        patientList.add(patient);
    }

    public Patient(int patientID, String name, int age, String contactinformation) {
        this.patientID = patientID;
        this.name = name;
        this.age = age;
        this.contactinformation = contactinformation;
        this.medicalHistory = new LinkedList<>();

    }

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

    public static void main(String[] args) {
        // Patient p1 = new Patient(123, "John", 25, "123-456-7890");
        // p1.addMedicalHistory("Fever");
        // p1.addMedicalHistory("Headache");
        // p1.displayMedicalHistory();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter 1 to add a new patient, 2 to manage existing patients, 3 to exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 3) {
                System.out.println("Exiting");
                break;
            }
            if (choice == 1) {
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

            } else if (choice == 2) {
                System.out.println("Mange existing patients");
                System.out.println("Enter patient ID to manage:");
                int patientIDToManage = scanner.nextInt();
                scanner.nextLine();

                System.out.println(
                        "Enter 1 to view patient details, 2 to update information, 3 to remove discharged patients");
                int manageChoice = scanner.nextInt();
                scanner.nextLine();

                boolean found = false;

                for (int i = 0; i < patientList.size(); i++) {
                    Patient patient = patientList.get(i);
                    if (patient.getPatientID() == patientIDToManage) {
                        found = true;
                        if (manageChoice == 1) {
                            System.out.println("Viewing patient " + patientIDToManage + " details");
                            System.out.println("Patient Name: " + patient.getName());
                            System.out.println("Patient Age: " + patient.getAge());
                            System.out.println("Contact Information" + patient.getContactInformation());
                            System.out.println("Known Medical History");
                            patient.displayMedicalHistory();

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

                            System.out.println("Patient Information Updated Successfully.");

                        } else if (manageChoice == 3) {
                            patientList.remove(i);
                            System.out.println("Removing discharged patient" + patientIDToManage);

                        } else {
                            System.out.println("Invalid choice");
                        }

                        break;

                    }

                }
                if (!found) {
                    System.out.println("Patient Not Found");
                }

            }
        }
        scanner.close();
    }
}