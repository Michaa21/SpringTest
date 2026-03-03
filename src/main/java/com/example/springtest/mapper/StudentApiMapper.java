package com.example.springtest.mapper;

import com.example.springtest.api.model.StudentCreateRequest;
import com.example.springtest.api.model.StudentResponse;
import com.example.springtest.domain.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LessonMapper.class)
public interface StudentApiMapper {
    @Mapping(target = "id", ignore = true)
    Student toEntity(StudentCreateRequest request);
    StudentResponse toResponse(Student student);
}
