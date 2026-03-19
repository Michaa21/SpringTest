package com.example.springtest.mapper;

import com.example.springtest.api.dto.request.LessonCreateRequest;
import com.example.springtest.api.dto.response.LessonResponse;
import com.example.springtest.domain.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    Lesson toEntity(LessonCreateRequest request);

    LessonResponse toResponse(Lesson lesson);
}
