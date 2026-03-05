package com.phricardo.eduapi.controller;

import com.phricardo.eduapi.controller.doc.CourseControllerDoc;
import com.phricardo.eduapi.dto.request.CourseRequestDto;
import com.phricardo.eduapi.dto.response.CourseResponseDto;
import com.phricardo.eduapi.mapper.CourseMapper;
import com.phricardo.eduapi.service.AuthenticatedUserService;
import com.phricardo.eduapi.service.CourseService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CourseController implements CourseControllerDoc {

  private final CourseService courseService;
  private final CourseMapper courseMapper;
  private final AuthenticatedUserService authenticatedUserService;

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('INSTRUTOR')")
  public CourseResponseDto create(
      @Valid @RequestBody final CourseRequestDto dto,
      @AuthenticationPrincipal final OAuth2User principal) {
    return courseMapper.toResponse(
        courseService.create(dto, authenticatedUserService.fromPrincipal(principal)));
  }

  @Override
  @GetMapping
  public List<CourseResponseDto> list() {
    return courseService.findAll().stream().map(courseMapper::toResponse).toList();
  }

  @Override
  @GetMapping("/{id}")
  public CourseResponseDto findById(@PathVariable final Long id) {
    return courseMapper.toResponse(courseService.findById(id));
  }

  @Override
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('INSTRUTOR')")
  public CourseResponseDto update(
      @PathVariable final Long id,
      @Valid @RequestBody final CourseRequestDto dto,
      @AuthenticationPrincipal final OAuth2User principal) {
    return courseMapper.toResponse(
        courseService.update(id, dto, authenticatedUserService.fromPrincipal(principal)));
  }

  @Override
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('INSTRUTOR')")
  public void delete(
      @PathVariable final Long id, @AuthenticationPrincipal final OAuth2User principal) {
    courseService.delete(id, authenticatedUserService.fromPrincipal(principal));
  }
}
