package com.example.springtest.mapper;

import com.example.springtest.api.model.BookCreateRequest;
import com.example.springtest.api.model.BookResponse;
import com.example.springtest.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(BookCreateRequest request);
    BookResponse toResponse(Book book);
    void update(BookCreateRequest request, @MappingTarget Book book);
}
