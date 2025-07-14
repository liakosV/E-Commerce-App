package gr.aueb.cf.e_commerce_app.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/authenticate", "/api/users/save", "/api/users/update/**").permitAll()
                        .requestMatchers("/api/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/roles").permitAll()
                        .requestMatchers("/api/roles/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                        .requestMatchers("/api/categories/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/products/save", "/api/products/remove/**").hasAnyAuthority("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/orders").hasAuthority("ADMIN")
                        .requestMatchers("/api/orders/**").hasAnyAuthority("ADMIN", "SELLER", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/regions").permitAll()
                        .requestMatchers("/api/regions/**").hasAuthority("ADMIN")
                        .requestMatchers("/styles/**", "/js/**", "/img/**").permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
