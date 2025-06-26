package com.dn.projectdashboard.Token;

import com.dn.projectdashboard.Team.TeamNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class TokenNotFoundAdvice {

    @ExceptionHandler(TeamNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String teamNotFoundHandler(TeamNotFoundException ex) {
        return ex.getMessage();
    }
}
