package com.phricardo.eduapi.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(final ResourceNotFoundException ex) {
    return build(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<Map<String, Object>> handleBusiness(final BusinessException ex) {
    return build(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, Object>> handleAccessDenied(final AccessDeniedException ex) {
    return build(HttpStatus.FORBIDDEN, "Acesso negado");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(
      final MethodArgumentNotValidException ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Validation Error");

    Map<String, String> fields = new LinkedHashMap<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      fields.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    body.put("fields", fields);

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneric(final Exception ex) {
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
  }

  private ResponseEntity<Map<String, Object>> build(final HttpStatus status, final String message) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
    return ResponseEntity.status(status).body(body);
  }
}
