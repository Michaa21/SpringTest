package com.example.springtest.controller;

import com.example.springtest.api.AuthorApi;
import com.example.springtest.api.dto.request.AuthorCreateRequest;
import com.example.springtest.api.dto.response.AuthorResponse;
import com.example.springtest.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorController implements AuthorApi {

    private final AuthorService authorService;

    @Override
    public ResponseEntity<AuthorResponse> createAuthor(AuthorCreateRequest request) {
        return ResponseEntity
                .ok(authorService.create(request));
    }

    @Override
    public ResponseEntity<AuthorResponse> getAuthorById(Long id) {
        return ResponseEntity.ok(authorService.getById(id));
    }

    @Override
    public ResponseEntity<AuthorResponse> updateAuthor(Long id, AuthorCreateRequest request) {
        return ResponseEntity.ok(authorService.update(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteAuthor(Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

