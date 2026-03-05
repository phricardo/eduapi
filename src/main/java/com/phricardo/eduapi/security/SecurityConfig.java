package com.phricardo.eduapi.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http.cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
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
        .exceptionHandling(
            exceptions ->
                exceptions
                    .authenticationEntryPoint(
                        (request, response, authException) -> {
                          response.setStatus(401);
                          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                          response
                              .getWriter()
                              .write(
                                  "{\"status\":401,\"error\":\"Unauthorized\","
                                      + "\"message\":\"Usuario nao autenticado. "
                                      + "Faca login em /oauth2/authorization/google\"}");
                        })
                    .accessDeniedHandler(
                        (request, response, accessDeniedException) -> {
                          response.setStatus(403);
                          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                          response
                              .getWriter()
                              .write(
                                  "{\"status\":403,\"error\":\"Forbidden\","
                                      + "\"message\":\"Usuario autenticado sem permissao para este recurso\"}");
                        }))
        .oauth2Login(
            oauth2 ->
                oauth2
                    .defaultSuccessUrl("/user/me", true)
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)));

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(List.of("*"));
    configuration.setAllowedMethods(List.of("*"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setExposedHeaders(List.of("*"));
    configuration.setAllowCredentials(false);

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
