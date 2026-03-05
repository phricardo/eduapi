package com.phricardo.eduapi.repository;

import com.phricardo.eduapi.entity.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
  @Override
  @EntityGraph(attributePaths = {"instrutor", "aulas"})
  List<Course> findAll();

  @Override
  @EntityGraph(attributePaths = {"instrutor", "aulas"})
  Optional<Course> findById(Long id);

  List<Course> findByInstrutorId(Long instrutorId);
}
