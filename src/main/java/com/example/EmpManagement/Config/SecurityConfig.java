package com.example.EmpManagement.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Let anyone access the public endpoint
                        // I want to change it also.. ONLY ADMIN CAN HAVE PERMIT FOR DEP AND EMP CREATION...
                        .requestMatchers("/api/v1/departments").permitAll()

                        // Any other request inside the app MUST be authenticated
                        .anyRequest().authenticated()
                )
                // Keep the default form login page active for testing
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
