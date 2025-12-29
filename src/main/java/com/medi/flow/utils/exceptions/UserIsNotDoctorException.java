package com.medi.flow.utils.exceptions;

public class UserIsNotDoctorException extends RuntimeException {
    public UserIsNotDoctorException(String message) {
        super(message);
    }
}
