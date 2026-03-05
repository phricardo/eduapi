package com.phricardo.eduapi.controller.doc;

import com.phricardo.eduapi.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Tag(name = "Autenticacao", description = "Operacoes relacionadas ao usuario autenticado")
public interface AuthControllerDoc {

  @Operation(
      summary = "Obter usuario autenticado",
      description = "Retorna os dados do usuario atualmente autenticado via OAuth2 Google")
  UserResponseDto me(@AuthenticationPrincipal OAuth2User principal);
}
