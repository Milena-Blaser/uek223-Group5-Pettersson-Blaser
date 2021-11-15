package com.example.demo.exception;

public class NotTheOwnerException extends Exception {
    public NotTheOwnerException(String message) {
        super(message);
    }
}
