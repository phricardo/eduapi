package com.phricardo.eduapi.mapper;

import com.phricardo.eduapi.dto.request.LessonRequestDto;
import com.phricardo.eduapi.dto.response.LessonResponseDto;
import com.phricardo.eduapi.entity.Lesson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {
  Lesson toEntity(LessonRequestDto dto);

  LessonResponseDto toResponse(Lesson lesson);
}
