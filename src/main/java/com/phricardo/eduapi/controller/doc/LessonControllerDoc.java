package com.phricardo.eduapi.controller.doc;

import com.phricardo.eduapi.dto.request.LessonRequestDto;
import com.phricardo.eduapi.dto.response.LessonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Tag(name = "Aulas", description = "Gerenciamento de aulas dos cursos")
public interface LessonControllerDoc {

  @Operation(
      summary = "Criar aula",
      description = "Cria uma aula para um curso. Apenas INSTRUTOR dono do curso")
  LessonResponseDto create(
      Long courseId, LessonRequestDto dto, @AuthenticationPrincipal OAuth2User principal);

  @Operation(
      summary = "Listar aulas do curso",
      description = "Retorna as aulas de um curso especifico")
  List<LessonResponseDto> listByCourse(Long courseId);

  @Operation(
      summary = "Atualizar aula",
      description = "Atualiza uma aula existente. Apenas INSTRUTOR dono")
  LessonResponseDto update(
      Long lessonId, LessonRequestDto dto, @AuthenticationPrincipal OAuth2User principal);

  @Operation(
      summary = "Excluir aula",
      description = "Remove uma aula existente. Apenas INSTRUTOR dono")
  void delete(Long lessonId, @AuthenticationPrincipal OAuth2User principal);
}
