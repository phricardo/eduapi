package com.phricardo.eduapi.mapper;

import com.phricardo.eduapi.dto.response.UserResponseDto;
import com.phricardo.eduapi.dto.response.UserSummaryDto;
import com.phricardo.eduapi.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "tipo", expression = "java(user.getTipo().name())")
  UserResponseDto toResponse(AppUser user);

  @Mapping(target = "tipo", expression = "java(user.getTipo().name())")
  UserSummaryDto toSummary(AppUser user);
}
