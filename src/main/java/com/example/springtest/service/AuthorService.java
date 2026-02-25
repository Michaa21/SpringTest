package com.example.springtest.service;

import com.example.springtest.api.model.AuthorCreateRequest;
import com.example.springtest.api.model.AuthorResponse;
import com.example.springtest.exception.EntityNotFoundException;
import com.example.springtest.mapper.AuthorApiMapper;
import com.example.springtest.model.Author;
import com.example.springtest.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorApiMapper authorApiMapper;

    @Transactional
    public AuthorResponse create(AuthorCreateRequest request) {
        Author author = authorApiMapper.toEntity(request);
        Author saved = authorRepository.save(author);
        return authorApiMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AuthorResponse getById(Long id) {
        Author author = findAuthor(id);
        return authorApiMapper.toResponse(author);
    }

    @Transactional
    public AuthorResponse update(Long id, AuthorCreateRequest request) {
        Author author = findAuthor(id);
        authorApiMapper.updateFromRequest(request, author);
        /*author.getBooks().clear();
        for (BookCreateRequest bookDto : request.getBooks()) {
            Book book = new Book();
            book.setTitle(bookDto.getTitle());
            book.setAuthor(author);
            author.getBooks().add(book);
        }*/
        return authorApiMapper.toResponse(author);
    }

    @Transactional
    public void delete(Long id) {
        authorRepository.delete(findAuthor(id));
    }

    private Author findAuthor(Long id) {
        return authorRepository.findWithBooksById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author", id));
    }
}

