package com.phricardo.eduapi.service;

import com.phricardo.eduapi.dto.request.LessonRequestDto;
import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.entity.Course;
import com.phricardo.eduapi.entity.Lesson;
import com.phricardo.eduapi.entity.enums.TipoUsuario;
import com.phricardo.eduapi.exception.BusinessException;
import com.phricardo.eduapi.exception.ResourceNotFoundException;
import com.phricardo.eduapi.mapper.LessonMapper;
import com.phricardo.eduapi.repository.LessonRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LessonService {

  private final LessonRepository lessonRepository;
  private final CourseService courseService;
  private final LessonMapper lessonMapper;

  @Transactional
  public Lesson create(final Long courseId, final LessonRequestDto dto, final AppUser instrutor) {
    validateInstrutor(instrutor);
    Course course = courseService.findById(courseId);
    courseService.validateOwner(course, instrutor);

    Lesson lesson = lessonMapper.toEntity(dto);
    lesson.setCurso(course);
    return lessonRepository.save(lesson);
  }

  @Transactional(readOnly = true)
  public List<Lesson> listByCourse(final Long courseId) {
    courseService.findById(courseId);
    return lessonRepository.findByCursoId(courseId);
  }

  @Transactional
  public Lesson update(final Long lessonId, final LessonRequestDto dto, final AppUser instrutor) {
    validateInstrutor(instrutor);
    Lesson lesson =
        lessonRepository
            .findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada"));
    courseService.validateOwner(lesson.getCurso(), instrutor);

    lesson.setTitulo(dto.titulo());
    lesson.setDuracaoMinutos(dto.duracaoMinutos());
    return lessonRepository.save(lesson);
  }

  @Transactional
  public void delete(final Long lessonId, final AppUser instrutor) {
    validateInstrutor(instrutor);
    Lesson lesson =
        lessonRepository
            .findById(lessonId)
            .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada"));
    courseService.validateOwner(lesson.getCurso(), instrutor);
    lessonRepository.delete(lesson);
  }

  private void validateInstrutor(final AppUser usuario) {
    if (usuario.getTipo() != TipoUsuario.INSTRUTOR) {
      throw new BusinessException("Apenas instrutores podem executar esta operação");
    }
  }
}
