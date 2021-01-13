package web.service;

import web.model.User;

import java.util.List;

public interface UserService {

    List<User> allUsers(); //listUser

    void add(User user);

    void update(User user);

    void delete(User user);

    void deleteById(long id);

    User getById(long id);

    User getByName(String name);
}
