package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.*;
import web.service.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping(value = "api/users")
public class RestAdminController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public RestAdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/roles")
    public ResponseEntity<List<Role>> allRoles() {
        return new ResponseEntity<>(roleService.allRoles(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok()
                .body(userService.allUsers());
    }


    @GetMapping("/getCurrent")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        return ResponseEntity.ok()
                .body(userService.getByName(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(userService.getById(id));
    }


    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        userService.add(user);
        return ResponseEntity.ok()
                .body(user.getUsername());
    }

    @PutMapping()
    public ResponseEntity<User> editUser(@RequestBody User user) {
        userService.update(user);
        return ResponseEntity.ok()
                .body(userService.getByName(user.getUsername()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok()
                .body("success");
    }


}
