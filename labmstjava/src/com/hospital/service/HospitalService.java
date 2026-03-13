package com.hospital.service;

import com.hospital.patient.Patient;
import com.hospital.exception.InvalidAgeException;
import com.hospital.exception.DuplicatePatientException;
import com.hospital.exception.PatientNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalService {
    private String filename = "patients.txt";
    private List<Patient> patients;

    public HospitalService() {
        patients = new ArrayList<>();
        loadPatients();
    }

    public void loadPatients() {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        int age = Integer.parseInt(parts[2].trim());
                        String disease = parts[3].trim();
                        patients.add(new Patient(id, name, age, disease));
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void addPatient(Patient p) throws InvalidAgeException, DuplicatePatientException {
        if (p.getAge() < 0 || p.getAge() > 120) {
            throw new InvalidAgeException("Age must be between 0 and 120.");
        }

        for (Patient existing : patients) {
            if (existing.getId() == p.getId()) {
                throw new DuplicatePatientException("Patient with ID " + p.getId() + " already exists!");
            }
        }

        if (p.isCritical()) {
            System.out.println("\n>>> PRIORITY ALERT <<<");
            System.out.println("Patient: " + p.getName() + " (ID: " + p.getId() + ")");
            System.out.println("Age: " + p.getAge() + " | Disease: " + p.getDisease());
            System.out.println(">>> IMMEDIATE ATTENTION REQUIRED <<<\n");
        }

        patients.add(p);
        savePatient(p);
        System.out.println("Patient added successfully!");
    }

    private void savePatient(Patient p) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write(p.getId() + "," + p.getName() + "," + p.getAge() + "," + p.getDisease());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    public Patient searchPatient(int id) throws PatientNotFoundException {
        for (Patient p : patients) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new PatientNotFoundException("Patient with ID " + id + " not found!");
    }

    public void displayPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        System.out.println("\n=== ALL PATIENTS ===");
        for (Patient p : patients) {
            p.show();
        }
        System.out.println("===================\n");
    }

    public int getCount() {
        return patients.size();
    }
}
