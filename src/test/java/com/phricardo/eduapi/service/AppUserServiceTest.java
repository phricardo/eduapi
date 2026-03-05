package com.phricardo.eduapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.entity.enums.TipoUsuario;
import com.phricardo.eduapi.exception.ResourceNotFoundException;
import com.phricardo.eduapi.repository.AppUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

  @Mock private AppUserRepository appUserRepository;

  @InjectMocks private AppUserService appUserService;

  @Test
  void findByIdShouldReturnUserWhenExists() {
    final AppUser user = new AppUser();
    user.setId(1L);
    when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));

    final AppUser result = appUserService.findById(1L);

    assertEquals(1L, result.getId());
  }

  @Test
  void findByIdShouldThrowWhenNotFound() {
    when(appUserRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> appUserService.findById(1L));
  }

  @Test
  void upsertGoogleUserShouldUpdateWhenUserExists() {
    final AppUser existing = new AppUser();
    existing.setId(10L);
    existing.setEmail("user@test.com");
    existing.setNome("Nome Antigo");
    when(appUserRepository.findByEmail("user@test.com")).thenReturn(Optional.of(existing));
    when(appUserRepository.save(any(AppUser.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    final AppUser result = appUserService.upsertGoogleUser("Nome Novo", "user@test.com");

    assertEquals("Nome Novo", result.getNome());
    assertEquals("user@test.com", result.getEmail());
    verify(appUserRepository).save(existing);
  }

  @Test
  void upsertGoogleUserShouldCreateAlunoWhenUserDoesNotExist() {
    when(appUserRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
    when(appUserRepository.save(any(AppUser.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    final AppUser result = appUserService.upsertGoogleUser("Novo", "new@test.com");

    assertEquals("Novo", result.getNome());
    assertEquals("new@test.com", result.getEmail());
    assertEquals(TipoUsuario.ALUNO, result.getTipo());
    verify(appUserRepository).save(any(AppUser.class));
  }
}
