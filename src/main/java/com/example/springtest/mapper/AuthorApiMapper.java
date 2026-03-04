package com.example.springtest.mapper;

import com.example.springtest.api.dto.request.AuthorCreateRequest;
import com.example.springtest.api.dto.response.AuthorResponse;
import com.example.springtest.domain.Author;
import com.example.springtest.domain.Book;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = BookMapper.class)
public interface AuthorApiMapper {
    @Mapping(target = "id", ignore = true)
    Author toEntity(AuthorCreateRequest request);

    AuthorResponse toResponse(Author author);

    @AfterMapping
    default void linkBooks(@MappingTarget Author author) {
            for (Book book : author.getBooks()) {
                book.setAuthor(author);
            }
    }

    @Mapping(target = "id", ignore = true)
    void updateFromRequest(AuthorCreateRequest request, @MappingTarget Author author);
}
