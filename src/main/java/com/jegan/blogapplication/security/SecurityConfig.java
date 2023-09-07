package com.jegan.blogapplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcDaoImpl jdbcUserDetailsService = new JdbcDaoImpl();
        jdbcUserDetailsService.setDataSource(dataSource);
        jdbcUserDetailsService.setUsersByUsernameQuery("SELECT name, password,active as enabled FROM users WHERE name = ?");
        jdbcUserDetailsService.setAuthoritiesByUsernameQuery("SELECT name, role FROM users WHERE name = ?");
        return jdbcUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/api/posts").permitAll()
                                .requestMatchers("/api/posts/delete").hasRole("AUTHOR")
                                .requestMatchers("/api/posts/update").hasRole("AUTHOR")
                                .anyRequest().permitAll()
                )
                .logout(logout -> logout.permitAll())
                .csrf().disable();
                http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
