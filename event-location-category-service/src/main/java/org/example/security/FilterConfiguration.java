package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.example.utils.Role.ADMIN;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@RequiredArgsConstructor
@Configuration
public class FilterConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(req ->
                        req.requestMatchers( "/api/v1/auth", "/api/v1/register").permitAll()
                                .requestMatchers(POST, "/api/v1/locations/**").hasRole(ADMIN.name())
                                .requestMatchers(PUT, "/api/v1/locations/**").hasRole(ADMIN.name())
                                .requestMatchers(DELETE, "/api/v1/locations/**").hasRole(ADMIN.name())
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/api/v1/logout")
                            .addLogoutHandler(logoutHandler)
                            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                ).build();
    }
}
