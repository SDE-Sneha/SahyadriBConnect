package com.sbc.exception;

public class InvalidBusinessStatusTransitionException
        extends RuntimeException {

    public InvalidBusinessStatusTransitionException(
            String message) {

        super(message);
    }
}