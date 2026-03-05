package com.phricardo.eduapi.service;

import com.phricardo.eduapi.dto.request.CourseRequestDto;
import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.entity.Course;
import com.phricardo.eduapi.entity.enums.TipoUsuario;
import com.phricardo.eduapi.exception.BusinessException;
import com.phricardo.eduapi.exception.ResourceNotFoundException;
import com.phricardo.eduapi.mapper.CourseMapper;
import com.phricardo.eduapi.repository.CourseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository courseRepository;
  private final CourseMapper courseMapper;

  @Transactional(readOnly = true)
  public List<Course> findAll() {
    return courseRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Course findById(final Long id) {
    return courseRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
  }

  @Transactional
  public Course create(final CourseRequestDto dto, final AppUser instrutor) {
    validateInstrutor(instrutor);
    Course course = courseMapper.toEntity(dto);
    course.setInstrutor(instrutor);
    return courseRepository.save(course);
  }

  @Transactional
  public Course update(final Long id, final CourseRequestDto dto, final AppUser instrutor) {
    validateInstrutor(instrutor);
    Course course = findById(id);
    validateOwner(course, instrutor);

    course.setTitulo(dto.titulo());
    course.setDescricao(dto.descricao());
    return courseRepository.save(course);
  }

  @Transactional
  public void delete(final Long id, final AppUser instrutor) {
    validateInstrutor(instrutor);
    Course course = findById(id);
    validateOwner(course, instrutor);
    courseRepository.delete(course);
  }

  private void validateInstrutor(final AppUser usuario) {
    if (usuario.getTipo() != TipoUsuario.INSTRUTOR) {
      throw new BusinessException("Apenas instrutores podem executar esta operação");
    }
  }

  public void validateOwner(final Course course, final AppUser instrutor) {
    if (!course.getInstrutor().getId().equals(instrutor.getId())) {
      throw new BusinessException("Somente o instrutor dono do curso pode alterar este recurso");
    }
  }
}
