package com.phricardo.eduapi.dto.response;

import java.util.List;

public record CourseResponseDto(
    Long id,
    String titulo,
    String descricao,
    UserSummaryDto instrutor,
    List<LessonResponseDto> aulas) {}
