/**
 * This class represents a validation error. Intended to store a response status and a message, for validating user inputs wherever necessary.
 * Default constructor represents the case where there is no error (sus).
 */
package com.example.demo.service;

public class Error {
    public final boolean isError;
    public final int status;
    public final String message;

    public Error() {
        isError = false;
        status = 200;
        message = "Good.";
    }

    public Error(int status, String message) {
        isError = true;
        this.status = status;
        this.message = message;
    }
}
