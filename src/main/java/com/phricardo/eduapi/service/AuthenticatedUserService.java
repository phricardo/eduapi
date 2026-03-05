package com.phricardo.eduapi.service;

import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

  private final AppUserService appUserService;

  public AppUser fromPrincipal(final OAuth2User principal) {
    if (principal == null) {
      throw new BusinessException("Principal OAuth2 ausente");
    }

    final String email = principal.getAttribute("email");
    final String nome = principal.getAttribute("name");

    if (email == null || email.isBlank()) {
      throw new BusinessException("Email não encontrado no principal autenticado");
    }

    // Garante criação no primeiro acesso e autocorreção se o usuário não existir no banco.
    return appUserService.upsertGoogleUser(nome != null ? nome : email, email);
  }
}
