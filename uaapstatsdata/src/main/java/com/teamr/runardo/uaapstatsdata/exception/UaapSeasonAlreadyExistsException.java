package com.teamr.runardo.uaapstatsdata.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UaapSeasonAlreadyExistsException extends RuntimeException {
    public UaapSeasonAlreadyExistsException(String message) {
        super(message);
    }
}
