package com.phricardo.eduapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/",
                        "/login/**",
                        "/oauth2/**",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(
            oauth2 ->
                oauth2
                    .defaultSuccessUrl("/user/me", true)
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)));

    return http.build();
  }
}
