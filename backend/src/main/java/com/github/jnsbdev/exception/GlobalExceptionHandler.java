package com.github.jnsbdev.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler
{

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleBadCredentials(BadCredentialsException ex) {
        return new ErrorMessage("Bad credentials: " + ex.getMessage());
    }

    // Missing Body
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorMessage handleMissingBody(HttpMessageNotReadableException ex) {
        return new ErrorMessage("Request-Body fehlt oder ist ungültig: " + ex.getMessage());
    }

    // null/invalid values
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorMessage handleIllegalArgumentException(Exception ex) {
        return new ErrorMessage("Ein Fehler ist aufgetreten: " + ex.getMessage());
    }

    // ID does not exist
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorMessage handleNoSuchElementException(Exception ex) {
        return new ErrorMessage(ex.getMessage());
    }

    // Default
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleException(Exception ex) {
        return new ErrorMessage("Ein unbekannter Fehler ist aufgetreten: " + ex.getMessage());
    }

}
