package com.phricardo.eduapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.entity.Course;
import com.phricardo.eduapi.entity.Enrollment;
import com.phricardo.eduapi.entity.enums.TipoUsuario;
import com.phricardo.eduapi.exception.BusinessException;
import com.phricardo.eduapi.repository.EnrollmentRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

  @Mock private EnrollmentRepository enrollmentRepository;
  @Mock private CourseService courseService;

  @InjectMocks private EnrollmentService enrollmentService;

  @Test
  void enrollShouldThrowWhenUserIsNotAluno() {
    final AppUser instrutor = new AppUser();
    instrutor.setTipo(TipoUsuario.INSTRUTOR);

    assertThrows(BusinessException.class, () -> enrollmentService.enroll(1L, instrutor));
  }

  @Test
  void enrollShouldThrowWhenAlreadyEnrolled() {
    final AppUser aluno = new AppUser();
    aluno.setId(1L);
    aluno.setTipo(TipoUsuario.ALUNO);

    when(courseService.findById(1L)).thenReturn(new Course());
    when(enrollmentRepository.existsByUsuarioIdAndCursoId(1L, 1L)).thenReturn(true);

    assertThrows(BusinessException.class, () -> enrollmentService.enroll(1L, aluno));
  }

  @Test
  void enrollShouldSaveWhenValid() {
    final AppUser aluno = new AppUser();
    aluno.setId(1L);
    aluno.setTipo(TipoUsuario.ALUNO);

    final Course curso = new Course();
    curso.setId(9L);

    when(courseService.findById(9L)).thenReturn(curso);
    when(enrollmentRepository.existsByUsuarioIdAndCursoId(1L, 9L)).thenReturn(false);
    when(enrollmentRepository.save(any(Enrollment.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    final Enrollment enrollment = enrollmentService.enroll(9L, aluno);

    assertEquals(aluno, enrollment.getUsuario());
    assertEquals(curso, enrollment.getCurso());
    assertNotNull(enrollment.getDataMatricula());
  }

  @Test
  void listByUserIdShouldDelegate() {
    when(enrollmentRepository.findByUsuarioId(7L)).thenReturn(List.of(new Enrollment()));

    final List<Enrollment> result = enrollmentService.listByUserId(7L);

    assertEquals(1, result.size());
    verify(enrollmentRepository).findByUsuarioId(7L);
  }
}
