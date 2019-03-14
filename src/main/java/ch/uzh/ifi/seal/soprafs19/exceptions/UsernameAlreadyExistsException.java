package ch.uzh.ifi.seal.soprafs19.exceptions;

import org.springframework.http.ResponseEntity;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException() {
        super("This username is already taken!");
    }

}
