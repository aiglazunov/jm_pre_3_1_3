package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByUsername(String username);

    @Query("SELECT u.password FROM User u where u.id = ?1")
    String getUserPassword(long userid);
}
