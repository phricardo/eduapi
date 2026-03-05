package com.phricardo.eduapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
  private String name = "EduAPI";
  private String description = "API REST";
  private String version = "1.0.0";
}
