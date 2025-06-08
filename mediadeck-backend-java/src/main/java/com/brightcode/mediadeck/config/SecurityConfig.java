package com.brightcode.mediadeck.config;

import com.brightcode.mediadeck.service.AppUserDetailsService;
import com.brightcode.mediadeck.service.AuthenticationSuccessHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                        .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/css/*").permitAll()
                            .requestMatchers("/images/*").permitAll()
                            .requestMatchers("/").permitAll()
                            .requestMatchers("/signin").permitAll()
                            .requestMatchers("/signup").permitAll()
                            .requestMatchers("/reset-password").permitAll()
                            .requestMatchers("/reset-password/verify").permitAll()
                            .requestMatchers("/profile/*").permitAll()
                            .requestMatchers("/signup-verify").permitAll()
                            .requestMatchers("/movies").permitAll()
                            .requestMatchers("/search").permitAll()
                            .requestMatchers("/user_search").permitAll()
                            .requestMatchers("/searched_profile").permitAll()
                            .requestMatchers("/search/*").permitAll()
                            .requestMatchers("/search/results").permitAll()
                            .requestMatchers("/search/movie-view/*").permitAll()
                            .requestMatchers("/movie-view/*").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer
                            .loginPage("/signin")
                            .successHandler(new AuthenticationSuccessHandler())
                            .permitAll();
                })
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // manual mapping if needed
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    public class DebugLoggingFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            System.out.println("Request URI: " + request.getRequestURI());
            System.out.println("Session ID: " + request.getRequestedSessionId());
            filterChain.doFilter(request, response);
        }
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


}
