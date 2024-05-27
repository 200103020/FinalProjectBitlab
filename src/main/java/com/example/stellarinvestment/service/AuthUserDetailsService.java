package com.example.stellarinvestment.service;

import com.example.stellarinvestment.model.AuthUserDetails;
import com.example.stellarinvestment.model.User;
import com.example.stellarinvestment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("No user found with the email " + email);

        return new AuthUserDetails(user);
    }
}
