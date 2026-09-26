package com.sbc.exception;

public class BusinessNotFoundException extends RuntimeException {

    public BusinessNotFoundException(String id) {
        super("Business not found: " + id);
    }
}