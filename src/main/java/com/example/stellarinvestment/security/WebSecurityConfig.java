package com.example.stellarinvestment.security;

import com.example.stellarinvestment.service.AuthUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/main/forbidden");

        http.authorizeRequests()
                .antMatchers("/main/**").hasAnyAuthority("Admin", "User", "Investor")
                .antMatchers("/project/**").hasAnyAuthority("Admin", "User")
                .antMatchers("/admin/**").hasAuthority("Admin")
                .antMatchers("/investor/**").hasAuthority("Investor")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/auth/login")
                .defaultSuccessUrl("/main/")
                .usernameParameter("email")
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .rememberMe()
                .key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
                .tokenValiditySeconds(7 * 24 * 60 * 60)
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }

    @Bean
    public AuthUserDetailsService userDetailsService() {
        return new AuthUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}