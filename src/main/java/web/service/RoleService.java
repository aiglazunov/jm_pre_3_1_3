package web.service;

import web.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> allRoles(); //listRoles

    Role findRoleByName (String role);

}
