package com.thesis.recommenderapp.service.exceptions;

public class EmailCouldNotBeSentException extends RuntimeException {

    public EmailCouldNotBeSentException(String message) {
        super(message);
    }

}
