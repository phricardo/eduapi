package com.phricardo.eduapi.controller.doc;

import com.phricardo.eduapi.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "Usuarios", description = "Consulta de usuarios cadastrados")
public interface UserControllerDoc {

  @Operation(summary = "Listar usuarios", description = "Retorna todos os usuarios cadastrados")
  List<UserResponseDto> list();

  @Operation(
      summary = "Buscar usuario por ID",
      description = "Retorna os dados de um usuario pelo ID")
  UserResponseDto findById(Long id);
}
