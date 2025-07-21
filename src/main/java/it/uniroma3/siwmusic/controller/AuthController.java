package it.uniroma3.siwmusic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siwmusic.model.User;
import it.uniroma3.siwmusic.model.enums.Role;
import it.uniroma3.siwmusic.service.UserService;
import it.uniroma3.siwmusic.utils.BaseController;

@Controller
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        User existingUser = userService.getUser(user.getUsername());
        if(existingUser != null) {
            model.addAttribute("error", "Email already registered");
            return "auth/register";
        }
        user.setRole(Role.ROLE_USER);
        userService.saveUser(user, true);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        this.userLogout();
        return "redirect:/";
    }

}
