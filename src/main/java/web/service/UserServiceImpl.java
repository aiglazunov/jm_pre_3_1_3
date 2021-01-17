package web.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.model.User;
import web.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    public void add(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(User user) {

        User user_current = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user_old = userRepository.findById(user.getId()).get();

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        boolean isCurrentUser = user_current.getId().equals(user.getId());
        boolean isUsersEquals = user.equals(user_old);

        if (isCurrentUser) {
            if (isCurrentUser && user_current.equals(user)) {
                if (user.getPassword() == null || "".equals(user.getPassword())) {
                    user.setPassword(user_current.getPassword());
                } else {
                    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                }
                userRepository.saveAndFlush(user);

            }

        } else {
            if (!(bCryptPasswordEncoder.matches(user.getPassword(), userRepository.getUserPassword(user.getId())) && isUsersEquals)) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                userRepository.saveAndFlush(user);
            }
        }


    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getById(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User getByName(String name) {
        return userRepository.getUserByUsername(name);
    }


}
