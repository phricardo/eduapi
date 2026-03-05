package com.phricardo.eduapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.phricardo.eduapi.dto.request.CourseRequestDto;
import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.entity.Course;
import com.phricardo.eduapi.entity.enums.TipoUsuario;
import com.phricardo.eduapi.exception.BusinessException;
import com.phricardo.eduapi.exception.ResourceNotFoundException;
import com.phricardo.eduapi.mapper.CourseMapper;
import com.phricardo.eduapi.repository.CourseRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

  @Mock private CourseRepository courseRepository;
  @Mock private CourseMapper courseMapper;

  @InjectMocks private CourseService courseService;

  @Test
  void createShouldSaveWhenUserIsInstrutor() {
    final AppUser instrutor = new AppUser();
    instrutor.setId(1L);
    instrutor.setTipo(TipoUsuario.INSTRUTOR);

    final Course course = new Course();
    when(courseMapper.toEntity(any(CourseRequestDto.class))).thenReturn(course);
    when(courseRepository.save(any(Course.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    final Course saved =
        courseService.create(new CourseRequestDto("Java", "Curso Java"), instrutor);

    assertSame(instrutor, saved.getInstrutor());
    verify(courseRepository).save(course);
  }

  @Test
  void createShouldThrowWhenUserIsAluno() {
    final AppUser aluno = new AppUser();
    aluno.setTipo(TipoUsuario.ALUNO);

    assertThrows(
        BusinessException.class, () -> courseService.create(new CourseRequestDto("X", "Y"), aluno));
  }

  @Test
  void updateShouldThrowWhenNotOwner() {
    final AppUser instrutor = new AppUser();
    instrutor.setId(1L);
    instrutor.setTipo(TipoUsuario.INSTRUTOR);

    final AppUser owner = new AppUser();
    owner.setId(2L);

    final Course course = new Course();
    course.setInstrutor(owner);

    when(courseRepository.findById(99L)).thenReturn(Optional.of(course));

    assertThrows(
        BusinessException.class,
        () -> courseService.update(99L, new CourseRequestDto("Novo", "Desc"), instrutor));
  }

  @Test
  void deleteShouldDeleteWhenOwner() {
    final AppUser instrutor = new AppUser();
    instrutor.setId(1L);
    instrutor.setTipo(TipoUsuario.INSTRUTOR);

    final Course course = new Course();
    course.setId(9L);
    course.setInstrutor(instrutor);

    when(courseRepository.findById(9L)).thenReturn(Optional.of(course));

    courseService.delete(9L, instrutor);

    verify(courseRepository).delete(course);
  }

  @Test
  void findByIdShouldThrowWhenNotFound() {
    when(courseRepository.findById(123L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> courseService.findById(123L));
  }
}
