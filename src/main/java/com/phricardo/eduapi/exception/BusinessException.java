package com.phricardo.eduapi.exception;

public class BusinessException extends RuntimeException {
  public BusinessException(final String message) {
    super(message);
  }
}
