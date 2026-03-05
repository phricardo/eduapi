package com.phricardo.eduapi.repository;

import com.phricardo.eduapi.entity.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
  Optional<AppUser> findByEmail(String email);
}
