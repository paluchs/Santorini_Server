package ch.uzh.ifi.seal.soprafs19.exceptions;

import org.springframework.http.ResponseEntity;

public class NotAllowedToEditException extends RuntimeException {

    public NotAllowedToEditException() { super("This username is already taken!"); }

}
