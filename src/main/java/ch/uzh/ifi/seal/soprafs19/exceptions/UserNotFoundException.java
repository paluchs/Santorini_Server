package ch.uzh.ifi.seal.soprafs19.exceptions;

import org.springframework.http.ResponseEntity;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Cannot find user");
    }

}