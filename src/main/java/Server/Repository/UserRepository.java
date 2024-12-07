package Server.Repository;

import Server.Entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
	Optional<User> findByUsername(String username);
}