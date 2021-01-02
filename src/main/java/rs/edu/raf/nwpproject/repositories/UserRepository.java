package rs.edu.raf.nwpproject.repositories;

import org.springframework.data.repository.CrudRepository;
import rs.edu.raf.nwpproject.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
