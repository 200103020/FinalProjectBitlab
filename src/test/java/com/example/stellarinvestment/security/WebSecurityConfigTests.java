package com.example.stellarinvestment.security;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebSecurityConfigTests {

    @Test
    void passwordEncoder() {
        WebSecurityConfig webSecurityConfig = new WebSecurityConfig();
        PasswordEncoder passwordEncoder = webSecurityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void authenticationProvider() {
        WebSecurityConfig webSecurityConfig = new WebSecurityConfig();
        DaoAuthenticationProvider result = webSecurityConfig.authenticationProvider();
        assertNotNull(result);
        assertTrue(result instanceof DaoAuthenticationProvider);
    }
}
