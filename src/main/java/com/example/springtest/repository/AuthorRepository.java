package com.example.springtest.repository;

import com.example.springtest.domain.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    @EntityGraph(attributePaths = "books")
    Optional<Author> findWithBooksById(UUID id);
}
