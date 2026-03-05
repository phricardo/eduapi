package com.phricardo.eduapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.phricardo.eduapi.dto.response.CourseResponseDto;
import com.phricardo.eduapi.dto.response.EnrollmentResponseDto;
import com.phricardo.eduapi.dto.response.UserResponseDto;
import com.phricardo.eduapi.entity.AppUser;
import com.phricardo.eduapi.entity.Course;
import com.phricardo.eduapi.entity.Enrollment;
import com.phricardo.eduapi.entity.enums.TipoUsuario;
import com.phricardo.eduapi.mapper.CourseMapper;
import com.phricardo.eduapi.mapper.EnrollmentMapper;
import com.phricardo.eduapi.mapper.LessonMapper;
import com.phricardo.eduapi.mapper.UserMapper;
import com.phricardo.eduapi.security.CustomOAuth2UserService;
import com.phricardo.eduapi.service.AppUserService;
import com.phricardo.eduapi.service.AuthenticatedUserService;
import com.phricardo.eduapi.service.CourseService;
import com.phricardo.eduapi.service.EnrollmentService;
import com.phricardo.eduapi.service.LessonService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiEndpointsWebTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CustomOAuth2UserService customOAuth2UserService;

  @MockBean private AuthenticatedUserService authenticatedUserService;
  @MockBean private AppUserService appUserService;
  @MockBean private CourseService courseService;
  @MockBean private LessonService lessonService;
  @MockBean private EnrollmentService enrollmentService;

  @MockBean private CourseMapper courseMapper;
  @MockBean private LessonMapper lessonMapper;
  @MockBean private EnrollmentMapper enrollmentMapper;
  @MockBean private UserMapper userMapper;

  @Test
  void getCursosShouldReturn401WhenUnauthenticated() throws Exception {
    mockMvc
        .perform(get("/cursos"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("login")));
  }

  @Test
  void getCursosShouldReturn200WhenAuthenticated() throws Exception {
    final Course course = new Course();
    course.setId(1L);
    when(courseService.findAll()).thenReturn(List.of(course));
    when(courseMapper.toResponse(any(Course.class)))
        .thenReturn(new CourseResponseDto(1L, "Curso", "Desc", null, List.of()));

    mockMvc
        .perform(
            get("/cursos")
                .with(
                    oauth2Login()
                        .authorities(new SimpleGrantedAuthority("ROLE_ALUNO"))
                        .attributes(attrs -> attrs.put("email", "a@a.com"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].titulo").value("Curso"));
  }

  @Test
  void postCursosShouldReturn403ForAluno() throws Exception {
    mockMvc
        .perform(
            post("/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Curso\",\"descricao\":\"Desc\"}")
                .with(
                    oauth2Login()
                        .authorities(new SimpleGrantedAuthority("ROLE_ALUNO"))
                        .attributes(attrs -> attrs.put("email", "aluno@test.com"))))
        .andExpect(status().isForbidden());
  }

  @Test
  void postCursosShouldReturn201ForInstrutor() throws Exception {
    final AppUser instrutor = new AppUser();
    instrutor.setId(1L);
    instrutor.setTipo(TipoUsuario.INSTRUTOR);
    final Course course = new Course();
    course.setId(1L);

    when(authenticatedUserService.fromPrincipal(any())).thenReturn(instrutor);
    when(courseService.create(any(), any())).thenReturn(course);
    when(courseMapper.toResponse(any(Course.class)))
        .thenReturn(new CourseResponseDto(1L, "Curso", "Desc", null, List.of()));

    mockMvc
        .perform(
            post("/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Curso\",\"descricao\":\"Desc\"}")
                .with(
                    oauth2Login()
                        .authorities(new SimpleGrantedAuthority("ROLE_INSTRUTOR"))
                        .attributes(attrs -> attrs.put("email", "instrutor@test.com"))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  void postCursosShouldReturn400WhenPayloadInvalid() throws Exception {
    mockMvc
        .perform(
            post("/cursos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"\",\"descricao\":\"\"}")
                .with(
                    oauth2Login()
                        .authorities(new SimpleGrantedAuthority("ROLE_INSTRUTOR"))
                        .attributes(attrs -> attrs.put("email", "instrutor@test.com"))))
        .andExpect(status().isBadRequest());
  }

  @Test
  void postMatriculasShouldReturn403ForInstrutor() throws Exception {
    mockMvc
        .perform(
            post("/matriculas/1")
                .with(
                    oauth2Login()
                        .authorities(new SimpleGrantedAuthority("ROLE_INSTRUTOR"))
                        .attributes(attrs -> attrs.put("email", "instrutor@test.com"))))
        .andExpect(status().isForbidden());
  }

  @Test
  void postMatriculasShouldReturn201ForAluno() throws Exception {
    final AppUser aluno = new AppUser();
    aluno.setId(1L);
    aluno.setTipo(TipoUsuario.ALUNO);
    final Enrollment enrollment = new Enrollment();
    enrollment.setId(1L);

    when(authenticatedUserService.fromPrincipal(any())).thenReturn(aluno);
    when(enrollmentService.enroll(any(Long.class), any(AppUser.class))).thenReturn(enrollment);
    when(enrollmentMapper.toResponse(any(Enrollment.class)))
        .thenReturn(new EnrollmentResponseDto(1L, null, null, null));

    mockMvc
        .perform(
            post("/matriculas/1")
                .with(
                    oauth2Login()
                        .authorities(new SimpleGrantedAuthority("ROLE_ALUNO"))
                        .attributes(attrs -> attrs.put("email", "aluno@test.com"))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  void getUserMeShouldReturn200WhenAuthenticated() throws Exception {
    final AppUser user = new AppUser();
    user.setId(1L);
    user.setNome("Teste");
    user.setEmail("teste@test.com");
    when(authenticatedUserService.fromPrincipal(any())).thenReturn(user);
    when(userMapper.toResponse(any(AppUser.class)))
        .thenReturn(new UserResponseDto(1L, "Teste", "teste@test.com", "ALUNO"));

    mockMvc
        .perform(
            get("/user/me")
                .with(
                    oauth2Login()
                        .authorities(new SimpleGrantedAuthority("ROLE_ALUNO"))
                        .attributes(attrs -> attrs.put("email", "teste@test.com"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("teste@test.com"));
  }

  @Test
  void getUsuariosShouldReturn401WhenUnauthenticated() throws Exception {
    mockMvc.perform(get("/usuarios")).andExpect(status().isUnauthorized());
  }
}
