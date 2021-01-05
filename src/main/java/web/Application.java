package web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import web.model.User;
import web.repository.UserRepository;
import web.service.UserService;
import web.service.UserServiceImpl;

import java.util.List;

@SpringBootApplication
//@Configuration + @EnableAutoConfiguration + @ComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);



    }

}
