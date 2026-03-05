package com.phricardo.eduapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LessonRequestDto(
    @NotBlank(message = "Titulo é obrigatório") String titulo,
    @NotNull(message = "Duracao é obrigatória")
        @Min(value = 1, message = "Duracao deve ser maior que zero")
        Integer duracaoMinutos) {}
