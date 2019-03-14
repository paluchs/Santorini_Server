package ch.uzh.ifi.seal.soprafs19.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.*;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import ch.uzh.ifi.seal.soprafs19.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class   UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    // Aggregate root

    @GetMapping("/users")
    Resources<Resource<User>> all() {

        List<Resource<User>> users = StreamSupport.stream(this.service.getUsers().spliterator(), false)
                .map(user -> new Resource<>(user,
                        linkTo(methodOn(UserController.class).one(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).all()).withRel("users")))
                .collect(Collectors.toList());

        return new Resources<>(users,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    User createUser(@RequestBody User newUser) {
        if (this.service.hasUsername(newUser.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        return this.service.createUser(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    Resource<User> one(@PathVariable Long id) {

        User user = this.service.getUser(id)
                .orElseThrow(() -> new UserNotFoundException());

        return new Resource<>(user,
                linkTo(methodOn(UserController.class).one(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    User updateUser(@PathVariable Long id, @RequestBody User changedUser) {
        User currentUser = this.service.getUser(id)
                .orElseThrow(() -> new UserNotFoundException());

        if (! currentUser.getToken().equals(changedUser.getToken())) {
            throw new NotAllowedToEditException();
        }

        if (this.service.hasUsername(changedUser.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        return this.service.updateUser(currentUser, changedUser);
    };

    // Login
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    User authenticate(@RequestBody User possibleUser) {
        User user = this.service.findUsername(possibleUser.getUsername())
                .orElseThrow(() -> new UserNotFoundException());

        if (user.getPassword().equals(possibleUser.getPassword())) {
            return this.service.authenticateUser(user);
        }
        throw new WrongPasswordException();
    };

    // Logout
    @PostMapping("/login/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    User logout(@PathVariable Long id) {
        User user = this.service.getUser(id)
                .orElseThrow(() -> new UserNotFoundException());
        return this.service.logoutUser(user);
    };
}

