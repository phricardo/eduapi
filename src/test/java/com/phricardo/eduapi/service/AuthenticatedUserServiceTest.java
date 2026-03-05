package com.phricardo.eduapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.exception.BusinessException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@ExtendWith(MockitoExtension.class)
class AuthenticatedUserServiceTest {

  @Mock private AppUserService appUserService;

  @InjectMocks private AuthenticatedUserService authenticatedUserService;

  @Test
  void fromPrincipalShouldThrowWhenPrincipalNull() {
    assertThrows(BusinessException.class, () -> authenticatedUserService.fromPrincipal(null));
  }

  @Test
  void fromPrincipalShouldThrowWhenEmailMissing() {
    final OAuth2User principal =
        new DefaultOAuth2User(
            List.of(new SimpleGrantedAuthority("ROLE_ALUNO")), Map.of("name", "No Email"), "name");

    assertThrows(BusinessException.class, () -> authenticatedUserService.fromPrincipal(principal));
  }

  @Test
  void fromPrincipalShouldUpsertUsingNameAndEmail() {
    final OAuth2User principal =
        new DefaultOAuth2User(
            List.of(new SimpleGrantedAuthority("ROLE_ALUNO")),
            Map.of("name", "Nome", "email", "email@test.com"),
            "email");

    final AppUser user = new AppUser();
    when(appUserService.upsertGoogleUser("Nome", "email@test.com")).thenReturn(user);

    final AppUser result = authenticatedUserService.fromPrincipal(principal);

    assertSame(user, result);
    verify(appUserService).upsertGoogleUser("Nome", "email@test.com");
  }
}
