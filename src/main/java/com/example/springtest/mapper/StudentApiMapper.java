package com.example.springtest.mapper;

import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.domain.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = LessonMapper.class)
public interface StudentApiMapper {
    @Mapping(target = "id", ignore = true)
    Student toEntity(StudentCreateRequest request);
    StudentResponse toResponse(Student student);
}
