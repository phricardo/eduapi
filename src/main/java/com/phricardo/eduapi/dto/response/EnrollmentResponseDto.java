package com.phricardo.eduapi.dto.response;

import java.time.LocalDateTime;

public record EnrollmentResponseDto(
    Long id, UserSummaryDto usuario, CourseResponseDto curso, LocalDateTime dataMatricula) {}
