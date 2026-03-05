package com.phricardo.eduapi.controller.doc;

import com.phricardo.eduapi.dto.response.EnrollmentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Tag(name = "Matriculas", description = "Operacoes de matricula de alunos em cursos")
public interface EnrollmentControllerDoc {

  @Operation(
      summary = "Realizar matricula",
      description = "Matricula o aluno autenticado em um curso")
  EnrollmentResponseDto enroll(Long cursoId, @AuthenticationPrincipal OAuth2User principal);

  @Operation(
      summary = "Listar matriculas por usuario",
      description = "Retorna as matriculas de um usuario")
  List<EnrollmentResponseDto> listByUser(Long userId);
}
