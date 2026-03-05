package com.phricardo.eduapi.repository;

import com.phricardo.eduapi.entity.Enrollment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
  boolean existsByUsuarioIdAndCursoId(Long usuarioId, Long cursoId);

  List<Enrollment> findByUsuarioId(Long usuarioId);
}
