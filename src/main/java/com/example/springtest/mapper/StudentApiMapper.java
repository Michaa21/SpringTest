package com.example.springtest.mapper;

import com.example.springtest.api.dto.request.StudentCreateRequest;
import com.example.springtest.api.dto.response.ExternalStudentResponse;
import com.example.springtest.api.dto.response.StudentResponse;
import com.example.springtest.domain.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = LessonMapper.class)
public interface StudentApiMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "extra", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "age", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    Student toEntity(StudentCreateRequest request);

    StudentResponse toResponse(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "extra", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "age", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    void update(StudentCreateRequest request, @MappingTarget Student student);

    @Mapping(target = "extra", source = "extraInfo")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    void updateFromExternalResponse(
            ExternalStudentResponse externalResponse,
            @MappingTarget Student student
    );
}