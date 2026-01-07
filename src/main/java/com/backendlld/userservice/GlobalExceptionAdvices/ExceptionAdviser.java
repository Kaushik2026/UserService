package com.backendlld.userservice.GlobalExceptionAdvices;

import com.backendlld.userservice.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdviser {

    @ExceptionHandler(InvalidUserNameOrPasswordException.class)
    public ResponseEntity<String> handleInvalidUserNameException(InvalidUserNameOrPasswordException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }
    @ExceptionHandler(UserAlreadyLoggedInException.class)
    public ResponseEntity<String> handleUserAlreadyLoggedInException(UserAlreadyLoggedInException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }
}
