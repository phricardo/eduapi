package com.phricardo.eduapi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "matriculas",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_usuario_curso",
          columnNames = {"usuario_id", "curso_id"})
    })
public class Enrollment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private AppUser usuario;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "curso_id", nullable = false)
  private Course curso;

  @Column(nullable = false)
  private LocalDateTime dataMatricula;

  @PrePersist
  public void prePersist() {
    if (dataMatricula == null) {
      dataMatricula = LocalDateTime.now();
    }
  }
}
