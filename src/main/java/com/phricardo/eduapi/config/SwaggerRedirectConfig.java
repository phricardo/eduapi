package com.phricardo.eduapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("!container-test")
public class SwaggerRedirectConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(final ViewControllerRegistry registry) {
    registry.addRedirectViewController("/", "/swagger-ui/index.html");
  }
}
