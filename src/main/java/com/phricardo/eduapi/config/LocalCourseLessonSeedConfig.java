package com.phricardo.eduapi.config;

import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.entity.Course;
import com.phricardo.eduapi.entity.Lesson;
import com.phricardo.eduapi.entity.enums.TipoUsuario;
import com.phricardo.eduapi.repository.AppUserRepository;
import com.phricardo.eduapi.repository.CourseRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
@RequiredArgsConstructor
public class LocalCourseLessonSeedConfig {

  private static final int TARGET_COURSES = 12;
  private static final int LESSONS_PER_COURSE = 4;
  private static final String INSTRUTOR_EMAIL = "seed.instrutor@local.test";

  private final AppUserRepository appUserRepository;
  private final CourseRepository courseRepository;

  @Bean
  public ApplicationRunner seedLocalCoursesAndLessons() {
    return args -> {
      final AppUser instrutor = getOrCreateInstrutor();
      final List<Course> existingCourses = courseRepository.findByInstrutorId(instrutor.getId());

      if (existingCourses.size() >= TARGET_COURSES) {
        return;
      }

      final Set<String> existingTitles = new HashSet<>();
      existingCourses.forEach(course -> existingTitles.add(course.getTitulo()));

      final List<Course> toSave = new ArrayList<>();

      for (int courseIndex = 1; courseIndex <= TARGET_COURSES; courseIndex++) {
        final String tituloCurso = "Curso Seed " + courseIndex;
        if (existingTitles.contains(tituloCurso)) {
          continue;
        }

        final Course course = new Course();
        course.setTitulo(tituloCurso);
        course.setDescricao(
            "Descricao do "
                + tituloCurso
                + " para ambiente local com foco em testes da API de cursos.");
        course.setInstrutor(instrutor);

        final List<Lesson> lessons = new ArrayList<>();
        for (int lessonIndex = 1; lessonIndex <= LESSONS_PER_COURSE; lessonIndex++) {
          final Lesson lesson = new Lesson();
          lesson.setTitulo("Aula " + lessonIndex + " - " + tituloCurso);
          lesson.setDuracaoMinutos(20 + (lessonIndex * 5));
          lesson.setCurso(course);
          lessons.add(lesson);
        }

        course.setAulas(lessons);
        toSave.add(course);
      }

      if (!toSave.isEmpty()) {
        courseRepository.saveAll(toSave);
      }
    };
  }

  private AppUser getOrCreateInstrutor() {
    return appUserRepository
        .findByEmail(INSTRUTOR_EMAIL)
        .orElseGet(
            () -> {
              final AppUser instrutor = new AppUser();
              instrutor.setNome("Instrutor Seed Local");
              instrutor.setEmail(INSTRUTOR_EMAIL);
              instrutor.setTipo(TipoUsuario.INSTRUTOR);
              return appUserRepository.save(instrutor);
            });
  }
}
