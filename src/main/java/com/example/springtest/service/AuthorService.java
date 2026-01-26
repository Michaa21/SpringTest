package com.example.springtest.service;

import com.example.springtest.model.Author;
import com.example.springtest.model.Book;
import com.example.springtest.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author createAuthor(Author author) {
        if (author.getBooks() != null) {
            for (Book book : author.getBooks()) {
                book.setAuthor(author);
            }
        }
        return authorRepository.save(author);
    }

    @Transactional(readOnly = true)
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автор с id " + id +
                        " не найден"));
    }

    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = getAuthorById(id);

        if (authorDetails.getName() != null) {
            author.setName(authorDetails.getName());
        }

        if (authorDetails.getBooks() != null) {
            author.getBooks().clear();
            for (Book book : authorDetails.getBooks()) {
                book.setAuthor(author);
                author.getBooks().add(book);
            }
        }

        return author;
    }

    public void deleteAuthor(Long id) {
        authorRepository.delete(getAuthorById(id));
    }
}
