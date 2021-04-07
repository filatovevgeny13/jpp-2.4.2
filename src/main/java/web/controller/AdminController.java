package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "")
    public String printAllUsers(Model model) {
        model.addAttribute(userService.getAllUsers());
        User currentUser = userService.findByEmail
                (SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("currentUser", currentUser);
        return "admin";
    }

    @GetMapping(value = "/addUser")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "userInfo";
    }

    @GetMapping(value = "/saveUser")
    public String saveUser(@ModelAttribute("user") User user, @ModelAttribute("Active") String isActive,
                           @ModelAttribute("isUser") String isUser, @ModelAttribute("isAdmin") String isAdmin) {
        if(isActive.equals("true")){
            user.setActive(true);
        }
        if(isAdmin.equals("true")){
            user.addRoleToUser(new Role("ROLE_ADMIN"));
        }
        if(isUser.equals("true")){
            user.addRoleToUser(new Role("ROLE_USER"));
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("user-delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/user-update/{id}")
    public String updateUserForm(@PathVariable("id") int id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "/user-update";
    }

    @PostMapping("/user-update")
    public String updateUser(User user) {
        userService.update(user);
        return "redirect:/admin";
    }
}
