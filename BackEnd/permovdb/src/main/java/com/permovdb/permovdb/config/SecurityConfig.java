package com.permovdb.permovdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.permovdb.permovdb.security.filter.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
