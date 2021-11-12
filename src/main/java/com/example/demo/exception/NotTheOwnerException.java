package com.example.demo.exception;

public class NotTheOwnerException extends Exception {
    public NotTheOwnerException() {
        super("You're not the owner of this entry and you do not have the authority to edit it");
    }
}
