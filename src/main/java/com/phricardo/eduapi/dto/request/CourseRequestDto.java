package com.phricardo.eduapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CourseRequestDto(
    @NotBlank(message = "Titulo é obrigatório") String titulo,
    @NotBlank(message = "Descricao é obrigatória") String descricao) {}
