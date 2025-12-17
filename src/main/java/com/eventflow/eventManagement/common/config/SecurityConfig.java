package com.eventflow.eventManagement.common.config;

import com.eventflow.eventManagement.common.utils.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/users/register", "/health").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/incidents")
                        .hasAnyRole("ADMIN", "OPERATOR")

                        .requestMatchers(HttpMethod.PUT, "/api/incidents/**")
                        .hasAnyRole("ADMIN", "OPERATOR")

                        .requestMatchers(HttpMethod.GET, "/api/incidents/**")
                        .hasAnyRole("ADMIN", "OPERATOR", "VIEWER")

                        .anyRequest().authenticated()
                )

                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

