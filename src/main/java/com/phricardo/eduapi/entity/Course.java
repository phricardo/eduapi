package com.phricardo.eduapi.entity;

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
@Table(name = "cursos")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String titulo;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String descricao;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "instrutor_id", nullable = false)
  private AppUser instrutor;

  @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Lesson> aulas = new ArrayList<>();

  @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Enrollment> matriculas = new ArrayList<>();

  @ManyToMany(mappedBy = "cursosInteresse")
  private Set<AppUser> interessados = new HashSet<>();
}
