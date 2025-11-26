package com.example.food_delivery_backend.config;

import com.example.food_delivery_backend.security.CustomUserDetailsService;
import com.example.food_delivery_backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ⭐ keep CORS config
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Auth endpoints
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/users/by-name/**").hasRole("CUSTOMER")

                        // Restaurants endpoints
                        .requestMatchers(HttpMethod.POST, "/api/restaurants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/restaurants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/restaurants/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/restaurants/**").hasAnyRole("ADMIN", "CUSTOMER")

                        // MenuItems endpoints
                        .requestMatchers(HttpMethod.POST, "/api/menu-items/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/menu-items/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/menu-items/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/menu-items").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/menu-items/**").hasAnyRole("ADMIN", "CUSTOMER")

                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("CUSTOMER","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAnyRole("CUSTOMER","ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAnyRole("CUSTOMER","ADMIN")  // exact path
                        .requestMatchers(HttpMethod.GET, "/api/orders").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("CUSTOMER","ADMIN")



                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
