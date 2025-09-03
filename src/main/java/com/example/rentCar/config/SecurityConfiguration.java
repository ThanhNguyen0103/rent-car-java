package com.example.rentCar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import com.example.rentCar.service.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration {

        @Bean
        public PasswordEncoder encoder() {
                return new BCryptPasswordEncoder();
        }

        // @Bean
        // public CustomUserDetailsService customUserDetailsService(UserService
        // userService) {
        // return new CustomUserDetailsService(userService);
        // }

        @Bean
        public DaoAuthenticationProvider authProvider(
                        PasswordEncoder passwordEncoder,
                        CustomUserDetailsService customUserDetailsService) {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(customUserDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder);
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder,
                        CustomUserDetailsService customUserDetailsService) {
                DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
                authenticationProvider.setUserDetailsService(customUserDetailsService);
                authenticationProvider.setPasswordEncoder(passwordEncoder);

                ProviderManager providerManager = new ProviderManager(authenticationProvider);
                providerManager.setEraseCredentialsAfterAuthentication(false);

                return providerManager;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
                String[] whiteList = {
                                "/", "/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/register",
                                "/storage/**"
                };
                http
                                .csrf(csrf -> csrf.disable())

                                // ------------///
                                .authorizeHttpRequests(req -> req.requestMatchers(whiteList).permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/cars/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/brands/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/car-models/**").permitAll()
                                                // .requestMatchers("/api/v1/users/**").hasAuthority("ADMIN")
                                                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                                                .anyRequest()
                                                .authenticated())

                                // -------------- //
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                                // --------//
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                                // --------//
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                return http.build();
        }

}
