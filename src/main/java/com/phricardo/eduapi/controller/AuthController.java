package com.phricardo.eduapi.controller;

import com.phricardo.eduapi.controller.doc.AuthControllerDoc;
import com.phricardo.eduapi.dto.response.UserResponseDto;
import com.phricardo.eduapi.mapper.UserMapper;
import com.phricardo.eduapi.service.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDoc {

  private final AuthenticatedUserService authenticatedUserService;
  private final UserMapper userMapper;

  @Override
  @GetMapping("/me")
  public UserResponseDto me(@AuthenticationPrincipal final OAuth2User principal) {
    return userMapper.toResponse(authenticatedUserService.fromPrincipal(principal));
  }
}
