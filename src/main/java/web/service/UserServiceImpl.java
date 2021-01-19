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

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User userContext = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //если текущий пользователь контекста == пользователю которого редактируем
        if (userContext.getId().equals(user.getId())) {
            // и если его пароль не менялся - берем из контекста
            if (user.getPassword() == null || "".equals(user.getPassword())) {
                user.setPassword(userContext.getPassword());
                // иначе кодируем пароль
            } else {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
            //если текущий пользователь контекста != пользователю которого редактируем
        } else {
            // и если его пароль не менялся - берем текущий из базы
            if (user.getPassword() == null || "".equals(user.getPassword())) {
                user.setPassword(userRepository.getUserPassword(user.getId()));
                // иначе кодируем пароль
            } else {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
        }

        userRepository.saveAndFlush(user);


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
