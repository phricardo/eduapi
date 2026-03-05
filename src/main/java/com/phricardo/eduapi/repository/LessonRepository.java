package com.phricardo.eduapi.repository;

import com.phricardo.eduapi.entity.Lesson;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
  List<Lesson> findByCursoId(Long cursoId);
}
