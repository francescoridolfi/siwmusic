package it.uniroma3.siwmusic.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import groovy.lang.Lazy;
import it.uniroma3.siwmusic.model.User;
import it.uniroma3.siwmusic.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User getUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User saveUser(User user, boolean passwordUpdated) {
        if(passwordUpdated)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    @Transactional
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }   
}
