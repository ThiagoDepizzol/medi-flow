package com.medi.flow.utils;

import java.util.StringJoiner;

public class MessageResponseDTO {

    private String message;

    public MessageResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MessageResponseDTO.class.getSimpleName() + "[", "]")
                .add("message='" + message + "'")
                .toString();
    }
}
