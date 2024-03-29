package com.example.spring_22.dao.author;

import com.example.spring_22.domain.Author;

import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> getByFullName(String fullName);

    Author save(Author author);
}
