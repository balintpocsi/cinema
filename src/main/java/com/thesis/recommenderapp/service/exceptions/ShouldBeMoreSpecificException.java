package com.thesis.recommenderapp.service.exceptions;

import lombok.Getter;

@Getter
public class ShouldBeMoreSpecificException extends RuntimeException {

    private String imdbId;

    public ShouldBeMoreSpecificException(String message, String imdbId) {
        super(message);
        this.imdbId = imdbId;
    }

}
