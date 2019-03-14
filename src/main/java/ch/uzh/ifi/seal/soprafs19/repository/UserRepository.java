package ch.uzh.ifi.seal.soprafs19.repository;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {
	User findByName(String name);
	Optional<User> findByUsername(String username);
	Optional<User> findByToken(String token);
	boolean existsByUsername(String username);
}
