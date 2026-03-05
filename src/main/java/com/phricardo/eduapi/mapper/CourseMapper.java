package com.phricardo.eduapi.mapper;

import com.phricardo.eduapi.dto.request.CourseRequestDto;
import com.phricardo.eduapi.dto.response.CourseResponseDto;
import com.phricardo.eduapi.entity.Course;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class, LessonMapper.class})
public interface CourseMapper {
  Course toEntity(CourseRequestDto dto);

  CourseResponseDto toResponse(Course course);
}
