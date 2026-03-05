package com.phricardo.eduapi.controller.doc;

import com.phricardo.eduapi.dto.request.CourseRequestDto;
import com.phricardo.eduapi.dto.response.CourseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Tag(name = "Cursos", description = "Gerenciamento de cursos da plataforma")
public interface CourseControllerDoc {

  @Operation(summary = "Criar curso", description = "Cria um novo curso. Apenas usuarios INSTRUTOR")
  CourseResponseDto create(CourseRequestDto dto, @AuthenticationPrincipal OAuth2User principal);

  @Operation(summary = "Listar cursos", description = "Retorna todos os cursos disponiveis")
  List<CourseResponseDto> list();

  @Operation(
      summary = "Buscar curso por ID",
      description = "Retorna os dados de um curso especifico")
  CourseResponseDto findById(Long id);

  @Operation(
      summary = "Atualizar curso",
      description = "Atualiza um curso existente. Apenas o instrutor dono")
  CourseResponseDto update(
      Long id, CourseRequestDto dto, @AuthenticationPrincipal OAuth2User principal);

  @Operation(
      summary = "Excluir curso",
      description = "Remove um curso existente. Apenas o instrutor dono")
  void delete(Long id, @AuthenticationPrincipal OAuth2User principal);
}
