package com.teamr.runardo.uaapstatsdata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourName, String fieldName, String fieldValue) {
        super(String.format("%s not found with the given input data %s: '%s'", resourName, fieldName, fieldValue));
    }
}
