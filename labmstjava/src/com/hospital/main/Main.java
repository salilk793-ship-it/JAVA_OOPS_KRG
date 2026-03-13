package com.hospital.main;

import com.hospital.patient.Patient;
import com.hospital.service.HospitalService;
import com.hospital.exception.InvalidAgeException;
import com.hospital.exception.DuplicatePatientException;
import com.hospital.exception.PatientNotFoundException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HospitalService hs = new HospitalService();
        int choice;

        do {
            showMenu();
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addPatient(sc, hs);
                    break;
                case 2:
                    hs.displayPatients();
                    break;
                case 3:
                    searchPatient(sc, hs);
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
            System.out.println();
        } while (choice != 4);

        sc.close();
    }

    private static void showMenu() {
        System.out.println("\n=== HOSPITAL MANAGEMENT ===");
        System.out.println("1. Add Patient");
        System.out.println("2. Show All Patients");
        System.out.println("3. Search Patient");
        System.out.println("4. Exit");
        System.out.println("==========================");
    }

    private static void addPatient(Scanner sc, HospitalService hs) {
        try {
            System.out.println("\n--- Add Patient ---");
            
            System.out.print("Enter ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Age: ");
            int age = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Disease: ");
            String disease = sc.nextLine();

            Patient p = new Patient(id, name, age, disease);
            hs.addPatient(p);

        } catch (InvalidAgeException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DuplicatePatientException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchPatient(Scanner sc, HospitalService hs) {
        try {
            System.out.println("\n--- Search ---");
            System.out.print("Enter ID: ");
            int searchId = sc.nextInt();

            Patient p = hs.searchPatient(searchId);
            System.out.println("Found:");
            p.show();

        } catch (PatientNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
