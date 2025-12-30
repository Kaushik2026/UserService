package com.backendlld.userservice.exceptions;

public class InvalidUserNameOrPasswordException extends Exception {
    public InvalidUserNameOrPasswordException(String message) {
        super(message);
    }
}
