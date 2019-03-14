package ch.uzh.ifi.seal.soprafs19.exceptions;

import org.springframework.http.ResponseEntity;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException() {
        super("Wrong password!");
    }

}