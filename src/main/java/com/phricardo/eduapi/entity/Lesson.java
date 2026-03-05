package com.phricardo.eduapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "aulas")
public class Lesson {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String titulo;

  @Column(nullable = false)
  private Integer duracaoMinutos;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "curso_id", nullable = false)
  private Course curso;
}
