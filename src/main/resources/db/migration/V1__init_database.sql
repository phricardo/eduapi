CREATE TABLE usuarios (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ALUNO', 'INSTRUTOR'))
);

CREATE TABLE cursos (
  id BIGSERIAL PRIMARY KEY,
  titulo VARCHAR(255) NOT NULL,
  descricao TEXT NOT NULL,
  instrutor_id BIGINT NOT NULL,
  CONSTRAINT fk_cursos_instrutor
    FOREIGN KEY (instrutor_id) REFERENCES usuarios (id)
);

CREATE TABLE aulas (
  id BIGSERIAL PRIMARY KEY,
  titulo VARCHAR(255) NOT NULL,
  duracao_minutos INTEGER NOT NULL,
  curso_id BIGINT NOT NULL,
  CONSTRAINT fk_aulas_curso
    FOREIGN KEY (curso_id) REFERENCES cursos (id)
);

CREATE TABLE matriculas (
  id BIGSERIAL PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  curso_id BIGINT NOT NULL,
  data_matricula TIMESTAMP NOT NULL,
  CONSTRAINT uk_usuario_curso UNIQUE (usuario_id, curso_id),
  CONSTRAINT fk_matriculas_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
  CONSTRAINT fk_matriculas_curso
    FOREIGN KEY (curso_id) REFERENCES cursos (id)
);

CREATE TABLE usuarios_cursos_interesse (
  curso_id BIGINT NOT NULL,
  usuario_id BIGINT NOT NULL,
  PRIMARY KEY (curso_id, usuario_id),
  CONSTRAINT fk_interesse_curso
    FOREIGN KEY (curso_id) REFERENCES cursos (id),
  CONSTRAINT fk_interesse_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);
