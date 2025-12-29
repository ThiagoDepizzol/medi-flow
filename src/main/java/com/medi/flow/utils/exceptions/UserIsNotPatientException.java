package com.medi.flow.utils.exceptions;

public class UserIsNotPatientException extends RuntimeException {
    public UserIsNotPatientException(String message) {
        super(message);
    }
}
