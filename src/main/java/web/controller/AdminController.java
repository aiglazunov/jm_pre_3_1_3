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
    public String index(ModelMap model) {
        model.addAttribute("users", userService.allUsers());
        //System.out.println(userService.allUsers());
        //Получим всех людей из DAO и передадим на отображение в представление
        return "admin/index";
    }
    // создание нового пользователя
    @GetMapping("/admin/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.allRoles());
        return "admin/new";
    }
    // сохранение нового пользователя
    @PostMapping("/admin/new")
    public String create(@ModelAttribute("user") User user,
                         @RequestParam(value = "allRoles") String[] roles) {

        //System.out.println(user);
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles) {
            //System.out.println(role);
            roleSet.add(roleService.findRoleByName(role));
        }
        user.setRoles(roleSet);
        userService.add(user);

        return "redirect:/admin";
    }
    // редактирования пользователя
    @GetMapping("/admin/edit")
    public String edit(Model model, @RequestParam(value = "userid", required = true) long userid) {
        model.addAttribute("user", userService.getById(userid));
        //model.addAttribute("allRoles",  new ArrayList<>(userService.show(id).getRoles()));
        model.addAttribute("allRoles", roleService.allRoles());
        return "admin/edit";
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

    @GetMapping("admin/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        User user = userService.getById(id);
        userService.delete(user);
        return "redirect:/admin";
    }

    //стартовая страница для Админа
    @GetMapping("/admin/admin")
    public String adminPage(Principal principal, ModelMap modelMap) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        StringBuilder sb = new StringBuilder();
        for (String role : roles) {
            sb.append(role).append(" ");
        }


        modelMap.addAttribute("username", principal.getName());
        modelMap.addAttribute("roleSet", new String(sb));

        return "admin/admin";
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


        modelMap.addAttribute("username", principal.getName());
        modelMap.addAttribute("roleSet", new String(sb));

        return "user";
    }
}
