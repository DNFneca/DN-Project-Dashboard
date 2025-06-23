package com.dn.projectdashboard.Sprint;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SprintNotFoundAdvice {

    @ExceptionHandler(SprintNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String sprintNotFoundHandler(SprintNotFoundException ex) {
        return ex.getMessage();
    }
}
