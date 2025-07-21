package it.uniroma3.siwmusic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.uniroma3.siwmusic.model.User;
import it.uniroma3.siwmusic.repository.UserRepository;

@Service
public class SiwUserDetailsService implements UserDetailsService{

    @Autowired
    public UserRepository userRepository;

    @Override
    public SiwUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userRepository.findByUsername(username).orElse(null);

        if(user == null)
            throw new UsernameNotFoundException("User not found with email: " + username);
        return new SiwUserDetails(user);
     }
}
