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

    private  UserService userService;
    private  RoleService roleService;

    @Autowired
    public RestAdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    //list roles
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> allRoles(){
        //return ResponseEntity.ok(userService.allUsers());
        return new ResponseEntity<>(roleService.allRoles(), HttpStatus.OK);
    }

    //list users
    @GetMapping
    public ResponseEntity<List<User>> allUsers(){
        //return ResponseEntity.ok(userService.allUsers());
        return new ResponseEntity<>(userService.allUsers(), HttpStatus.OK);
    }
    // get current user
    @GetMapping("/getCurrent")
    public ResponseEntity<User> getCurrentUser(Principal principal){
        //return ResponseEntity.ok(userService.allUsers());
        return new ResponseEntity<>(userService.getByName(principal.getName()), HttpStatus.OK);
    }

    // get user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        //return ResponseEntity.ok(userService.getById(id));
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }
    // create user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        userService.add(user);
        return  new ResponseEntity<>(userService.getByName(user.getUsername()), HttpStatus.OK);
    }
    // edit user
    @PutMapping()
    public ResponseEntity<User> editUser(@RequestBody User user) {

        userService.update(user);
        return new ResponseEntity<>(userService.getByName(user.getUsername()), HttpStatus.OK);
    }
    // delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        userService.delete(user);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }




}
