package com.example.spring_22.services.author;



import com.example.spring_22.domain.Author;

public interface AuthorService {

    Author saveAuthorIfNotExists(Author author);
}
