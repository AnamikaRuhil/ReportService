package com.poc.reports.exception;

public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String message) {
        super("Data not found: " + message);

    }
}
