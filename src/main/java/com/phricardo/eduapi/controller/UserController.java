package com.phricardo.eduapi.controller;

import com.phricardo.eduapi.controller.doc.UserControllerDoc;
import com.phricardo.eduapi.dto.response.UserResponseDto;
import com.phricardo.eduapi.mapper.UserMapper;
import com.phricardo.eduapi.service.AppUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController implements UserControllerDoc {

  private final AppUserService appUserService;
  private final UserMapper userMapper;

  @Override
  @GetMapping
  public List<UserResponseDto> list() {
    return appUserService.findAll().stream().map(userMapper::toResponse).toList();
  }

  @Override
  @GetMapping("/{id}")
  public UserResponseDto findById(@PathVariable final Long id) {
    return userMapper.toResponse(appUserService.findById(id));
  }
}
