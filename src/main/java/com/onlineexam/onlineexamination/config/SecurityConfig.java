package com.onlineexam.onlineexamination.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Registration and login are public
                        .requestMatchers(
                                "/api/students/register",
                                "/api/students/login"
                        ).permitAll()

                        // Only ADMIN can add questions
                        .requestMatchers(HttpMethod.POST, "/api/questions/**")
                        .hasRole("ADMIN")

                        // Only ADMIN can update questions
                        .requestMatchers(HttpMethod.PUT, "/api/questions/**")
                        .hasRole("ADMIN")

                        // Only ADMIN can delete questions
                        .requestMatchers(HttpMethod.DELETE, "/api/questions/**")
                        .hasRole("ADMIN")
                                // Only ADMIN can create exams
                                .requestMatchers(HttpMethod.POST, "/api/exams/**")
                                .hasRole("ADMIN")

// Only ADMIN can update exams
                                .requestMatchers(HttpMethod.PUT, "/api/exams/**")
                                .hasRole("ADMIN")

// Only ADMIN can delete exams
                                .requestMatchers(HttpMethod.DELETE, "/api/exams/**")
                                .hasRole("ADMIN")

                        // All remaining APIs require login
                        .anyRequest().authenticated()
                )

                .httpBasic(basic -> basic.realmName("Online Examination System"))

                .formLogin(form -> form.disable());

        return http.build();
    }
}