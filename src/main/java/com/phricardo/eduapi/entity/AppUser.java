package com.phricardo.eduapi.entity;

import com.phricardo.eduapi.entity.enums.TipoUsuario;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TipoUsuario tipo = TipoUsuario.ALUNO;

  @OneToMany(mappedBy = "instrutor")
  private List<Course> cursosCriados = new ArrayList<>();

  @OneToMany(mappedBy = "usuario")
  private List<Enrollment> matriculas = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "usuarios_cursos_interesse",
      joinColumns = @JoinColumn(name = "usuario_id"),
      inverseJoinColumns = @JoinColumn(name = "curso_id"))
  private Set<Course> cursosInteresse = new HashSet<>();
}
