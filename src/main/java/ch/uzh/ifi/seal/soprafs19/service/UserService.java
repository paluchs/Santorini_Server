package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {return this.userRepository.findById(id);}

    public Optional<User> findUsername(String user) {return this.userRepository.findByUsername(user);}

    public Optional<User> findByToken(String token) {return this.userRepository.findByToken(token);}

    public boolean hasUsername(String username) {return this.userRepository.existsByUsername(username);}

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreationDate(LocalDateTime.now());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User updateUser(User user, User changedUser) {
        if (changedUser.getUsername() != null) {
            user.setUsername(changedUser.getUsername());
        }

        if (changedUser.getBirthdayDate() != null ) {
            user.setBirthdayDate(changedUser.getBirthdayDate());
        }

        if (changedUser.getName() != null) {
            user.setName(changedUser.getName());
        }
        userRepository.save(user);
        log.debug("Updated information for User: {}", user);
        return user;
    }

    public User authenticateUser(User user) {
        user.setStatus(UserStatus.ONLINE);
        return user;
    }

    public User logoutUser(User user) {
        user.setStatus(UserStatus.OFFLINE);
        return user;
    }
}
