package com.poktest.spring.boot.rest.secure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity   // this for check security in controller
@EnableGlobalMethodSecurity(prePostEnabled = true) // this for check PreAuthorize role in controller
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    // Create 2 users for demo
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.inMemoryAuthentication()
//                .withUser("user").password("{noop}password").roles("USER")
//                .and()
//                .withUser("admin").password("{noop}password").roles("ADMIN","USER");


        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .passwordEncoder(encoder)
                .withUser("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(encoder.encode("password"))
                .roles("ADMIN","USER");
    }

      /*@Bean
    public UserDetailsService userDetailsService() {
        //ok for demo
        User.UserBuilder users = User.withDefaultPasswordEncoder();

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user").password("password").roles("USER").build());
        manager.createUser(users.username("admin").password("password").roles("USER", "ADMIN").build());
        return manager;
    }*/


    // Secure the endpoins with HTTP Basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.httpBasic()
            .and()
            .authorizeRequests()
//            .antMatchers(HttpMethod.GET, "/book/**").hasRole("USER")    // comment because use @PreAuthorize("hasRole('USER')") in controller
            .antMatchers(HttpMethod.POST, "/book").hasRole("ADMIN")
            .antMatchers(HttpMethod.PUT, "/book/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.PATCH, "/book/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.DELETE, "/book/**").hasRole("ADMIN")
//          .antMatchers("/api/**")
//          .authenticated()
//          .antMatchers("/public/**")
//          .permitAll()
            .and()
            .csrf().disable()
            .formLogin().disable();
    }

}
