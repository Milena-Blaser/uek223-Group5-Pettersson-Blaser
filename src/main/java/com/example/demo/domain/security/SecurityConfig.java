package com.example.demo.domain.security;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
 @EnableWebSecurity @RequiredArgsConstructor @EnableGlobalMethodSecurity(prePostEnabled = true)

 public class SecurityConfig extends WebSecurityConfigurerAdapter {

     private final UserDetailsService userDetailsService;
     private final PasswordEncoder passwordEncoder;


     @Autowired
     public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

                 auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
     }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/list/**").hasAnyRole("ADMIN", "USER")
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/").hasRole("ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers("/list/add").hasAuthority("CREATE_LIST_ENTRY")
                .and()
                .authorizeRequests()
                .antMatchers("/list/get/**").hasAuthority("READ_LIST_ENTRY")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin();
    }
 }
