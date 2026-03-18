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

        if (request.getBooks() != null) {
            List<Book> books = request.getBooks()
                    .stream()
                    .map(br -> {
                        Book book = bookMapper.toEntity(br);
                        book.setAuthor(author);
                        return book;
                    })
                    .collect(Collectors.toList());

            author.setBooks(books);
        }

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

        if (request.getBooks() != null) {
            author.getBooks().clear();

            request.getBooks().forEach(br -> {
                Book book = bookMapper.toEntity(br);
                book.setAuthor(author);
                author.getBooks().add(book);
            });
        }

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

