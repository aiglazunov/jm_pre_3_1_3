package web.service;

import org.springframework.stereotype.Service;
import web.model.Role;
import web.repository.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public List<Role> allRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findRoleByName(String role) {
        return roleRepository.getRoleByRole(role);
    }
}
