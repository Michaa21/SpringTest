package com.example.springtest.service;

import com.example.springtest.api.dto.request.AuthorCreateRequest;
import com.example.springtest.api.dto.response.AuthorResponse;
import com.example.springtest.domain.Author;
import com.example.springtest.domain.Book;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.mapper.AuthorApiMapper;
import com.example.springtest.mapper.BookMapper;
import com.example.springtest.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorApiMapper authorApiMapper;
    private final BookMapper bookMapper;

    @Transactional
    public AuthorResponse create(AuthorCreateRequest request) {
        Author author = authorApiMapper.toEntity(request);

        request.getBooks().forEach(br -> {
            Book book = bookMapper.toEntity(br);
            author.addBook(book);
        });
        Author saved = authorRepository.save(author);
        return authorApiMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AuthorResponse getById(UUID id) {
        Author author = findAuthor(id);
        return authorApiMapper.toResponse(author);
    }

    @Transactional
    public AuthorResponse update(UUID id, AuthorCreateRequest request) {
        Author author = findAuthor(id);

        authorApiMapper.updateFromRequest(request, author);

        request.getBooks().forEach(br -> {
            if (br.getId() != null) {
                Book existing = author.getBooks().stream()
                        .filter(b -> Objects.equals(b.getId(), br.getId()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Book", br.getId()));
                bookMapper.update(br, existing);
            } else {
                Book newBook = bookMapper.toEntity(br);
                author.addBook(newBook);
            }
        });

        Author saved = authorRepository.save(author);
        return authorApiMapper.toResponse(saved);
    }


    @Transactional
    public void delete(UUID id) {
        authorRepository.delete(findAuthor(id));
    }

    @Transactional
    public Author findAuthor(UUID id) {
        return authorRepository.findWithBooksById(id)
                .orElseThrow(() -> {
                    log.warn("Author with id {} not found", id);
                    return new EntityNotFoundException("Author", id);
                });
    }
}

