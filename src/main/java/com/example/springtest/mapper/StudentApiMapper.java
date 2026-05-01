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
    Student toEntity(StudentCreateRequest request);

    @Mapping(target = "extra", source = "extra")
    StudentResponse toResponse(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "extra", ignore = true)
    void update(StudentCreateRequest request, @MappingTarget Student student);

    @Mapping(target = "extra", source = "extraInfo")
    void updateFromExternalResponse(
            ExternalStudentResponse externalResponse,
            @MappingTarget StudentResponse studentResponse
    );
}
