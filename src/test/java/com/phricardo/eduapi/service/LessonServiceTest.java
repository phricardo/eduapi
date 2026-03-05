package com.phricardo.eduapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.phricardo.eduapi.dto.request.LessonRequestDto;
import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.entity.Course;
import com.phricardo.eduapi.entity.Lesson;
import com.phricardo.eduapi.entity.enums.TipoUsuario;
import com.phricardo.eduapi.exception.BusinessException;
import com.phricardo.eduapi.exception.ResourceNotFoundException;
import com.phricardo.eduapi.mapper.LessonMapper;
import com.phricardo.eduapi.repository.LessonRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

  @Mock private LessonRepository lessonRepository;
  @Mock private CourseService courseService;
  @Mock private LessonMapper lessonMapper;

  @InjectMocks private LessonService lessonService;

  @Test
  void createShouldSaveWhenInstrutorOwner() {
    final AppUser instrutor = new AppUser();
    instrutor.setId(1L);
    instrutor.setTipo(TipoUsuario.INSTRUTOR);

    final Course course = new Course();
    course.setId(10L);
    course.setInstrutor(instrutor);

    final Lesson lesson = new Lesson();

    when(courseService.findById(10L)).thenReturn(course);
    when(lessonMapper.toEntity(any(LessonRequestDto.class))).thenReturn(lesson);
    when(lessonRepository.save(any(Lesson.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    final Lesson saved = lessonService.create(10L, new LessonRequestDto("Aula", 30), instrutor);

    assertEquals(course, saved.getCurso());
    verify(courseService).validateOwner(course, instrutor);
    verify(lessonRepository).save(lesson);
  }

  @Test
  void createShouldThrowWhenUserNotInstrutor() {
    final AppUser aluno = new AppUser();
    aluno.setTipo(TipoUsuario.ALUNO);

    assertThrows(
        BusinessException.class,
        () -> lessonService.create(1L, new LessonRequestDto("A", 20), aluno));
  }

  @Test
  void updateShouldThrowWhenLessonNotFound() {
    final AppUser instrutor = new AppUser();
    instrutor.setTipo(TipoUsuario.INSTRUTOR);
    when(lessonRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> lessonService.update(99L, new LessonRequestDto("A", 20), instrutor));
  }

  @Test
  void deleteShouldDeleteWhenLessonExistsAndOwner() {
    final AppUser instrutor = new AppUser();
    instrutor.setId(1L);
    instrutor.setTipo(TipoUsuario.INSTRUTOR);

    final Course course = new Course();
    final AppUser owner = new AppUser();
    owner.setId(1L);
    course.setInstrutor(owner);

    final Lesson lesson = new Lesson();
    lesson.setCurso(course);

    when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

    lessonService.delete(1L, instrutor);

    verify(courseService).validateOwner(course, instrutor);
    verify(lessonRepository).delete(lesson);
  }
}
