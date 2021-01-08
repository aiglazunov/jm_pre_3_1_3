package web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    //список всех пользователей с ссылками
    @GetMapping("/admin")
    public String index(ModelMap model, Principal principal) {
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("user", userService.getByName(principal.getName()));
        model.addAttribute("allRoles", roleService.allRoles());
        //System.out.println(userService.allUsers());
        //Получим всех людей из DAO и передадим на отображение в представление
        return "admin/index";
    }

    // сохранение нового пользователя
    @PostMapping("/admin/new")
    public String create(@ModelAttribute("user") User user,
                         @RequestParam(value = "allRoles", required = false) String[] roles) {

        //System.out.println(user);
        Set<Role> roleSet = new HashSet<>();
        if ( roles != null) {
            for (String role : roles) {
                //System.out.println(role);
                roleSet.add(roleService.findRoleByName(role));
            }

        }
        else {
            roleSet.add(roleService.findRoleByName("ROLE_USER"));
        }
        user.setRoles(roleSet);
        userService.add(user);

        return "redirect:/admin";
    }

    // сохранение редактирования пользователя
    @PostMapping("/admin/edit")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam(value = "allRoles") String[] roles) {
        //System.out.println(user);
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            //System.out.println(role);
            roleSet.add(roleService.findRoleByName(role));
        }


        user.setRoles(roleSet);
        userService.update(user);
        return "redirect:/admin";
    }

    // удаление пользователя
    @PostMapping("/admin/delete")
    public String delete2(@ModelAttribute("user") User user) {

        //model.addAttribute("allRoles",  new ArrayList<>(userService.show(id).getRoles()));
        userService.delete(user);
        return "redirect:/admin";
    }

    // страница с информацией о пользователе
    @GetMapping("/user")
    public String userPage(Principal principal, ModelMap modelMap) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        StringBuilder sb = new StringBuilder();
        for (String role : roles) {
            sb.append(role).append(" ");
        }

        modelMap.addAttribute("user", userService.getByName(principal.getName()));
        modelMap.addAttribute("username", principal.getName());
        modelMap.addAttribute("roleSet", new String(sb));

        return "user";
    }
}
