package com.example.spring_22.dao.author;

import com.example.spring_22.domain.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository{

    private final EntityManager entityManager;

    @Override
    public Optional<Author> getByFullName(String fullName) {
        TypedQuery<Author> query = entityManager.createQuery("select a from Author a " +
                "where a.fullName = :full_name", Author.class);
        query.setParameter("full_name", fullName);
        List<Author> authors = query.getResultList();
        return authors.isEmpty() ? Optional.empty() : Optional.of(authors.get(0));
    }

    @Override
    public Author save(Author author) {
        if (author.getId() <= 0) {
            entityManager.persist(author);
            return author;
        } else {
            return entityManager.merge(author);
        }
    }
}
