package it.uniroma3.siwmusic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siwmusic.model.enums.Role;
import it.uniroma3.siwmusic.model.User;
import it.uniroma3.siwmusic.service.PlaylistService;
import it.uniroma3.siwmusic.service.UserService;
import it.uniroma3.siwmusic.utils.BaseController;

@Controller
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private PlaylistService playlistService;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               Model model,
                               @org.springframework.web.bind.annotation.RequestParam("confirmPassword") String confirmPassword) {

        // Verifica se username è già usato
        User existingUser = userService.getUser(user.getUsername());
        if (existingUser != null) {
            model.addAttribute("error", "Username già registrato.");
            return "auth/register";
        }

        // Controlla che password e conferma coincidano
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Le password non coincidono.");
            return "auth/register";
        }

        
        // Imposta ruolo e salva
        user.setRole(Role.ROLE_USER);
        User savedUser = userService.saveUser(user, true);
        // Crea automaticamente la playlist personale
        playlistService.createPlaylistForUser(savedUser);

        // Redirect al login con messaggio di successo
        return "redirect:/login?success";
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