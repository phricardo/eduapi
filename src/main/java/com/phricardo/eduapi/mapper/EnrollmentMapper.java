package com.phricardo.eduapi.mapper;

import com.phricardo.eduapi.dto.response.EnrollmentResponseDto;
import com.phricardo.eduapi.entity.Enrollment;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class, CourseMapper.class})
public interface EnrollmentMapper {
  EnrollmentResponseDto toResponse(Enrollment enrollment);
}
