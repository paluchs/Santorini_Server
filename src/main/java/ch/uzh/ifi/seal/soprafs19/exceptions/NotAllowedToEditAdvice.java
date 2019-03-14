package ch.uzh.ifi.seal.soprafs19.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class NotAllowedToEditAdvice {

    @ResponseBody
    @ExceptionHandler(NotAllowedToEditException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String NotAllowedToEditHandler(NotAllowedToEditException ex) {
        return ex.getMessage();
    }
}