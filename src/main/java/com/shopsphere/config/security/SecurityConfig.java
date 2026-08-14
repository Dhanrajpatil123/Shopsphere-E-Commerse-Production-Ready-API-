package com.shopsphere.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private CustomUserDetailsService customUserDetailsService;

    private PasswordEncoder passwordEncoder;



    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);

        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return authenticationProvider;
    }




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers( "/api/users/register")
                                .permitAll()



                                .requestMatchers("/api/users")
                                .hasRole("ADMIN")


                                // only admin can delete any user
                                // Unlike update/deactivate, a customer cannot delete even their own account through this endpoint.
                                // but we manage this in service layer
                                .requestMatchers(HttpMethod.DELETE, "/api/users/**")
                                .hasRole("ADMIN")


                                /*
                                .requestMatchers(HttpMethod.PUT, "/api/categories/**")
                                .hasRole("ADMIN")

                                .requestMatchers(HttpMethod.DELETE, "/api/categories/**")
                                .hasRole("ADMIN")
                                 */


                                .anyRequest()
                                .authenticated()
                )
//                .httpBasic(Customizer.withDefaults());
                .httpBasic(httpBasic -> {});
        return http.build();
    }

}
