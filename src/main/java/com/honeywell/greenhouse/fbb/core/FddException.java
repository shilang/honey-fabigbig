package com.honeywell.greenhouse.fbb.core;

public class FddException extends RuntimeException {

    private static final long serialVersionUID = 4956814464034992234L;

    public FddException(String message) {
        super(message);
    }

    public FddException(String message, Throwable cause) {
        super(message, cause);
    }

}
