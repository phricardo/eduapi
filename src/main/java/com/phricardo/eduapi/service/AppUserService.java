package com.phricardo.eduapi.service;

import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.entity.enums.TipoUsuario;
import com.phricardo.eduapi.exception.ResourceNotFoundException;
import com.phricardo.eduapi.repository.AppUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppUserService {

  private final AppUserRepository appUserRepository;

  @Transactional(readOnly = true)
  public List<AppUser> findAll() {
    return appUserRepository.findAll();
  }

  @Transactional(readOnly = true)
  public AppUser findById(final Long id) {
    return appUserRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
  }

  @Transactional(readOnly = true)
  public AppUser findByEmail(final String email) {
    return appUserRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new ResourceNotFoundException("Usuario não encontrado para o email autenticado"));
  }

  @Transactional
  public AppUser upsertGoogleUser(final String nome, final String email) {
    return appUserRepository
        .findByEmail(email)
        .map(
            existing -> {
              existing.setNome(nome);
              return appUserRepository.save(existing);
            })
        .orElseGet(
            () -> {
              AppUser novo = new AppUser();
              novo.setNome(nome);
              novo.setEmail(email);
              novo.setTipo(TipoUsuario.ALUNO);
              return appUserRepository.save(novo);
            });
  }
}
