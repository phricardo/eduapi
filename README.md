![EduAPI Logo](.github/eduapi.png)

# EduAPI

API REST para plataforma de cursos online (backend only), com autenticação OAuth2 Google.

## Tecnologias
- Java 21
- Spring Boot
- Spring Security (OAuth2 Google)
- Spring Data JPA (Hibernate)
- Flyway
- PostgreSQL
- Swagger (Springdoc)

## Como executar
1. Suba o banco:
   ```bash
   docker compose up -d
   ```
2. Configure as variáveis em `.env.dev` (principalmente `GOOGLE_CLIENT_ID` e `GOOGLE_CLIENT_SECRET`).
3. Inicie a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
   No Windows PowerShell:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

## Login
Para autenticar com Google, acesse:

`http://localhost:8080/oauth2/authorization/google`

Após login, você pode validar o usuário autenticado em:

`http://localhost:8080/user/me`

## Swagger
Documentação e teste de endpoints:

`http://localhost:8080/swagger-ui/index.html`

## Uso rápido
- `GET /cursos`: lista cursos disponíveis
- `POST /cursos`: cria curso (somente `INSTRUTOR`)
- `POST /cursos/{id}/aulas`: cria aula no curso
- `POST /matriculas/{cursoId}`: matrícula em curso (somente `ALUNO`)

Se não estiver autenticado, a API retorna `401` com mensagem de login.

## Testes - Evidência
<img width="1472" height="860" alt="image" src="https://github.com/user-attachments/assets/3d4bfa24-fdf5-4288-b71a-0c3697b81478" />
