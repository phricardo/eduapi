package com.phricardo.eduapi.repository;

import com.phricardo.eduapi.entity.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
  List<Course> findByInstrutorId(Long instrutorId);
}
