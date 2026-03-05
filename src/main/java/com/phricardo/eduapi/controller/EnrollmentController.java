package com.phricardo.eduapi.controller;

import com.phricardo.eduapi.controller.doc.EnrollmentControllerDoc;
import com.phricardo.eduapi.dto.response.EnrollmentResponseDto;
import com.phricardo.eduapi.mapper.EnrollmentMapper;
import com.phricardo.eduapi.service.AuthenticatedUserService;
import com.phricardo.eduapi.service.EnrollmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EnrollmentController implements EnrollmentControllerDoc {

  private final EnrollmentService enrollmentService;
  private final EnrollmentMapper enrollmentMapper;
  private final AuthenticatedUserService authenticatedUserService;

  @Override
  @PostMapping("/matriculas/{cursoId}")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('ALUNO')")
  public EnrollmentResponseDto enroll(
      @PathVariable final Long cursoId, @AuthenticationPrincipal final OAuth2User principal) {
    return enrollmentMapper.toResponse(
        enrollmentService.enroll(cursoId, authenticatedUserService.fromPrincipal(principal)));
  }

  @Override
  @GetMapping("/usuarios/{id}/matriculas")
  public List<EnrollmentResponseDto> listByUser(@PathVariable("id") final Long userId) {
    return enrollmentService.listByUserId(userId).stream()
        .map(enrollmentMapper::toResponse)
        .toList();
  }
}
