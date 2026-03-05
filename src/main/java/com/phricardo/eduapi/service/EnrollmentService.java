package com.phricardo.eduapi.service;

import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.entity.Course;
import com.phricardo.eduapi.entity.Enrollment;
import com.phricardo.eduapi.entity.enums.TipoUsuario;
import com.phricardo.eduapi.exception.BusinessException;
import com.phricardo.eduapi.repository.EnrollmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

  private final EnrollmentRepository enrollmentRepository;
  private final CourseService courseService;

  @Transactional
  public Enrollment enroll(final Long cursoId, final AppUser aluno) {
    if (aluno.getTipo() != TipoUsuario.ALUNO) {
      throw new BusinessException("Apenas alunos podem se matricular");
    }

    Course curso = courseService.findById(cursoId);

    if (enrollmentRepository.existsByUsuarioIdAndCursoId(aluno.getId(), cursoId)) {
      throw new BusinessException("Aluno já matriculado neste curso");
    }

    Enrollment enrollment = new Enrollment();
    enrollment.setUsuario(aluno);
    enrollment.setCurso(curso);
    enrollment.setDataMatricula(LocalDateTime.now());
    return enrollmentRepository.save(enrollment);
  }

  @Transactional(readOnly = true)
  public List<Enrollment> listByUserId(final Long userId) {
    return enrollmentRepository.findByUsuarioId(userId);
  }
}
