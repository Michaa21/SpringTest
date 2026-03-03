package com.example.springtest.mapper;

import com.example.springtest.api.model.LessonCreateRequest;
import com.example.springtest.api.model.LessonResponse;
import com.example.springtest.domain.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    Lesson toEntity(LessonCreateRequest request);

    LessonResponse toResponse(Lesson lesson);
}
