package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.Role;
import web.model.User;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getRoleByRole(String name);
}
