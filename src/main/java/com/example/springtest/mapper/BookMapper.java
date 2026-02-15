package com.example.springtest.mapper;

import com.example.springtest.api.model.BookCreateRequest;
import com.example.springtest.api.model.BookResponse;
import com.example.springtest.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(BookCreateRequest request);
    BookResponse toResponse(Book book);
}
