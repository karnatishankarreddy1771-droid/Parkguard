package com.anurag.parking.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.anurag.parking.filter.JwtAuthenticationFilter;
import com.anurag.parking.repository.UserRepository;
import com.anurag.parking.service.JwtService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {
        return new JwtAuthenticationFilter(jwtService, userRepository);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(
            @Value("${parking.cors.allowed-origins:http://localhost:5173,http://localhost:3000}")
            String allowedOrigins) {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList());

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of(
                "Authorization", "Content-Type"
        ));

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter filter) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .contentTypeOptions(contentTypeOptions -> {})
                        .frameOptions(frameOptions -> frameOptions.deny())
                        .httpStrictTransportSecurity(hsts ->
                                hsts.includeSubDomains(true)
                                        .maxAgeInSeconds(31536000))
                        .referrerPolicy(referrer -> referrer.policy(
                                ReferrerPolicyHeaderWriter.ReferrerPolicy
                                        .STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        .addHeaderWriter(new StaticHeadersWriter(
                                "Permissions-Policy",
                                "camera=(), microphone=(), geolocation=()")))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/health").permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/admin/login",
                                "/auth/login")
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(
                        filter,
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}