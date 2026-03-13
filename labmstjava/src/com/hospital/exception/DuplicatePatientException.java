package com.hospital.exception;

public class DuplicatePatientException extends Exception {
    public DuplicatePatientException(String msg) {
        super(msg);
    }
}
