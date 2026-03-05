package com.phricardo.eduapi.controller;

import com.phricardo.eduapi.controller.doc.LessonControllerDoc;
import com.phricardo.eduapi.dto.request.LessonRequestDto;
import com.phricardo.eduapi.dto.response.LessonResponseDto;
import com.phricardo.eduapi.mapper.LessonMapper;
import com.phricardo.eduapi.service.AuthenticatedUserService;
import com.phricardo.eduapi.service.LessonService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LessonController implements LessonControllerDoc {

  private final LessonService lessonService;
  private final LessonMapper lessonMapper;
  private final AuthenticatedUserService authenticatedUserService;

  @Override
  @PostMapping("/cursos/{id}/aulas")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('INSTRUTOR')")
  public LessonResponseDto create(
      @PathVariable("id") final Long courseId,
      @Valid @RequestBody final LessonRequestDto dto,
      @AuthenticationPrincipal final OAuth2User principal) {
    return lessonMapper.toResponse(
        lessonService.create(courseId, dto, authenticatedUserService.fromPrincipal(principal)));
  }

  @Override
  @GetMapping("/cursos/{id}/aulas")
  public List<LessonResponseDto> listByCourse(@PathVariable("id") final Long courseId) {
    return lessonService.listByCourse(courseId).stream().map(lessonMapper::toResponse).toList();
  }

  @Override
  @PutMapping("/aulas/{id}")
  @PreAuthorize("hasRole('INSTRUTOR')")
  public LessonResponseDto update(
      @PathVariable("id") final Long lessonId,
      @Valid @RequestBody final LessonRequestDto dto,
      @AuthenticationPrincipal final OAuth2User principal) {
    return lessonMapper.toResponse(
        lessonService.update(lessonId, dto, authenticatedUserService.fromPrincipal(principal)));
  }

  @Override
  @DeleteMapping("/aulas/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('INSTRUTOR')")
  public void delete(
      @PathVariable("id") final Long lessonId,
      @AuthenticationPrincipal final OAuth2User principal) {
    lessonService.delete(lessonId, authenticatedUserService.fromPrincipal(principal));
  }
}
